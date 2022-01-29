package me.brmc.backrooms.resourcepack.updater;

import me.brmc.backrooms.resourcepack.FileConfigurationWrapper;
import me.brmc.backrooms.utils.ConfigUtils;

public class ResourcePackUpdater {

	final FileConfigurationWrapper resourcePackInfoWrapper;
	final ConfigUtils configUtils;

	public ResourcePackUpdater(final FileConfigurationWrapper resourcePackInfoWrapper, final ConfigUtils configUtils) {
		this.resourcePackInfoWrapper = resourcePackInfoWrapper;
		this.configUtils = configUtils;
	}

}
