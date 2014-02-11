package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class Corner {
    public static final int SOUTH_WEST_CORNER = 0;
    public static final int SOUTH_EAST_CORNER = 10;
    public static final int NORTH_WEST_CORNER = 110;
    public static final int NORTH_EAST_CORNER = 120;


    private static final int[] SOUTH_WEST_BARRICADE = {
        2, 12, 22
    };
    private static final int[] SOUTH_WEST_SECONDARY = {
        3, 13, 23, 33
    };

    private static final int[] SOUTH_EAST_BARRICADE = {
        8, 20, 32,
    };
    private static final int[] SOUTH_EAST_SECONDARY = {
        7, 19, 31, 43
    };

    private static final int[] NORTH_WEST_BARRICADE = {
        112, 100, 88,
    };
    private static final int[] NORTH_WEST_SECONDARY = {
        77, 89, 101, 113
    };

    private static final int[] NORTH_EAST_BARRICADE = {
        118, 108, 98,
    };
    private static final int[] NORTH_EAST_SECONDARY = {
        87, 97, 107, 117
    };

    public static BitBoard north;
    public static BitBoard south;
    public static BitBoard east;
    public static BitBoard west;

    public int corner;

    public int xBarricade;
    public int diagBarricade;
    public int yBarricade;

    public BitBoard secondary;

    public int xDirection;
    public int yDirection;

    public int xNearCorner;
    public int yNearCorner;

    public BitBoard xHemisphere;
    public BitBoard yHemisphere;

    public boolean isBarricaded;


    public static Corner getSouthWestCorner(int boardSize, int dimensions) {
        Corner corner = new Corner();
        corner.corner = SOUTH_WEST_CORNER;

        corner.xBarricade = SOUTH_WEST_BARRICADE[0];
        corner.diagBarricade = SOUTH_WEST_BARRICADE[1];
        corner.yBarricade = SOUTH_WEST_BARRICADE[2];

        corner.secondary = new BitBoard(boardSize);
        for (int i : SOUTH_WEST_SECONDARY) {
            corner.secondary.set(i);
        }

        corner.xNearCorner = 1;
        corner.yNearCorner = dimensions;

        corner.xDirection = 1;
        corner.yDirection = dimensions;

        if (north == null) {
            createHemispheres(boardSize, dimensions);
        }
        corner.xHemisphere = west;
        corner.yHemisphere = south;

        return corner;
    }

    public static Corner getSouthEastCorner(int boardSize, int dimensions) {
        Corner corner = new Corner();

        corner.corner = SOUTH_EAST_CORNER;

        corner.xBarricade = SOUTH_EAST_BARRICADE[0];
        corner.diagBarricade = SOUTH_EAST_BARRICADE[1];
        corner.yBarricade = SOUTH_EAST_BARRICADE[2];

        corner.secondary = new BitBoard(boardSize);
        for (int i : SOUTH_EAST_SECONDARY) {
            corner.secondary.set(i);
        }

        corner.xNearCorner = dimensions - 2;
        corner.yNearCorner = dimensions * 2 - 1;

        corner.xDirection = -1;
        corner.yDirection = dimensions;

        if (north == null) {
            createHemispheres(boardSize, dimensions);
        }
        corner.xHemisphere = east;
        corner.yHemisphere = south;

        return corner;
    }
    public static Corner getNorthWestCorner(int boardSize, int dimensions) {
        Corner corner = new Corner();

        corner.corner = NORTH_WEST_CORNER;

        corner.xBarricade = NORTH_WEST_BARRICADE[0];
        corner.diagBarricade = NORTH_WEST_BARRICADE[1];
        corner.yBarricade = NORTH_WEST_BARRICADE[2];

        corner.secondary = new BitBoard(boardSize);
        for (int i : NORTH_WEST_SECONDARY) {
            corner.secondary.set(i);
        }

        corner.xNearCorner = dimensions * dimensions - dimensions + 1;
        corner.yNearCorner = dimensions * dimensions - dimensions * 2;

        corner.xDirection = 1;
        corner.yDirection = -dimensions;

        if (north == null) {
            createHemispheres(boardSize, dimensions);
        }
        corner.xHemisphere = west;
        corner.yHemisphere = north;

        return corner;
    }
    public static Corner getNorthEastCorner(int boardSize, int dimensions) {
        Corner corner = new Corner();

        corner.corner = NORTH_EAST_CORNER;

        corner.xBarricade = NORTH_EAST_BARRICADE[0];
        corner.diagBarricade = NORTH_EAST_BARRICADE[1];
        corner.yBarricade = NORTH_EAST_BARRICADE[2];

        corner.secondary = new BitBoard(boardSize);
        for (int i : NORTH_EAST_SECONDARY) {
            corner.secondary.set(i);
        }

        corner.xNearCorner = dimensions * dimensions - 2;
        corner.yNearCorner = dimensions * dimensions - dimensions - 1;

        corner.xDirection = -1;
        corner.yDirection = -dimensions;

        if (north == null) {
            createHemispheres(boardSize, dimensions);
        }
        corner.xHemisphere = east;
        corner.yHemisphere = north;

        return corner;
    }

    private static void createHemispheres(int boardSize, int dimensions) {

        int half = dimensions / 2;

        north = new BitBoard(boardSize);
        south = new BitBoard(boardSize);
        east = new BitBoard(boardSize);
        west = new BitBoard(boardSize);
        for (int i = 0; i < dimensions; i++) {
            for (int j = 0; j < dimensions; j++) {
                if (i < half) {
                    south.set(i * dimensions + j);
                } else if (i > half) {
                    north.set(i * dimensions + j);
                }
                if (j < half) {
                    west.set(i * dimensions + j);
                } else if (j > half) {
                    east.set(i * dimensions + j);
                }
            }
        }
    }
}
