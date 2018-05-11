package org.atoiks.games.screening;

public class World {

    public static final byte TILE_EMPTY = 0;
    public static final byte TILE_BLOCK = 1;
    public static final byte TILE_CDOOR = 2;
    public static final byte TILE_ODOOR = 3;
    public static final byte TILE_SUSBR = 4;
    public static final byte TILE_STONE = 5;

    public final int WIDTH, HEIGHT;

    private final byte[][] map;

    public World() {
        this(160, 90);
    }

    public World(final int WIDTH, final int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.map = new byte[HEIGHT][WIDTH];
    }

    public boolean isInteractiveTile(final int x, final int y) {
        switch (map[y][x]) {
            case TILE_CDOOR:
            case TILE_ODOOR:
                return true;
            default:
                return false;
        }
    }

    public boolean tryInteractWithTile(final int x, final int y) {
        switch (map[y][x]) {
            case TILE_CDOOR:
                map[y][x] = TILE_ODOOR;
                return true;
            case TILE_ODOOR:
                map[y][x] = TILE_CDOOR;
                return true;
            default:
                return false;
        }
    }

    public boolean isSolidTile(final int x, final int y) {
        try {
            switch (map[y][x]) {
                case TILE_BLOCK:
                case TILE_CDOOR:
                case TILE_SUSBR:
                case TILE_STONE:
                    return true;
                default:
                    return false;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public int closestSolidTileWithin(final int x, final int y1, final int y2) {
        for (int i = y1; y1 < y2; ++i) {
            if (isSolidTile(x, i)) return i;
        }
        return y2;
    }

    public void setTile(final int x, final int y, final byte tile) {
        map[y][x] = tile;
    }

    public byte getTile(final int x, final int y) {
        return map[y][x];
    }

    public byte[] getRow(final int row) {
        return map[row];
    }

    public int getRowCount() {
        return map.length;
    }
}