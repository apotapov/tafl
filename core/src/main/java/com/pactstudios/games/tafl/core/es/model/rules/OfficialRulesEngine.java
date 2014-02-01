package com.pactstudios.games.tafl.core.es.model.rules;

import java.util.BitSet;

import com.badlogic.gdx.utils.Array;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.enums.DrawReasonEnum;
import com.pactstudios.games.tafl.core.enums.PlayerWarningEnum;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;
import com.pactstudios.games.tafl.core.es.model.board.Move;

public class OfficialRulesEngine extends RulesEngine {

    OfficialMoveRules moveRules;
    OfficialCaptureRules captureRules;
    OfficialGameEndRules gameEndRules;


    public OfficialRulesEngine(TaflMatch match) {
        super(match);
        moveRules = new OfficialMoveRules(match);
        captureRules = new OfficialCaptureRules(match);
        gameEndRules = new OfficialGameEndRules(match);
    }

    @Override
    public int getFirstTurn() {
        return Constants.BoardConstants.BLACK_TEAM;
    }

    @Override
    public int getSecondTurn() {
        return Constants.BoardConstants.WHITE_TEAM;
    }



    @Override
    public int checkWinner() {
        return gameEndRules.checkWinner();
    }

    @Override
    public void recordBoardConfiguration(int boardHash) {
        gameEndRules.recordBoardConfiguration(boardHash);
    }

    @Override
    public void undoBoardConfiguration() {
        gameEndRules.undoBoardConfiguration();
    }

    @Override
    public DrawReasonEnum checkDraw() {
        return gameEndRules.checkDraw();
    }

    @Override
    public BitSet getCapturedPieces(int destination) {
        return captureRules.getCapturedPieces(destination);
    }

    @Override
    public boolean isMoveLegal(int source, int destination) {
        return moveRules.isMoveLegal(source, destination);
    }

    @Override
    public Array<Move> legalMoves() {
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
}
