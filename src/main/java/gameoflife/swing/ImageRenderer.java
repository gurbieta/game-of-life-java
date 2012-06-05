package gameoflife.swing;

import gameoflife.Game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ImageRenderer extends JPanel implements GameObserver {

	private static final long serialVersionUID = 1L;

	private final int width;
	private final int height;
	private final int scale;

	private boolean[][] cells;
	private Game game;

	public ImageRenderer(int width, int height, int scale) {
		this.width = width;
		this.height = height;
		this.cells = new boolean[width][height];
		this.setName("image.renderer");
		Dimension size = new Dimension(width * scale, height * scale);
		this.setPreferredSize(size);
		this.setMaximumSize(size);
		this.setMinimumSize(size);
		this.scale = scale;
	}

	public void gridChanged(boolean[][] grid, final Game g) {
		this.cells = grid;
		this.game = g;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ImageRenderer.this.repaint();
				if (game.isLoop()) {
					game.nextGen();
				}
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (int row = 0; row < height; row++) {
			for (int column = 0; column < width; column++) {
				Color color = cells[column][row] ? Color.BLACK : Color.WHITE;
				g.setColor(color);
				g.fillRect(column * scale, row * scale, scale, scale);
				g.setColor(Color.GRAY);
				g.drawRect(column * scale, row * scale, scale, scale);
			}
		}
	}

}
