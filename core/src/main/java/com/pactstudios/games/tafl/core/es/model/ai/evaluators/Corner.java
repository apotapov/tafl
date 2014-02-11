package com.pactstudios.games.tafl.core.es.model.ai.evaluators;

import com.pactstudios.games.tafl.core.es.model.ai.optimization.BitBoard;

public class Corner {
    public static final int SOUTH_WEST_CORNER = 0;
    public static final int SOUTH_EAST_CORNER = 10;
    public static final int NORTH_WEST_CORNER = 110;
    public static final int NORTH_EAST_CORNER = 120;


    private static final int[] SOUTH_WEST_CORNER_VALUES = {
        2, 22, 12,
    };
    private static final int[] SOUTH_EAST_CORNER_VALUES = {
        8, 32, 20,
    };
    private static final int[] NORTH_WEST_CORNER_VALUES = {
        88, 112, 100,
    };
    private static final int[] NORTH_EAST_CORNER_VALUES = {
        98, 118, 108,
    };

    public static BitBoard north;
    public static BitBoard south;
    public static BitBoard east;
    public static BitBoard west;



    public int corner;
    public BitBoard protection;
    public int xDirection;
    public int yDirection;
    public boolean isBarricaded;

    public int xNearCorner;
    public int yNearCorner;

    public BitBoard xHemisphere;
    public BitBoard yHemisphere;


    public static Corner getSouthWestCorner(int boardSize, int dimensions) {
        Corner corner = new Corner();
        corner.corner = SOUTH_WEST_CORNER;
        corner.protection = new BitBoard(boardSize);
        for (int element : SOUTH_WEST_CORNER_VALUES) {
            corner.protection.set(element);
        }

        corner.xNearCorner = 1;
        corner.yNearCorner = dimensions;

        corner.xDirection = 1;
        corner.yDirection = dimensions;

        if (north == null) {
            createHemispheres(boardSize, dimensions);
        }
        corner.xHemisphere = south;
        corner.yHemisphere = west;

        return corner;
    }

    public static Corner getSouthEastCorner(int boardSize, int dimensions) {
        Corner corner = new Corner();

        corner.corner = SOUTH_EAST_CORNER;
        corner.protection = new BitBoard(boardSize);
        for (int element : SOUTH_EAST_CORNER_VALUES) {
            corner.protection.set(element);
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
        corner.protection = new BitBoard(boardSize);
        for (int element : NORTH_WEST_CORNER_VALUES) {
            corner.protection.set(element);
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
        corner.protection = new BitBoard(boardSize);
        for (int element : NORTH_EAST_CORNER_VALUES) {
            corner.protection.set(element);
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
