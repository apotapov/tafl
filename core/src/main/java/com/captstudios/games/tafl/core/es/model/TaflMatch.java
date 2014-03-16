package com.captstudios.games.tafl.core.es.model;

import com.badlogic.gdx.utils.Array;
import com.captstudios.games.tafl.core.consts.Constants;
import com.captstudios.games.tafl.core.enums.AiType;
import com.captstudios.games.tafl.core.enums.LifeCycle;
import com.captstudios.games.tafl.core.enums.RulesEngineType;
import com.captstudios.games.tafl.core.es.model.ai.AiFactory;
import com.captstudios.games.tafl.core.es.model.ai.AiStrategy;
import com.captstudios.games.tafl.core.es.model.ai.optimization.BitBoard;
import com.captstudios.games.tafl.core.es.model.ai.optimization.moves.Move;
import com.captstudios.games.tafl.core.es.model.ai.optimization.transposition.ZorbistHash;
import com.captstudios.games.tafl.core.es.model.rules.RulesFactory;

public class TaflMatch {

    public String name;
    public LifeCycle status;
    public int turn = Constants.BoardConstants.NO_TEAM;
    public RulesEngineType rulesType;

    public long created;
    public long updated;

    public int computerTeam = Constants.BoardConstants.NO_TEAM;

    public String boardRepresentation;
    public Array<Move> initialUndoStack;

    public TaflBoard board;
    public AiStrategy aiStrategy;
    public AiStrategy hintStrategy;

    public boolean computerStarts;

    public Array<TaflMatchObserver> observers;

    public TaflMatch() {
        observers = new Array<TaflMatchObserver>();
    }

    public void initialize(TaflMatchObserver...observers) {
        initializeComponents();
        intializeTurn();

        registerObservers(observers);
        initializeMatch();
    }

    private void intializeTurn() {
        if (turn == Constants.BoardConstants.NO_TEAM) {
            turn = board.rules.getFirstTurn();
        }
        if (computerTeam == Constants.BoardConstants.NO_TEAM) {
            int firstTurn = board.rules.getFirstTurn();
            int secondTurn = (firstTurn + 1) % 2;
            computerTeam = computerStarts ? firstTurn : secondTurn;
        }
    }

    private void initializeComponents() {
        int boardSize = boardRepresentation.length();

        ZorbistHash hash = new ZorbistHash(Constants.BoardConstants.PIECE_TYPES,
                boardSize);
        hash.generate();

        board = new TaflBoard((int)Math.sqrt(boardRepresentation.length()),
                Constants.BoardConstants.PIECE_TYPES,
                hash,
                RulesFactory.getRules(rulesType));
        if (initialUndoStack != null && initialUndoStack.size > 0) {
            for (Move move : initialUndoStack) {
                board.undoStack.add(board.movePool.obtain().populate(move));
            }
            initialUndoStack.clear();
        }

        hintStrategy = AiFactory.getAiStrategy(AiType.AI_INTERMEDIATE, board);
    }

    private void initializeMatch() {
        addPieces();
        for (TaflMatchObserver observer : observers) {
            observer.initializeMatch(this);
        }
    }

    private void registerObservers(TaflMatchObserver... observers) {
        registerObserver(board.rules);
        for (TaflMatchObserver observer : observers) {
            registerObserver(observer);
        }
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
                board.addPiece(Constants.BoardConstants.KING, i);
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
        return turn != computerTeam;
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
