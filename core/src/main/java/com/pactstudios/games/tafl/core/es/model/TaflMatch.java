package com.pactstudios.games.tafl.core.es.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.RulesEngineType;
import com.pactstudios.games.tafl.core.es.model.ai.AiFactory;
import com.pactstudios.games.tafl.core.es.model.ai.AiStrategy;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.GameBoard;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;
import com.pactstudios.games.tafl.core.es.model.rules.RulesFactory;
import com.pactstudios.games.tafl.core.utils.TaflPreferenceService;

public class TaflMatch {

    public String name;
    public LifeCycle status;
    public int turn = Constants.BoardConstants.NO_TEAM;
    public RulesEngineType rulesType;

    public long created;
    public long updated;
    public float timer;

    public boolean versusComputer;
    public int computerTeam = Constants.BoardConstants.NO_TEAM;
    public AiType aiType;

    public String boardRepresentation;
    public Array<Move> initialUndoStack;

    public TaflBoard board;
    public AiStrategy aiStrategy;

    public boolean computerStarts;

    public Array<TaflMatchObserver> observers;

    public TaflMatch() {
        observers = new Array<TaflMatchObserver>();
    }

    public void initialize(TaflPreferenceService preferenceService, TaflMatchObserver...observers) {
        initializeComponents();
        intializeTurn();

        registerObservers(preferenceService, observers);
        initializeMatch();
    }

    private void intializeTurn() {
        if (turn == Constants.BoardConstants.NO_TEAM) {
            turn = board.rules.getFirstTurn();
        }
        if (versusComputer && computerTeam == Constants.BoardConstants.NO_TEAM) {
            int firstTurn = board.rules.getFirstTurn();
            int secondTurn = (firstTurn + 1) % 2;
            computerTeam = computerStarts ? firstTurn : secondTurn;
        }
    }

    private void initializeComponents() {
        Move.movePool = new Pool<Move>() {
            @Override
            protected Move newObject() {
                return new Move(board.boardSize);
            }
        };

        int boardSize = boardRepresentation.length();

        ZorbistHash hash = new ZorbistHash(GameBoard.NUMBER_OF_TEAMS,
                boardSize);
        hash.generate();

        board = new TaflBoard((int)Math.sqrt(boardRepresentation.length()),
                GameBoard.NUMBER_OF_TEAMS,
                hash,
                RulesFactory.getRules(rulesType));
        if (initialUndoStack != null && initialUndoStack.size > 0) {
            board.undoStack.addAll(initialUndoStack);
            initialUndoStack.clear();
        }

        aiStrategy = AiFactory.getAiStrategy(aiType,
                board.rules, boardSize);
    }

    private void initializeMatch() {
        addPieces();
        for (TaflMatchObserver observer : observers) {
            observer.initializeMatch(this);
        }
    }

    private void registerObservers(TaflPreferenceService preferenceService,
            TaflMatchObserver... observers) {
        registerObserver(board.rules);
        for (TaflMatchObserver observer : observers) {
            registerObserver(observer);
        }
        registerObserver(preferenceService);
    }

    public void registerObserver(TaflMatchObserver observer) {
        observers.add(observer);
    }

    private void addPieces() {
        for (int i = 0; i < boardRepresentation.length(); i++) {
            char current = boardRepresentation.charAt(i);
            if (current == Constants.BoardConstants.WHITE_PIECE) {
                board.addPiece(Constants.BoardConstants.WHITE_TEAM, i);
            } else if (current == Constants.BoardConstants.BLACK_PIECE) {
                board.addPiece(Constants.BoardConstants.BLACK_TEAM, i);
            } else if (current == Constants.BoardConstants.KING_PIECE) {
                board.king = i;
                board.addPiece(Constants.BoardConstants.WHITE_TEAM, i);
            }
        }
    }

    @Override
    public int hashCode() {
        return name == null ? 0 : name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other != null &&
                other instanceof TaflMatch &&
                name != null &&
                name.equals(((TaflMatch)other).name);
    }

    @Override
    public String toString() {
        return boardRepresentation;
    }

    public void applyMove(Move move, boolean simulate) {
        board.applyMove(move, simulate);
        if (!simulate) {
            for (TaflMatchObserver observer : observers) {
                observer.applyMove(this, move);
            }
        }
    }

    public Move undoMove() {
        Move move = board.undoMove();
        if (move != null) {
            for (TaflMatchObserver observer : observers) {
                observer.undoMove(this, move);
            }
            changeTurn();
            return move;
        }
        return null;
    }

    public void removePieces(int team, BitBoard capturedPieces) {
        board.removePieces(team, capturedPieces);

        for (TaflMatchObserver observer : observers) {
            observer.removePieces(this, team, capturedPieces);
        }
    }

    public boolean acceptInput() {
        return !versusComputer || turn != computerTeam;
    }

    public BitBoard currentBitBoard() {
        return board.bitBoards[turn];
    }

    public void changeTurn() {
        turn = (turn + 1) % 2;

        for (TaflMatchObserver observer : observers) {
            observer.changeTurn(this);
        }
    }

    public void gameOver(LifeCycle status) {
        this.status = status;
        for (TaflMatchObserver observer : observers) {
            observer.gameOver(this, status);
        }
    }
}
