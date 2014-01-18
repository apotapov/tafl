package com.pactstudios.games.tafl.core.es.model.map.cells;

import com.artemis.Entity;


public abstract class ModelCell {
    public int x;
    public int y;
    public Entity entity;

    public ModelCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract boolean canWalk();

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + x + ", " + y;
    }

    public void reset() {
    }
}
