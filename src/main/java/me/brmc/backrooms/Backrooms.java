package me.brmc.backrooms;

import org.bukkit.plugin.java.JavaPlugin;

public class Backrooms extends JavaPlugin {

	private static Backrooms instance;

	@Override
	public void onLoad() {
		instance = this;
	}

	public static Backrooms get() {
		return instance;
	}

}
