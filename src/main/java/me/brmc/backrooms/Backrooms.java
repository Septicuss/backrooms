package me.brmc.backrooms;

import org.bukkit.plugin.java.JavaPlugin;

import me.brmc.backrooms.commands.WorldCommand;
import me.brmc.backrooms.generator.GeneratorManager;
import me.brmc.backrooms.levels.LevelManager;
import me.brmc.backrooms.resourcepack.ResourcePackManager;
import me.brmc.backrooms.utils.ConfigUtils;
import revxrsal.commands.bukkit.BukkitCommandHandler;

public class Backrooms extends JavaPlugin {

	private static Backrooms instance;

	private ConfigUtils configUtils;

	private GeneratorManager generatorManager;
	private LevelManager levelManager;
	private ResourcePackManager resourcePackManager;

	@Override
	public void onEnable() {
		instance = this;

		configUtils = new ConfigUtils(instance);

		levelManager = new LevelManager();
		generatorManager = new GeneratorManager();
		resourcePackManager = new ResourcePackManager();

		BukkitCommandHandler commandHandler = BukkitCommandHandler.create(this);
		commandHandler.register(new WorldCommand());
	}

	@Override
	public void onDisable() {
	}

	public static Backrooms get() {
		return instance;
	}

	public ConfigUtils getConfigUtils() {
		return configUtils;
	}

	public GeneratorManager getGeneratorManager() {
		return generatorManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public ResourcePackManager getResourcePackManager() {
		return resourcePackManager;
	}

}
