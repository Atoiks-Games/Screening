package org.atoiks.games.screening;

public class Player {

    public boolean w, a, s, d;

    private World world;
    private float x, y;
    private float dy = 0;

    public void setWorld(final World world) {
        this.world = world;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }

    public void update(final float dt) {
        if (w) {
            if (dy == 0) y -= 1f;
        }
        if (a) {
            x -= 0.2f;
        }
        if (d) {
            x += 0.2f;
        }
        if (s) {
            world.tryInteractWithTile(getX(), getY() + 1);
            s = false; // Otherwise player will continue to interact with tile
        }

        if (world.isSolidTile(getX(), getY() + 1)) {
            dy = 0;
        } else {
            dy += 0.1f;
        }
        final int k = world.closestSolidTileWithin(getX(), getY(), (int) (y += (int) dy));
        if (k < y) {
            y = k - 1; dy = 0;
        }
    }
}