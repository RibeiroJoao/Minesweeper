package minesweeper;

/**
 * Value Object for coordinate.
 * 
 * @author Joao Ribeiro
 */
class Coordenada {
	private final int x;
	private final int y;

	/**
	 * Ctor.
	 * 
	 * @param x axis value.
	 * @param y axis value.
	 */
	Coordenada(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * getter for x.
	 * 
	 * @return the x value
	 */
	public int getX() {
		return x;
	}

	/**
	 * Getter for y.
	 * 
	 * @return the y value.
	 */
	public int getY() {
		return y;
	}
}