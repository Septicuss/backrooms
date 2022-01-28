package me.brmc.backrooms.levels;

import me.brmc.backrooms.generator.Generatable;
import me.brmc.backrooms.generator.object.Matrix;

public class Level0 extends AbstractLevel implements Generatable {

	private String levelName = this.getClass().getTypeName();
	private String secondLevelName;
	private String levelDescription;

	public Level0(String secondLevelName, String levelDescription) {
		this.secondLevelName = secondLevelName;
		this.levelDescription = levelDescription;
	}

	@Override
	public Matrix generateMatrix() {
		Matrix chunkPattern = new Matrix(16, 16);
		return chunkPattern;
	}
	
	public String getLevelName() {
		return levelName;
	}

	public String getSecondLevelName() {
		return secondLevelName;
	}

	public String getLevelDescription() {
		return levelDescription;
	}

}
