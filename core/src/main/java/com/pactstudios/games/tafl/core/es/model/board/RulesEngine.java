package com.pactstudios.games.tafl.core.es.model.board;

import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.es.model.board.cells.CornerCell;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Piece;
import com.pactstudios.games.tafl.core.es.model.objects.PieceType;
import com.pactstudios.games.tafl.core.es.model.objects.Team;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.pactstudios.games.tafl.core.level.TaflLevel;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class RulesEngine {

    public TaflLevel level;
    public Team turn;

    public RulesEngine(TaflLevel level, World world) {
        this.level = level;
        this.turn = Team.BLACK;
    }

    public void changeTurn() {
        if (turn == Team.BLACK) {
            turn = Team.WHITE;
        } else {
            turn = Team.BLACK;
        }
    }

    public boolean checkTurn(Piece piece) {
        return piece != null && piece.type.team == turn;
    }

    public Lifecycle checkGameState(ModelCell end) {
        if (checkCapture(end)) {
            return Lifecycle.LOSS;
        } else if (checkWin(end)) {
            return Lifecycle.WIN;
        }
        return Lifecycle.PLAY;
    }

    public void movePiece(Vector2 position, ModelCell start, ModelCell end) {
        Piece piece = start.piece;
        position.set(MapUtils.getTilePositionCenter(end));
        start.piece = null;
        end.piece = piece;
        level.log.log(piece.type.team, start, end);
    }

    private boolean checkWin(ModelCell end) {
        return end instanceof CornerCell && end.piece.type == PieceType.KING;
    }

    private boolean checkCapture(ModelCell end) {
        boolean capture = checkCaptureAbove(end);
        capture |= checkCaptureBelow(end);
        capture |= checkCaptureRight(end);
        capture |= checkCaptureLeft(end);
        return capture;
    }

    private boolean checkCaptureLeft(ModelCell end) {
        ModelCell first = level.board.getCell(end.x - 1, end.y);
        ModelCell second = level.board.getCell(end.x - 2, end.y);
        ModelCell third = level.board.getCell(end.x - 1, end.y + 1);
        ModelCell fourth = level.board.getCell(end.x - 1, end.y - 1);
        return checkCapture(end.piece, first, second, third, fourth);
    }

    private boolean checkCaptureRight(ModelCell end) {
        ModelCell first = level.board.getCell(end.x + 1, end.y);
        ModelCell second = level.board.getCell(end.x + 2, end.y);
        ModelCell third = level.board.getCell(end.x + 1, end.y + 1);
        ModelCell fourth = level.board.getCell(end.x + 1, end.y - 1);
        return checkCapture(end.piece, first, second, third, fourth);
    }

    private boolean checkCaptureBelow(ModelCell end) {
        ModelCell first = level.board.getCell(end.x, end.y - 1);
        ModelCell second = level.board.getCell(end.x, end.y - 2);
        ModelCell third = level.board.getCell(end.x + 1, end.y - 1);
        ModelCell fourth = level.board.getCell(end.x - 1, end.y - 1);
        return checkCapture(end.piece, first, second, third, fourth);
    }

    private boolean checkCaptureAbove(ModelCell end) {
        ModelCell first = level.board.getCell(end.x, end.y + 1);
        ModelCell second = level.board.getCell(end.x, end.y + 2);
        ModelCell third = level.board.getCell(end.x + 1, end.y + 1);
        ModelCell fourth = level.board.getCell(end.x - 1, end.y + 1);
        return checkCapture(end.piece, first, second, third, fourth);
    }

    private boolean checkCapture(Piece piece, ModelCell first, ModelCell second, ModelCell third, ModelCell fourth) {
        if (first != null && first.piece != null && second != null && second.piece != null) {
            if (piece.type.team != first.piece.type.team && piece.type.team == second.piece.type.team) {
                if (first.piece.type == PieceType.KING) {
                    if (third != null && third.piece != null && fourth != null && fourth.piece != null) {
                        if (piece.type.team == third.piece.type.team && piece.type.team == fourth.piece.type.team) {
                            first.piece.entity.deleteFromWorld();
                            first.piece = null;
                            return true;
                        }
                    }
                } else {
                    first.piece.entity.deleteFromWorld();
                    first.piece = null;
                }
            }
        }
        return false;
    }

    public boolean legalMove(Piece piece, ModelCell start, ModelCell end) {
        if (end.piece == null && (end.canWalk() || piece.type == PieceType.KING)) {
            if (start.x == end.x) {
                int increment = Integer.signum(end.y - start.y);
                for (int i = start.y + increment; i != end.y; i += increment) {
                    ModelCell examined = level.board.getCell(start.x, i);
                    if (examined.piece != null) {
                        return false;
                    }
                }
                return true;
            } else if (start.y == end.y) {
                int increment = Integer.signum(end.x - start.x);
                for (int i = start.x + increment; i != end.x; i += increment) {
                    ModelCell examined = level.board.getCell(i, start.y);
                    if (examined.piece != null) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
