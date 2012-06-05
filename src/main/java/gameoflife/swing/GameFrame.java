package gameoflife.swing;

import gameoflife.Game;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GameFrame extends JFrame implements GameObserver {

	private static final int HEIGHT = 60;
	private static final int WIDTH = 60;
	private static final int SCALE = 10;
	private static final long serialVersionUID = 1L;
	private final Game game;
	private GameObserver delegateListener = GameObserver.NULL;
	private JPanel allComponents = new JPanel(new BorderLayout());
	private JPanel gridPanel = new JPanel(new BorderLayout());
	private JPanel btnsPanel = new JPanel(new GridBagLayout());

	public GameFrame(Game game) {
		this.game = game;

		setUpFrame();
		setUpGrid();

		clearMatrixButton();
		createNextStepButton();
		createStartButton();

		allComponents.add(gridPanel, BorderLayout.NORTH);
		allComponents.add(btnsPanel, BorderLayout.SOUTH);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(allComponents);

		this.pack();
		this.setVisible(true);
	}

	private void setUpFrame() {
		this.setName("game.frame");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void setUpGrid() {
		ImageRenderer imageRenderer = new ImageRenderer(WIDTH, HEIGHT, SCALE);
		imageRenderer.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				game.toggle(e.getX() / SCALE, e.getY() / SCALE);
			}
		});
		game.setObserver(imageRenderer);
		this.delegateListener = imageRenderer;
		gridPanel.add(imageRenderer, BorderLayout.CENTER);
	}

	private void clearMatrixButton() {
		JButton clearMatrix = new JButton("Clear");
		clearMatrix.setName("clear.matrix");
		clearMatrix.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				game.setLoop(false);
				game.clearMatrix();
				game.nextGen();
			}
		});
		btnsPanel.add(clearMatrix);
	}

	private void createNextStepButton() {
		JButton nextStep = new JButton("Next Step");
		nextStep.setName("next.step");
		nextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				game.setLoop(false);
				game.nextGen();
			}
		});
		btnsPanel.add(nextStep);
	}

	private void createStartButton() {
		JButton start = new JButton("Start");
		start.setName("start");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if (!game.isLoop()) {
					game.setLoop(true);
					game.nextGen();
				}
			}
		});
		btnsPanel.add(start);
	}

	public void gridChanged(boolean[][] grid, Game g) {
		delegateListener.gridChanged(grid, g);
	}

	public static void main(String[] args) {
		Game g = new Game(HEIGHT);

//		int start = 85;
//		int end = 350;
//
//		for (int i = start; i < end; i++) {
//			g.set(i, i, true);
//		}
//
//		int j = end - 1;
//		for (int i = start; i < end; i++) {
//			g.set(i, j--, true);
//		}

		new GameFrame(g);
	}
}
