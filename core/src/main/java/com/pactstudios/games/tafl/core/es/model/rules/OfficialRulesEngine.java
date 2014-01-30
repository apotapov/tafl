package com.pactstudios.games.tafl.core.es.model.rules;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.GameBoard;
import com.pactstudios.games.tafl.core.es.model.board.cells.CornerCell;
import com.pactstudios.games.tafl.core.es.model.board.cells.KingCell;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.board.cells.RegularCell;
import com.pactstudios.games.tafl.core.es.model.objects.GamePiece;
import com.pactstudios.games.tafl.core.es.model.objects.PieceType;
import com.pactstudios.games.tafl.core.es.model.objects.Team;

public class OfficialRulesEngine extends RulesEngine {

    Array<ModelCell> legalMoves;
    Array<GamePiece> capturedPieces;

    public OfficialRulesEngine(TaflMatch match) {
        super(match);
        legalMoves = new Array<ModelCell>();
        capturedPieces = new Array<GamePiece>();

        populateBoard();
    }

    @Override
    public Team checkWinner(ModelCell end, Array<GamePiece> capturedPieces) {
        Team winner = null;
        if (checkCaptureKing(capturedPieces)) {
            winner = Team.BLACK;
        } else if (checkKingEscaped(end)) {
            winner = Team.WHITE;
        }
        return winner;
    }

    @Override
    public Team checkWinner() {
        Team winner = null;
        if (checkCaptureKing()) {
            winner = Team.BLACK;
        } else if (checkKingEscaped()) {
            winner = Team.WHITE;
        }
        return winner;
    }

    private boolean checkCaptureKing() {
        for (int i = 0; i < match.board.dimensions; i++) {
            for (int j = 0; j < match.board.dimensions; j++) {
                ModelCell cell = match.board.getCell(i, j);
                if (cell.piece != null && cell.piece.type == PieceType.KING) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkKingEscaped() {
        return checkKingEscaped(match.board.cornerCells[0]) ||
                checkKingEscaped(match.board.cornerCells[1]) ||
                checkKingEscaped(match.board.cornerCells[2]) ||
                checkKingEscaped(match.board.cornerCells[3]);
    }



    private boolean checkCaptureKing(Array<GamePiece> capturedPieces) {
        for (GamePiece piece : capturedPieces) {
            if (piece.type == PieceType.KING) {
                return true;
            }
        }
        return false;
    }

    private boolean checkKingEscaped(ModelCell end) {
        return end != null && end instanceof CornerCell && end.piece != null && end.piece.type == PieceType.KING;
    }

    @Override
    public Array<GamePiece> getCapturedPieces(ModelCell end) {
        capturedPieces.clear();

        checkCaptureUp(end);
        checkCaptureDown(end);
        checkCaptureRight(end);
        checkCaptureLeft(end);

        return capturedPieces;
    }

    private void checkCaptureLeft(ModelCell end) {
        ModelCell first = match.board.getCell(end.x - 1, end.y);
        ModelCell second = match.board.getCell(end.x - 2, end.y);
        ModelCell third = match.board.getCell(end.x - 1, end.y + 1);
        ModelCell fourth = match.board.getCell(end.x - 1, end.y - 1);
        checkCapture(end.piece, first, second, third, fourth);
    }

    private void checkCaptureRight(ModelCell end) {
        ModelCell first = match.board.getCell(end.x + 1, end.y);
        ModelCell second = match.board.getCell(end.x + 2, end.y);
        ModelCell third = match.board.getCell(end.x + 1, end.y + 1);
        ModelCell fourth = match.board.getCell(end.x + 1, end.y - 1);
        checkCapture(end.piece, first, second, third, fourth);
    }

    private void checkCaptureDown(ModelCell end) {
        ModelCell first = match.board.getCell(end.x, end.y - 1);
        ModelCell second = match.board.getCell(end.x, end.y - 2);
        ModelCell third = match.board.getCell(end.x + 1, end.y - 1);
        ModelCell fourth = match.board.getCell(end.x - 1, end.y - 1);
        checkCapture(end.piece, first, second, third, fourth);
    }

    private void checkCaptureUp(ModelCell end) {
        ModelCell first = match.board.getCell(end.x, end.y + 1);
        ModelCell second = match.board.getCell(end.x, end.y + 2);
        ModelCell third = match.board.getCell(end.x + 1, end.y + 1);
        ModelCell fourth = match.board.getCell(end.x - 1, end.y + 1);
        checkCapture(end.piece, first, second, third, fourth);
    }

    private void checkCapture(GamePiece piece, ModelCell first, ModelCell second, ModelCell third, ModelCell fourth) {
        try {
            if (first != null && first.piece != null && piece.type.team != first.piece.type.team) {
                if (first.piece.type != PieceType.KING) {
                    if (isHostile(piece, second)) {
                        capturedPieces.add(first.piece);
                    }
                } else {
                    if (isKingHostile(piece, second) && isKingHostile(piece, third) && isKingHostile(piece, fourth)) {
                        capturedPieces.add(first.piece);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isHostile(GamePiece piece, ModelCell oppositeCell) {
        return oppositeCell != null &&
                ((oppositeCell.piece != null && piece.type.team == oppositeCell.piece.type.team) ||
                        (!oppositeCell.canWalk() &&
                                (oppositeCell.piece == null || piece.type.team == oppositeCell.piece.type.team)));
    }

    private boolean isKingHostile(GamePiece piece, ModelCell oppositeCell) {
        return oppositeCell != null && oppositeCell.piece != null && piece.type.team == oppositeCell.piece.type.team;
    }

    @Override
    public boolean legalMove(GamePiece piece, ModelCell start, ModelCell end) {
        if (end.piece == null && (end.canWalk() || piece.type == PieceType.KING)) {
            if (start.x == end.x) {
                int increment = Integer.signum(end.y - start.y);
                for (int i = start.y + increment; i != end.y; i += increment) {
                    ModelCell examined = match.board.getCell(start.x, i);
                    if (examined.piece != null) {
                        return false;
                    }
                }
                return true;
            } else if (start.y == end.y) {
                int increment = Integer.signum(end.x - start.x);
                for (int i = start.x + increment; i != end.x; i += increment) {
                    ModelCell examined = match.board.getCell(i, start.y);
                    if (examined.piece != null) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public Array<ModelCell> legalMoves(ModelCell start) {
        legalMoves.clear();

        legalUp(start);
        legalDown(start);
        legalRight(start);
        legalLeft(start);

        return legalMoves;
    }

    private void legalUp(ModelCell start) {
        ModelCell next = start;
        while ((next = next.up()) != null && next.piece == null) {
            if (next.canWalk() || start.piece.type == PieceType.KING) {
                legalMoves.add(next);
            }
        }
    }

    private void legalDown(ModelCell start) {
        ModelCell next = start;
        while ((next = next.down()) != null && next.piece == null) {
            if (next.canWalk() || start.piece.type == PieceType.KING) {
                legalMoves.add(next);
            }
        }
    }

    private void legalRight(ModelCell start) {
        ModelCell next = start;
        while ((next = next.right()) != null && next.piece == null) {
            if (next.canWalk() || start.piece.type == PieceType.KING) {
                legalMoves.add(next);
            }
        }
    }

    private void legalLeft(ModelCell start) {
        ModelCell next = start;
        while ((next = next.left()) != null && next.piece == null) {
            if (next.canWalk() || start.piece.type == PieceType.KING) {
                legalMoves.add(next);
            }
        }
    }

    @Override
    public void populateBoard() {
        match.board.cells = new ModelCell[match.board.dimensions][match.board.dimensions];
        for (int i = 0; i < match.board.dimensions; i++) {
            for (int j = 0; j < match.board.dimensions; j++) {
                ModelCell cell = createCell(match.board, i, j);
                match.board.cells[i][j] = cell;
            }
        }
        match.board.cornerCells[0] = match.board.cells[0][0];
        match.board.cornerCells[1] = match.board.cells[0][match.board.dimensions - 1];
        match.board.cornerCells[2] = match.board.cells[match.board.dimensions - 1][0];
        match.board.cornerCells[3] = match.board.cells[match.board.dimensions - 1][match.board.dimensions - 1];
    }

    protected ModelCell createCell(GameBoard board, int x, int y) {
        if ((x == 0 && y == 0) ||
                (x == board.dimensions - 1 && y == 0) ||
                (x == 0 && y == board.dimensions - 1) ||
                (x == board.dimensions -1 && y == board.dimensions -1)) {
            return new CornerCell(x, y, board);
        } else if (x == board.dimensions / 2 && y == board.dimensions / 2) {
            return new KingCell(x, y, board);
        } else {
            return new RegularCell(x, y, board);
        }
    }

    @Override
    public Team getFirstTurn() {
        return Team.BLACK;
    }

    @Override
    public Team getSecondTurn() {
        return Team.WHITE;
    }
}
