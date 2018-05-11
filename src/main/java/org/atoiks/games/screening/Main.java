package org.atoiks.games.screening;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;

import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BorderLayout;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {

    public static void main(String[] args) {
        final World world = new World();
        world.setTile(0, 10, World.TILE_BLOCK);
        world.setTile(1, 10, World.TILE_BLOCK);
        world.setTile(2, 10, World.TILE_SUSBR);
        world.setTile(3, 10, World.TILE_SUSBR);
        world.setTile(4, 10, World.TILE_SUSBR);
        world.setTile(5, 10, World.TILE_BLOCK);
        world.setTile(6, 10, World.TILE_BLOCK);

        world.setTile(1, 70, World.TILE_BLOCK);
        world.setTile(2, 70, World.TILE_BLOCK);
        world.setTile(3, 70, World.TILE_CDOOR);
        world.setTile(4, 70, World.TILE_BLOCK);
        world.setTile(5, 70, World.TILE_BLOCK);

        world.setTile(1, 80, World.TILE_BLOCK);
        world.setTile(2, 80, World.TILE_BLOCK);
        world.setTile(3, 80, World.TILE_BLOCK);
        world.setTile(4, 80, World.TILE_BLOCK);
        world.setTile(5, 80, World.TILE_BLOCK);

        final Player player = new Player();
        player.setX(2);
        player.setY(50);

        player.setWorld(world);

        final Window window = new Window(world, player);
        window.start();

        while (Window.hasWindows()) {
            Window.forEachWindow(w -> w.update(0.02f));
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
                //
            }
        }
    }
}

class Window {

    private static final Map<Integer, Window> list = new HashMap<>();

    private final GameFrame gframe;
    private final InputHandler inp;
    private final JFrame frame;
    private final int idx;

    public Window(final World world, final Player player) {
        gframe = new GameFrame(world, player);
        inp = new InputHandler(world, player);

        frame = new JFrame("Atoiks Games - Screening");
        frame.add(gframe, BorderLayout.CENTER);
        frame.addKeyListener(inp);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                list.remove(Window.this.idx);
                Window.this.frame.dispose();
            }
        });

        list.put((idx = list.size()), this);
    }

    public void start() {
        frame.setSize(600, 480);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void update(final float dt) {
        gframe.update(0.020f);
        frame.repaint();
    }

    public static void forEachWindow(final Consumer<? super Window> f) {
        list.values().forEach(f);
    }

    public static boolean hasWindows() {
        return !list.isEmpty();
    }
}

class InputHandler extends KeyAdapter {

    private final Player player;
    private final World world;

    public InputHandler(final World world, final Player player) {
        this.world = world;
        this.player = player;
    }

    @Override
    public void keyPressed(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: player.w = true; break;
            case KeyEvent.VK_S: player.s = true; break;
            case KeyEvent.VK_A: player.a = true; break;
            case KeyEvent.VK_D: player.d = true; break;
            case KeyEvent.VK_E: {
                final Window window = new Window(world, player);
                window.start();
                break;
            }
        }
    }

    @Override
    public void keyReleased(final KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W: player.w = false; break;
            case KeyEvent.VK_S: player.s = false; break;
            case KeyEvent.VK_A: player.a = false; break;
            case KeyEvent.VK_D: player.d = false; break;
        }
    }
}

class GameFrame extends JPanel {

    private final World world;
    private final Player player;

    public GameFrame(final World world, final Player player) {
        this.world = world;
        this.player = player;
    }

    public void update(final float dt) {
        player.update(dt);
    }

    @Override
    public void paintComponent(final Graphics gr) {
        final Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.black);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        final Point origin = this.getLocationOnScreen();
        g.translate(-origin.getX(), -origin.getY());

        final int ymult = 810 / world.HEIGHT;
        final int xmult = 1440 / world.WIDTH;

        for (int i = 0; i < world.getRowCount(); ++i) {
            final byte[] row = world.getRow(i);
            for (int j = 0; j < row.length; ++j) {
                switch (row[j]) {
                    case World.TILE_EMPTY: break;
                    case World.TILE_BLOCK: {
                        g.setColor(Color.white);
                        g.fillRect(j * xmult, i * ymult, xmult, ymult);
                        break;
                    }
                    case World.TILE_STONE: {
                        g.setColor(Color.gray);
                        g.fillRect(j * xmult, i * ymult, xmult, ymult);
                        break;
                    }
                    case World.TILE_CDOOR: {
                        g.setColor(Color.orange);
                        g.fillRect(j * xmult, i * ymult, xmult, ymult / 2);
                        break;
                    }
                    case World.TILE_ODOOR: {
                        g.setColor(Color.orange);
                        g.fillRect((j - 1) * xmult, i * ymult - ymult / 2, xmult, ymult / 2);
                        break;
                    }
                    case World.TILE_SUSBR: {
                        final int y1 = i * ymult + 3 * ymult / 8;
                        final int y2 = i * ymult + 6 * ymult / 8;
                        final int y3 = i * ymult + 7 * ymult / 8;
                        g.setColor(Color.orange);
                        g.drawLine(j * xmult, y1, (j + 1) * xmult, y1);
                        g.setColor(Color.white);
                        g.drawLine(j * xmult, y2, (j + 1) * xmult, y2);
                        g.drawLine(j * xmult, y3, (j + 1) * xmult, y3);
                        break;
                    }
                }
            }
        }

        g.setColor(Color.lightGray);
        g.fillRect((int) (player.getX() * xmult), (int) (player.getY() * ymult), xmult, ymult);
    }
}