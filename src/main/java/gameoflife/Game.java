package gameoflife;

import gameoflife.swing.GameObserver;

public class Game {

	public static final String LS = System.getProperty("line.separator");

	public final int SIZE;

	private boolean[][] matrix;
	private boolean[][] futureMatrix;
	private boolean bounded = false;
	private boolean loop = false;

	private GameObserver observer;

	public Game() {
		this.SIZE = 50;
		clearMatrix();
	}

	public Game(int size) {
		this.SIZE = size;
		clearMatrix();
	}

	public Game(String matrix) {
		String[] lines = matrix.split(LS);
		this.SIZE = lines.length;

		clearMatrix();

		String[] chars;
		for (int i = 0; i < SIZE; i++) {
			chars = lines[i].split("");
			for (int j = 0; j < SIZE; j++) {
				set(i, j, chars[j].equals("X"));
			}
		}
	}

	public void clearMatrix() {
		matrix = new boolean[SIZE][SIZE];
		futureMatrix = new boolean[SIZE][SIZE];
	}

	public boolean isAlive(int x, int y) {
		return matrix[x][y];
	}

	public void toggle(int x, int y) {
		matrix[x][y] = !matrix[x][y];
		this.loop = false;
		notifyObserver();
	}

	public void set(int x, int y, boolean status) {
		matrix[x][y] = status;
	}

	public int getNeighborsQty(int x, int y) {
		return getNeighborsUp(x, y) + getNeighborsSame(x, y)
				+ getNeighborsDown(x, y);
	}

	public int getNeighborsUp(int x, int y) {
		int qty = 0;
		if (y != 0) {
			if (matrix[x][y - 1])
				qty++;
			if ((x > 0 && matrix[x - 1][y - 1])
					|| (x == 0 && matrix[SIZE - 1][y - 1]))
				qty++;
			if ((x < SIZE - 1 && matrix[x + 1][y - 1])
					|| (x == SIZE - 1 && matrix[0][y - 1]))
				qty++;
		} else if (!bounded) {
			if (matrix[x][SIZE - 1])
				qty++;
			if ((x > 0 && matrix[x - 1][SIZE - 1])
					|| (x == 0 && matrix[SIZE - 1][SIZE - 1]))
				qty++;
			if ((x < SIZE - 1 && matrix[x + 1][SIZE - 1])
					|| (x == SIZE - 1 && matrix[0][SIZE - 1]))
				qty++;

		}

		return qty;
	}

	public int getNeighborsDown(int x, int y) {

		int qty = 0;
		if (y < SIZE - 1) {
			if (matrix[x][y + 1])
				qty++;
			if ((x > 0 && matrix[x - 1][y + 1])
					|| (x == 0 && matrix[SIZE - 1][y + 1]))
				qty++;
			if ((x < SIZE - 1 && matrix[x + 1][y + 1])
					|| (x == SIZE - 1 && matrix[0][y + 1]))
				qty++;
		} else if (!bounded) {
			if (matrix[x][0])
				qty++;
			if ((x > 0 && matrix[x - 1][0]) || (x == 0 && matrix[SIZE - 1][0]))
				qty++;
			if ((x < SIZE - 1 && matrix[x + 1][0])
					|| (x == SIZE - 1 && matrix[0][0]))
				qty++;

		}

		return qty;

	}

	public int getNeighborsSame(int x, int y) {
		int qty = 0;

		if ((x > 0 && matrix[x - 1][y])
				|| (!bounded && x == 0 && matrix[SIZE - 1][y]))
			qty++;
		if (x < SIZE - 1 && matrix[x + 1][y]
				|| (!bounded && x == SIZE - 1 && matrix[0][y]))
			qty++;

		return qty;
	}

	public void nextGen() {
		if (isLoop()) {
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e) {
//				throw new RuntimeException(e);
//			}
		}

		futureMatrix = new boolean[SIZE][SIZE];
		int qty;
		boolean status;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				qty = getNeighborsQty(i, j);
				status = matrix[i][j];
				if (loneliness(qty, status) || overpopulation(qty, status)
						|| populate(qty, status)) {
					futureMatrix[i][j] = !matrix[i][j];
				} else {
					futureMatrix[i][j] = matrix[i][j];
				}
			}
		}

		matrix = futureMatrix;

		notifyObserver();
	}

	public String render() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				sb.append(matrix[i][j] ? "X" : "-");
			}
			if (i != SIZE - 1) {
				sb.append(LS);
			}
		}

		return sb.toString();
	}

	public boolean loneliness(int qty, boolean status) {
		return status && qty <= 1;
	}

	public boolean overpopulation(int qty, boolean status) {
		return status && qty > 3;
	}

	public boolean survive(int qty, boolean status) {
		return status && (qty == 2 || qty == 3);
	}

	public boolean populate(int qty, boolean status) {
		return !status && qty == 3;
	}

	public void setObserver(GameObserver observer) {
		this.observer = observer;
		notifyObserver();
	}

	private void notifyObserver() {
		if (observer != null) {
			observer.gridChanged(matrix, this);
		}
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public static void main(String[] args) throws InterruptedException {
		Game g = new Game(10);

		// g.set(3, 0, true);
		// g.set(4, 1, true);
		// g.set(2, 2, true);
		// g.set(3, 2, true);
		// g.set(4, 2, true);

		g.set(4, 4, true);
		g.set(5, 4, true);
		g.set(5, 5, true);

		for (int i = 0; i < 2; i++) {
			System.out.println(g.render());
			System.out.println();
			g.nextGen();
			Thread.sleep(200);
		}
	}
}
