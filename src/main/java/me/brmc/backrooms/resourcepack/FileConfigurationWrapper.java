package me.brmc.backrooms.resourcepack;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class FileConfigurationWrapper {

	private String fullPath;
	private String fileName;

	private FileConfiguration fileConfiguration;
	private File file;

	public FileConfigurationWrapper(final String path, final String fileName) {
		this.fileName = fileName;
		this.fullPath = path + "/" + fileName + ".yml";

		this.file = loadFile(fullPath);
		this.fileConfiguration = loadFileConfiguration(file);
	}

	// File handling

	private File loadFile(final String fullPath) {
		final File file = new File(fullPath);

		try {
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		} catch (IOException exception) {
			return null;
		}

		return file;
	}

	public void save() {
		try {
			fileConfiguration.save(file);
		} catch (IOException exception) {
			exception.printStackTrace();
			return;
		}
	}

	private FileConfiguration loadFileConfiguration(final File file) {
		return YamlConfiguration.loadConfiguration(file);
	}

	// Getters

	public FileConfiguration getConfig() {
		return fileConfiguration;
	}

	public String getName() {
		return fileName;
	}

	public String getFullPath() {
		return fullPath;
	}

	public File getFile() {
		return file;
	}

}
