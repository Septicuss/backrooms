package me.brmc.backrooms.resourcepack;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class FileConfigurationWrapper {

	private String path;
	private String fileName;
	private FileConfiguration config;

	public FileConfigurationWrapper(final String path, final String fileName, final FileConfiguration config) {
		this.fileName = fileName;
		this.path = path + "/" + fileName + ".yml";
		this.config = config;
	}

	public String getName() {
		return fileName;
	}

	public String getPath() {
		return path;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public File getFile() {
		return new File(path);
	}

}
