package gameoflife.swing;

import gameoflife.Game;

public interface GameObserver {

	GameObserver NULL = new GameObserver() {

		public void gridChanged(boolean[][] grid, Game g) {
		}

	};

	void gridChanged(boolean[][] grid, Game g);

}
