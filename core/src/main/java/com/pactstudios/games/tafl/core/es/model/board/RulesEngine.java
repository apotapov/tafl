package com.pactstudios.games.tafl.core.es.model.board;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Vector2;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.board.cells.CornerCell;
import com.pactstudios.games.tafl.core.es.model.board.cells.ModelCell;
import com.pactstudios.games.tafl.core.es.model.objects.Team;
import com.pactstudios.games.tafl.core.es.systems.events.LifecycleEvent.Lifecycle;
import com.pactstudios.games.tafl.core.level.TaflLevel;
import com.pactstudios.games.tafl.core.utils.MapUtils;

public class RulesEngine {

    public TaflLevel level;
    public Team turn;

    protected World world;
    protected GroupManager groupManager;

    public RulesEngine(TaflLevel level, World world) {
        this.level = level;
        this.turn = Team.BLACK;
        this.world = world;
        this.groupManager = world.getManager(GroupManager.class);
    }

    public void changeTurn() {
        if (turn == Team.BLACK) {
            turn = Team.WHITE;
        } else {
            turn = Team.BLACK;
        }
    }

    public boolean checkTurn(Entity entity) {
        return entity != null && groupManager.isInGroup(entity, turn.toString());
    }

    public Lifecycle checkGameState(Entity movingEntity, ModelCell end) {
        if (checkCapture(movingEntity, end)) {
            return Lifecycle.LOSS;
        } else if (checkWin(movingEntity, end)) {
            return Lifecycle.WIN;
        }
        return Lifecycle.PLAY;
    }

    public void movePiece(Entity entity, Vector2 position, ModelCell start, ModelCell end) {
        position.set(MapUtils.getTilePositionCenter(end));
        start.entity = null;
        end.entity = entity;
        logMove(entity, start, end);
    }

    private void logMove(Entity entity, ModelCell start, ModelCell end) {
        level.log.log(getTeam(entity), start, end);
    }

    private boolean checkWin(Entity entity, ModelCell end) {
        return end instanceof CornerCell &&
                groupManager.isInGroup(entity, Constants.GroupConstants.KING);
    }

    private boolean checkCapture(Entity entity, ModelCell end) {
        Team movingTeam = getTeam(entity);

        boolean capture = checkCaptureAbove(end, movingTeam);
        capture |= checkCaptureBelow(end, movingTeam);
        capture |= checkCaptureRight(end, movingTeam);
        capture |= checkCaptureLeft(end, movingTeam);
        return capture;
    }

    private boolean checkCaptureLeft(ModelCell end,
            Team movingTeam) {
        ModelCell first = level.board.getCell(end.x - 1, end.y);
        ModelCell second = level.board.getCell(end.x - 2, end.y);
        ModelCell third = level.board.getCell(end.x - 1, end.y + 1);
        ModelCell fourth = level.board.getCell(end.x - 1, end.y - 1);
        return checkCapture(movingTeam, first, second, third, fourth);
    }

    private boolean checkCaptureRight(ModelCell end,
            Team movingTeam) {
        ModelCell first = level.board.getCell(end.x + 1, end.y);
        ModelCell second = level.board.getCell(end.x + 2, end.y);
        ModelCell third = level.board.getCell(end.x + 1, end.y + 1);
        ModelCell fourth = level.board.getCell(end.x + 1, end.y - 1);
        return checkCapture(movingTeam, first, second, third, fourth);
    }

    private boolean checkCaptureBelow(ModelCell end,
            Team movingTeam) {
        ModelCell first = level.board.getCell(end.x, end.y - 1);
        ModelCell second = level.board.getCell(end.x, end.y - 2);
        ModelCell third = level.board.getCell(end.x + 1, end.y - 1);
        ModelCell fourth = level.board.getCell(end.x - 1, end.y - 1);
        return checkCapture(movingTeam, first, second, third, fourth);
    }

    private boolean checkCaptureAbove(ModelCell end,
            Team movingTeam) {
        ModelCell first = level.board.getCell(end.x, end.y + 1);
        ModelCell second = level.board.getCell(end.x, end.y + 2);
        ModelCell third = level.board.getCell(end.x + 1, end.y + 1);
        ModelCell fourth = level.board.getCell(end.x - 1, end.y + 1);
        return checkCapture(movingTeam, first, second, third, fourth);
    }

    private boolean checkCapture(Team movingTeam, ModelCell first, ModelCell second, ModelCell third, ModelCell fourth) {
        if (first != null && first.entity != null && second != null && second.entity != null) {
            Team firstTeam = getTeam(first.entity);
            Team secondTeam = getTeam(second.entity);
            if (movingTeam != firstTeam && secondTeam == movingTeam) {
                if (groupManager.isInGroup(first.entity, Constants.GroupConstants.KING)) {
                    if (third != null && third.entity != null && fourth != null && fourth.entity != null) {
                        Team thirdTeam = getTeam(third.entity);
                        Team fourthTeam = getTeam(fourth.entity);
                        if (movingTeam == thirdTeam && movingTeam == fourthTeam) {
                            first.entity.deleteFromWorld();
                            first.entity = null;
                            return true;
                        }
                    }
                } else {
                    first.entity.deleteFromWorld();
                    first.entity = null;
                }
            }
        }
        return false;
    }

    public boolean legalMove(Entity entity, ModelCell start, ModelCell end) {
        if (end.entity == null && (end.canWalk() || groupManager.isInGroup(entity, Constants.GroupConstants.KING))) {
            if (start.x == end.x) {
                int increment = Integer.signum(end.y - start.y);
                for (int i = start.y + increment; i != end.y; i += increment) {
                    ModelCell examined = level.board.getCell(start.x, i);
                    if (examined.entity != null) {
                        return false;
                    }
                }
                return true;
            } else if (start.y == end.y) {
                int increment = Integer.signum(end.x - start.x);
                for (int i = start.x + increment; i != end.x; i += increment) {
                    ModelCell examined = level.board.getCell(i, start.y);
                    if (examined.entity != null) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    private Team getTeam(Entity e) {
        if (groupManager.isInGroup(e, Team.WHITE.toString())) {
            return Team.WHITE;
        } else {
            return Team.BLACK;
        }
    }

}
