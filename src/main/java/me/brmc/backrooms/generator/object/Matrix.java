package me.brmc.backrooms.generator.object;

public class Matrix {

	private String[][] data;
	private int width;
	private int height;

	// Constructor

	public Matrix(int width, int height) {
		data = new String[width][height];
		this.width = width;
		this.height = height;
	}

	// Primitive methods

	public void put(int x, int y, String value) {
		data[x][y] = value;
	}

	public String get(int x, int y) {
		return data[x][y];
	}

	public void remove(int x, int y) {
		put(x, y, null);
	}

	// Other methods

	public void fillRectangle(int x1, int y1, int x2, int y2, String value) {
		int minX = Math.min(x1, x2);
		int maxX = Math.max(x1, x2);
		int minY = Math.min(y1, y2);
		int maxY = Math.max(y1, y2);

		for (int x = minX; x <= maxX; x++) {
			for (int y = minY; y <= maxY; y++) {
				put(x, y, value);
			}
		}
	}

	public void fill(String value) {
		fillRectangle(0, 0, width, height, value);
	}

}
