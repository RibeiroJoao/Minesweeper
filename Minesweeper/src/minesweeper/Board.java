package minesweeper;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.plaf.ColorUIResource;

import sun.swing.PrintColorUIResource;

/**
 * Represents the Board and all its operations.
 *
 * @author Joao Ribeiro
 */
public class Board {

	private int largura;
	private int comprimento;
	private char[][] gameset = new char[10][10];
	private int bombsFlagedCorrectly;
	private String nrRedDots = "20";
	private ArrayList<Coordenada> listOfBombsExisting = new ArrayList<>();
	private ArrayList<Coordenada> listOfBombsCorrectlySelected = new ArrayList<>();
	private long startTime = 0;

	public Board(int largura, int comprimento) {
		this.largura = largura;
		this.comprimento = comprimento;
		bombsFlagedCorrectly = 0;
		createSet();
		placeBombs();
		completeBoard();
		presentBoard();
		startTime = System.nanoTime();
		// printSet(); //on console
	}

	private void presentBoard() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTextArea redCounter = new JTextArea();
		redCounter.setPreferredSize(new Dimension(80, 20));
		redCounter.setText(nrRedDots + " bombs");
		JPanel buttonPanel = new JPanel();
		JPanel containerPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(largura, comprimento));

		for (int i = 0; i < largura; i++) {
			for (int j = 0; j < comprimento; j++) {
				JButton button = new JButton();

				// Left Click
				button.addActionListener((ActionEvent e) -> {
					button.setBackground(Color.white);
					int x = (int) ((button.getLocation().x) / 60);
					int y = (int) ((button.getLocation().y) / 60);
					String actualValue = new StringBuilder().append("").append(Board.this.gameset[y][x]).toString();
					button.setText(actualValue);
					if (button.getText().equals("B")) {
						JDialog dialog = new JDialog();
						JButton newGame = new JButton("Novo Jogo");
						newGame.addActionListener((ActionEvent e1) -> {
							dialog.setVisible(false);
							frame.setVisible(false);
							new Board(largura, comprimento);
						});
						dialog.setPreferredSize(new Dimension(200, 80));
						Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
						dialog.setLocation(dim.width / 2 - dialog.getSize().width / 2,
								dim.height / 2 - dialog.getSize().height / 2);
						dialog.setTitle("UPS!!!");
						dialog.add(newGame);
						dialog.pack();
						dialog.setVisible(true);
					}
					if (button.getText().equals("0")) {
						// System.out.println("zero pressed");
					}
				});

				// Right Click
				button.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON3) {
							if (button.getBackground().toString().contains("r=255,g=0,b=0")) {
								button.setForeground(new PrintColorUIResource(0, new Color(51, 51, 51)));
								button.setBackground(new ColorUIResource(238, 238, 238));
								increaseNrRedDots();
								redCounter.setText(nrRedDots + " bombs");
							} else {
								decreaseNrRedDots();
								redCounter.setText(nrRedDots + " bombs");

								int x = (int) ((button.getLocation().x) / 60);
								int y = (int) ((button.getLocation().y) / 60);
								if (gameset[y][x] == 'B' && !listOfBombsCorrectlySelectedContains(y, x)) {
									listOfBombsCorrectlySelected.add(new Coordenada(y, x));
									bombsFlagedCorrectly++;
									int nrRedFlags = Integer.parseInt(nrRedDots);
									if (bombsFlagedCorrectly == 20 && nrRedFlags == 0) {
										JDialog dialog = new JDialog();
										JButton newGame = new JButton("Novo Jogo");
										newGame.addActionListener((ActionEvent e1) -> {
											dialog.setVisible(false);
											frame.setVisible(false);
											new Board(largura, comprimento);
										});
										dialog.setTitle("PARABENS!!!");
										dialog.add(newGame);
										dialog.setPreferredSize(new Dimension(200, 80));
										Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
										dialog.setLocation(dim.width / 2 - dialog.getSize().width / 2,
												dim.height / 2 - dialog.getSize().height / 2);
										dialog.pack();
										dialog.setVisible(true);
										long elapsedTime = (System.nanoTime() - startTime) / 1000000;
										printTime(elapsedTime);
									}
								}
								button.setForeground(Color.red);
								button.setBackground(Color.red);
							}

						}
					}
				});

				buttonPanel.add(button);
			}
		}
		switch (largura) {
		case 10:
			buttonPanel.setPreferredSize(new Dimension(600, 600));
			break;
		case 30:
			buttonPanel.setPreferredSize(new Dimension(600, 600));
			break;
		case 60:
			buttonPanel.setPreferredSize(new Dimension(800, 800));
			break;
		default:
			break;
		}
		containerPanel.add(redCounter);
		containerPanel.add(buttonPanel);
		frame.getContentPane().add(containerPanel);
		frame.pack();
		frame.setVisible(true);
	}

	private void createSet() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				gameset[i][j] = '-';
			}
		}
	}

	private void placeBombs() {
		int nrBombsPlaced = 0;
		while (nrBombsPlaced < 20) {
			int x = (int) (Math.random() * (this.largura));
			int y = (int) (Math.random() * (this.comprimento));
			if (gameset[x][y] == '-') {
				gameset[x][y] = 'B';
				nrBombsPlaced++;
				listOfBombsExisting.add(new Coordenada(x, y));
			}
		}
	}

	private void completeBoard() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				int value = 0;
				if (gameset[i][j] != 'B') {
					// four corner places
					if (i == 0 || j == 0 || i == 9 || j == 9) {
						if (i == 0 && j == 0) {
							if (gameset[0][1] == 'B') {
								value++;
							}
							if (gameset[1][1] == 'B') {
								value++;
							}
							if (gameset[1][0] == 'B') {
								value++;
							}
						}
						if (i == 9 && j == 0) {

							if (gameset[8][0] == 'B') {
								value++;
							}
							if (gameset[8][1] == 'B') {
								value++;
							}
							if (gameset[9][1] == 'B') {
								value++;
							}
						}
						if (i == 0 && j == 9) {
							if (gameset[0][8] == 'B') {
								value++;
							}
							if (gameset[1][8] == 'B') {
								value++;
							}
							if (gameset[1][9] == 'B') {
								value++;
							}
						}
						if (i == 9 && j == 9) {
							if (gameset[9][8] == 'B') {
								value++;
							}
							if (gameset[8][8] == 'B') {
								value++;
							}
							if (gameset[8][9] == 'B') {
								value++;
							}
						}

						// four border lines minus corners
						if (i == 0 && j != 9 && j != 0) {
							if (gameset[i][j - 1] == 'B') {
								value++;
							}
							if (gameset[i][j + 1] == 'B') {
								value++;
							}
							if (gameset[i + 1][j - 1] == 'B') {
								value++;
							}
							if (gameset[i + 1][j] == 'B') {
								value++;
							}
							if (gameset[i + 1][j + 1] == 'B') {
								value++;
							}
						}
						if (i == 9 && j != 9 && j != 0) {
							if (gameset[i][j - 1] == 'B') {
								value++;
							}
							if (gameset[i][j + 1] == 'B') {
								value++;
							}
							if (gameset[i - 1][j - 1] == 'B') {
								value++;
							}
							if (gameset[i - 1][j] == 'B') {
								value++;
							}
							if (gameset[i - 1][j + 1] == 'B') {
								value++;
							}
						}
						if (j == 0 && i != 9 && i != 0) {
							if (gameset[i - 1][j] == 'B') {
								value++;
							}
							if (gameset[i + 1][j] == 'B') {
								value++;
							}
							if (gameset[i - 1][j + 1] == 'B') {
								value++;
							}
							if (gameset[i][j + 1] == 'B') {
								value++;
							}
							if (gameset[i + 1][j + 1] == 'B') {
								value++;
							}
						}
						if (j == 9 && i != 9 && i != 0) {
							if (gameset[i - 1][j] == 'B') {
								value++;
							}
							if (gameset[i + 1][j] == 'B') {
								value++;
							}
							if (gameset[i - 1][j - 1] == 'B') {
								value++;
							}
							if (gameset[i][j - 1] == 'B') {
								value++;
							}
							if (gameset[i + 1][j - 1] == 'B') {
								value++;
							}
						}
					} // all places except borders
					else {
						if (gameset[i - 1][j - 1] == 'B') {
							value++;
						}
						if (gameset[i - 1][j] == 'B') {
							value++;
						}
						if (gameset[i - 1][j + 1] == 'B') {
							value++;
						}
						if (gameset[i][j - 1] == 'B') {
							value++;
						}
						if (gameset[i][j + 1] == 'B') {
							value++;
						}
						if (gameset[i + 1][j - 1] == 'B') {
							value++;
						}
						if (gameset[i + 1][j] == 'B') {
							value++;
						}
						if (gameset[i + 1][j + 1] == 'B') {
							value++;
						}
					}
					this.gameset[i][j] = Integer.toString(value).charAt(0);
				}
			}
		}
	}

	private void increaseNrRedDots() {
		int newNr = Integer.parseInt(nrRedDots);
		newNr++;
		nrRedDots = Integer.toString(newNr);
	}

	private void decreaseNrRedDots() {
		int newNr = Integer.parseInt(nrRedDots);
		newNr--;
		nrRedDots = Integer.toString(newNr);
	}

	private boolean listOfBombsCorrectlySelectedContains(int i, int j) {
		for (Coordenada par : listOfBombsCorrectlySelected) {
			if (i == par.getX() && j == par.getY()) {
				return true;
			}
		}
		return false;
	}

	private void printTime(long timeMS) {
		int timeSEG = (int) timeMS / 1000;
		if ((int) (Math.log10(timeSEG) + 1) < 3) {
			System.out.println("Your time: " + timeSEG + " sec");
		} else {
			double timeMIN = timeSEG / 60;
			System.out.println("Your time: " + timeMIN + " min");
		}
	}

	/*
	 * private void checkTimeRecord(long mayBrecord) { String outString = null; try
	 * { Scanner in = new Scanner(new FileReader("records.txt")); StringBuilder sb =
	 * new StringBuilder(); while (in.hasNext()) { sb.append(in.next()); }
	 * in.close(); outString = sb.toString(); } catch (FileNotFoundException ex) {
	 * Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex); } int
	 * record = Integer.parseInt(outString); if (mayBrecord < record) {
	 * System.out.println("NEW RECORD! Congrats!"); String newRecord =
	 * String.valueOf(mayBrecord); try { File file = new File("records.txt");
	 * BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	 * writer.write(newRecord); writer.close(); } catch (IOException ex) {
	 * Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex); } } else
	 * { System.out.println("Record is " + record);
	 * System.out.println("Better luck next Time!"); } }
	 */

	/*
	 * private void printSet() { System.out.println("   0 1 2 3 4 5 6 7 8 9");
	 * System.out.println("   -------------------"); for (int i = 0; i < 10; i++) {
	 * System.out.print(i + "| "); for (int j = 0; j < 10; j++) {
	 * System.out.print(gameset[i][j] + " "); } System.out.println(); } }
	 */
}