package me.brmc.backrooms.levels;

import java.util.HashMap;

public class LevelManager {

	private HashMap<String, AbstractLevel> levels = new HashMap<>();

	public LevelManager() {
		
		levels.put("Level0", new Level0("Tutorial Level", null));
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, AbstractLevel> getLevels() {
		return (HashMap<String, AbstractLevel>) levels.clone();
	}

}
