package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.LifeCycle;
import com.pactstudios.games.tafl.core.enums.PlayerWarningEnum;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.TaflMove;
import com.pactstudios.games.tafl.core.es.model.ai.optimization.moves.Move;

public class OfficialRulesEngine extends RulesEngine {

    OfficialMoveRules moveRules;
    OfficialCaptureRules captureRules;
    OfficialGameEndRules gameEndRules;

    @Override
    public int getFirstTurn() {
        return Constants.BoardConstants.BLACK_TEAM;
    }

    @Override
    public int checkWinner() {
        return gameEndRules.checkWinner();
    }

    @Override
    public DrawReasonEnum checkDraw() {
        return gameEndRules.checkDraw();
    }

    @Override
    public BitSet getCapturedPieces(Move move) {
        return captureRules.getCapturedPieces(move);
    }

    @Override
    public boolean isMoveLegal(int source, int destination) {
        return moveRules.isMoveLegal(source, destination);
    }

    @Override
    public Array<TaflMove> legalMoves() {
        return moveRules.legalMoves();
    }

    @Override
    public void calculateLegalMoves() {
        moveRules.calculateLegalMoves();
    }

    @Override
    public BitSet legalMoves(int source) {
        return moveRules.legalMoves(source);
    }

    @Override
    public PlayerWarningEnum checkPlayerWarning() {
        return moveRules.checkPlayerWarning();
    }

    @Override
    public void initializeMatch(TaflMatch match) {
        moveRules = new OfficialMoveRules(match);
        captureRules = new OfficialCaptureRules(match);
        gameEndRules = new OfficialGameEndRules(match);

        calculateLegalMoves();
        gameEndRules.initializeMatch(match);
    }

    @Override
    public void applyMove(TaflMatch match, TaflMove move) {
        gameEndRules.applyMove(match, move);
    }

    @Override
    public void undoMove(TaflMatch match, TaflMove move) {
        gameEndRules.undoMove(match, move);
    }

    @Override
    public void addPiece(TaflMatch match, int team, int pieces) {
        gameEndRules.addPiece(match, team, pieces);
    }

    @Override
    public void removePieces(TaflMatch match, int captor, BitSet capturedPieces) {
        gameEndRules.removePieces(match, captor, capturedPieces);
    }

    @Override
    public void changeTurn(TaflMatch match) {
        calculateLegalMoves();
        gameEndRules.changeTurn(match);
    }

    @Override
    public void gameOver(TaflMatch match, LifeCycle status) {
        gameEndRules.gameOver(match, status);
    }
}
