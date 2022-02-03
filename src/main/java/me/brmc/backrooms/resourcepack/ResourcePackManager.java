package me.brmc.backrooms.resourcepack;

import java.io.IOException;

import org.bukkit.Bukkit;

import me.brmc.backrooms.Backrooms;
import me.brmc.backrooms.resourcepack.updater.ResourcePackUpdater;

public class ResourcePackManager {

	private FileConfigurationWrapper resourcePackInfoWrapper;
	private ResourcePackUpdater updater;

	public ResourcePackManager() {

		initializeUpdater();
		updateAsync();

	}

	// Updater

	public void initializeUpdater() {
		this.resourcePackInfoWrapper = new FileConfigurationWrapper("pack", "packinfo");
		this.updater = new ResourcePackUpdater(resourcePackInfoWrapper);
	}

	public void updateAsync() {

		Bukkit.getScheduler().runTaskAsynchronously(Backrooms.get(), () -> {
			update();
		});

	}

	public void update() {

		if (updater == null) {
			return;
		}

		try {
			out("-- Resource Pack Updater Running --");

			updater.runUpdater();

			out("-- Resource Pack Updater Finished --");
		} catch (

		IOException exception) {
			out("x - Updater has failed");
			exception.printStackTrace();
		}
	}

	public String getResourcePackUrl() {
		return updater.getUrl();
	}

	// Utility

	private void out(String message) {
		System.out.println(message);
	}

}
