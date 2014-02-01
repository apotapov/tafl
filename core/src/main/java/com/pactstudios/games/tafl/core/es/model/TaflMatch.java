package com.pactstudios.games.tafl.core.es.model;

import java.util.BitSet;
import java.util.Date;

import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.AiType;
import com.pactstudios.games.tafl.core.enums.GameBoardType;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.RulesEngineType;
import com.pactstudios.games.tafl.core.enums.Team;
import com.pactstudios.games.tafl.core.es.model.ai.AiFactory;
import com.pactstudios.games.tafl.core.es.model.ai.AiStrategy;
import com.pactstudios.games.tafl.core.es.model.board.GameBitBoard;
import com.pactstudios.games.tafl.core.es.model.board.Move;
import com.pactstudios.games.tafl.core.es.model.log.MatchLogEntry;
import com.pactstudios.games.tafl.core.es.model.rules.RulesEngine;
import com.pactstudios.games.tafl.core.es.model.rules.RulesFactory;
import com.pactstudios.games.tafl.core.utils.TaflDatabaseService;

@DatabaseTable(tableName = Constants.DbConstants.MATCH_TABLE)
public class TaflMatch {

    public static final String ID_COLUMN = "_id";
    public static final String NAME_COLUMN = "name";
    public static final String STATUS_COLUMN = "status";
    public static final String TURN_COLUMN = "turn";
    public static final String BOARD_TYPE = "boardType";
    public static final String RULE_ENGINE_COLUMN = "rules";
    public static final String CREATED_COLUMN = "created";
    public static final String UPDATED_COLUMN = "updated";
    public static final String TIMER_COLUMN = "timer";
    public static final String VERSUS_COMPUTER_COLUMN = "versus_computer";
    public static final String COMPUTER_TEAM_COLUMN = "computer_team";
    public static final String AI_TYPE_COLUMN = "ai_type";
    public static final String WHITE_PIECES_COLUMN = "white_pieces";
    public static final String BLACK_PIECES_COLUMN = "black_pieces";
    public static final String KING_COLUMN = "king";

    // id is generated by the database and set on the object automagically
    @DatabaseField(generatedId = true)
    public int _id;

    @DatabaseField(columnName = NAME_COLUMN, canBeNull = false)
    public String name;

    @DatabaseField(columnName = STATUS_COLUMN, canBeNull = false)
    public LifeCycle status;

    @DatabaseField(columnName = TURN_COLUMN, canBeNull = false)
    public Team turn;

    @DatabaseField(columnName = BOARD_TYPE, canBeNull = false)
    public GameBoardType boardType;

    @DatabaseField(columnName = RULE_ENGINE_COLUMN, canBeNull = false)
    public RulesEngineType rulesType;

    @DatabaseField(columnName = CREATED_COLUMN, canBeNull = false)
    public Date created;

    @DatabaseField(columnName = UPDATED_COLUMN, canBeNull = false)
    public Date updated;

    @DatabaseField(columnName = TIMER_COLUMN, canBeNull = false)
    public float timer;

    @DatabaseField(columnName = VERSUS_COMPUTER_COLUMN, canBeNull = false)
    public boolean versusComputer;

    @DatabaseField(columnName = COMPUTER_TEAM_COLUMN)
    public Team computerTeam;

    @DatabaseField(columnName = AI_TYPE_COLUMN, canBeNull = false)
    public AiType aiType;

    @DatabaseField(columnName = WHITE_PIECES_COLUMN, canBeNull = false)
    public String whitePieces;

    @DatabaseField(columnName = BLACK_PIECES_COLUMN, canBeNull = false)
    public String blackPieces;

    @DatabaseField(columnName = KING_COLUMN, canBeNull = false)
    private int king;

    @ForeignCollectionField
    public ForeignCollection<MatchLogEntry> persistedLog;

    public RulesEngine rulesEngine;
    public TaflBoard board;
    public AiStrategy aiStrategy;

    public Entity[] pieceEntities;

    public Array<Move> undoStack;
    public Array<Move> simulatedMoves;

    public boolean computerStarts;

    public TaflMatch() {
        undoStack = new Array<Move>();
        simulatedMoves = new Array<Move>();
    }

    public void initialize(TaflDatabaseService dbService) {
        board = new TaflBoard(boardType.dimensions,
                GameBitBoard.NUMBER_OF_TEAMS,
                dbService.hashs.get(boardType.dimensions),
                king);

        board.stringToBitSet(whitePieces, Team.WHITE.bitBoardId());
        board.stringToBitSet(blackPieces, Team.BLACK.bitBoardId());

        board.initialize();

        rulesEngine = RulesFactory.getRules(rulesType, this);
        aiStrategy = AiFactory.getAiStrategy(aiType);

        pieceEntities = new Entity[board.numberCells];

        if (turn == null) {
            turn = rulesEngine.getFirstTurn();
        }
        if (computerTeam == null) {
            computerTeam = computerStarts ? rulesEngine.getFirstTurn() :
                rulesEngine.getSecondTurn();
        }
        if (_id == 0) {
            dbService.createMatch(this);
        }

        rulesEngine.calculateLegalMoves();
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
        return Integer.toString(_id);
    }

    public void removePiece(int captor, int capturedPiece) {
        board.removePiece(captor, capturedPiece);

        Entity e = pieceEntities[capturedPiece];
        if (e != null) {
            e.deleteFromWorld();
            pieceEntities[capturedPiece] = null;
        }
    }

    public void simulateMove(Move move) {
        if (move != null) {
            applyMove(move, true);
            move.capturedPieces.clear();
            move.capturedPieces.or(rulesEngine.getCapturedPieces(move.destination));
            board.bitBoards[(move.pieceType + 1) % 2].andNot(move.capturedPieces);
            simulatedMoves.add(move);
        }
    }

    public void rollBackSimulatedMove() {
        if (simulatedMoves.size > 0) {
            Move move = simulatedMoves.pop();
            undoMove(move, true);
            board.bitBoards[(move.pieceType + 1) % 2].or(move.capturedPieces);
        }
    }

    public void applyMove(Move move, boolean simulate) {
        board.applyMove(move);
        if (!simulate) {
            Entity e = pieceEntities[move.source];
            pieceEntities[move.source] = null;
            pieceEntities[move.destination] = e;

            undoStack.add(move.clone());
            rulesEngine.recordBoardConfiguration(board.hashCode());
        }
    }

    public void undoMove(Move move, boolean simulate) {
        board.undoMove(move);
        if (!simulate) {
            Entity e = pieceEntities[move.destination];
            pieceEntities[move.destination] = null;
            pieceEntities[move.source] = e;

            rulesEngine.undoBoardConfiguration();
        }
    }

    public Move undoMove() {
        if (undoStack.size > 0) {
            Move move = undoStack.pop();
            undoMove(move, false);
            return move;
        }
        return null;
    }

    public boolean acceptInput() {
        return !versusComputer || turn != computerTeam;
    }

    public BitSet currentBitBoard() {
        return board.bitBoards[turn.bitBoardId()];
    }

    public void updateKing(int king) {
        this.king = king;
    }
}
