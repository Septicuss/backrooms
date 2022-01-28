package me.brmc.backrooms.generator;

import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.WorldCreator;

import me.brmc.backrooms.Backrooms;
import me.brmc.backrooms.levels.AbstractLevel;

public class GeneratorManager {

	public GeneratorManager() {
		createWorlds();
	}
	
	public void generateChunk(Chunk chunk) {
		
	}

	public void createWorlds() {
		HashMap<String, AbstractLevel> levels = Backrooms.get().getLevelManager().getLevels();
		for (AbstractLevel level : levels.values()) {
			
			if (Backrooms.get().getServer().getWorld(level.getLevelName()) == null) {
				
				WorldCreator worldCreator = new WorldCreator(level.getLevelName());
				worldCreator.generator(new BukkitGenerator());
				
				Backrooms.get().getServer().createWorld(worldCreator);
			}
		}
	}
}
