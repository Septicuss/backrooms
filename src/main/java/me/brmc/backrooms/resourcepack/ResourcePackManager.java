package me.brmc.backrooms.resourcepack;

import me.brmc.backrooms.utils.ConfigUtils;

public class ResourcePackManager {

	// 1. Check if resourcepack needs to be updated
	// 2.1 If it doesn't, don't do anything
	// 2.2 If it does, continue
	// 3. Backup the old resource pack files and zip into a separate backup folder
	// 4. Download the latest files from GitHub repository backrooms-resourcepack
	// 5. Optimize and zip the latest files from GitHub repository
	// 6. Upload the latest zip to "backrooms-host" repository if it's validated and
	// working, otherwise use backup

	private ConfigUtils configUtils;

	public ResourcePackManager(final ConfigUtils configUtils) {
		this.configUtils = configUtils;
	}

	public ConfigUtils getConfigUtils() {
		return configUtils;
	}

}
