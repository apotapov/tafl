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
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;

public class BasicRulesEngine extends RulesEngine {

    Array<ModelCell> legalMoves;
    Array<ModelCell> capturedPieces;

    public BasicRulesEngine(TaflMatch match) {
        super(match, Team.BLACK);
        legalMoves = new Array<ModelCell>();
        capturedPieces = new Array<ModelCell>();

        populateBoard();
    }

    @Override
    public Lifecycle checkGameState(ModelCell end, Array<ModelCell> capturedPieces) {
        if (checkCaptureKing(capturedPieces)) {
            return Lifecycle.LOSS;
        } else if (checkWin(end)) {
            return Lifecycle.WIN;
        }
        return Lifecycle.PLAY;
    }

    private boolean checkCaptureKing(Array<ModelCell> capturedPieces) {
        for (ModelCell cell : capturedPieces) {
            if (cell.piece.type == PieceType.KING) {
                return true;
            }
        }
        return false;
    }

    private boolean checkWin(ModelCell end) {
        return end instanceof CornerCell && end.piece.type == PieceType.KING;
    }

    @Override
    public Array<ModelCell> getCapturedPieces(ModelCell end) {
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
        if (first != null && first.piece != null && piece.type.team != first.piece.type.team &&
                second != null && second.piece != null && piece.type.team == second.piece.type.team) {

            if (first.piece.type == PieceType.KING) {
                if (third != null && third.piece != null && piece.type.team == third.piece.type.team &&
                        fourth != null && fourth.piece != null && piece.type.team == fourth.piece.type.team) {

                    capturedPieces.add(first);
                }
            } else {
                capturedPieces.add(first);
            }
        }
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
        match.board.cells = new ModelCell[match.board.dimentions][match.board.dimentions];
        for (int i = 0; i < match.board.dimentions; i++) {
            for (int j = 0; j < match.board.dimentions; j++) {
                ModelCell cell = createCell(match.board, i, j);
                match.board.cells[i][j] = cell;
            }
        }
    }

    protected ModelCell createCell(GameBoard board, int x, int y) {
        if ((x == 0 && y == 0) ||
                (x == board.dimentions - 1 && y == 0) ||
                (x == 0 && y == board.dimentions - 1) ||
                (x == board.dimentions -1 && y == board.dimentions -1)) {
            return new CornerCell(x, y, board);
        } else if (x == board.dimentions / 2 && y == board.dimentions / 2) {
            return new KingCell(x, y, board);
        } else {
            return new RegularCell(x, y, board);
        }
    }
}
