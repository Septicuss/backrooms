package me.brmc.backrooms;

import org.bukkit.plugin.java.JavaPlugin;

import me.brmc.backrooms.generator.GeneratorManager;
import me.brmc.backrooms.levels.LevelManager;

public class Backrooms extends JavaPlugin {

	private static Backrooms instance;

	private GeneratorManager generatorManager;
	private LevelManager levelManager;

	@Override
	public void onEnable() {
		instance = this;
		
		levelManager = new LevelManager();
		generatorManager = new GeneratorManager();

	}

	@Override
	public void onDisable() {
	}

	public static Backrooms get() {
		return instance;
	}

	public GeneratorManager getGeneratorManager() {
		return generatorManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}
	
	
	
}
