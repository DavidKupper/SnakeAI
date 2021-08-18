package de.fhws.davidkupper.snakegame;

import de.fhws.davidkupper.flatgame.Paintable;

import java.awt.Graphics;

public abstract class Item implements Paintable {
    private int x;
    private int y;

    public Item(int x, int y) {
        this.setPos(x, y);
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
