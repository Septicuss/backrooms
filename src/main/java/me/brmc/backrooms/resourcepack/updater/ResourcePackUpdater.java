package me.brmc.backrooms.resourcepack.updater;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;

import me.brmc.backrooms.Constants;
import me.brmc.backrooms.resourcepack.FileConfigurationWrapper;
import me.brmc.backrooms.resourcepack.updater.object.GitHubConnection;
import me.brmc.backrooms.resourcepack.updater.object.UpdateStage;
import me.brmc.backrooms.resourcepack.updater.utils.ResourcePackGitHubUtils;
import me.brmc.backrooms.resourcepack.updater.utils.ResourcePackZipUtils;

public class ResourcePackUpdater {

	private FileConfigurationWrapper resourcePackInfoWrapper;
	private FileConfiguration resourcePackInfo;

	private GHRepository filesRepo;
	private GitHubConnection connection;

	private String token;
	private String url;
	private int hash;

	public ResourcePackUpdater(final FileConfigurationWrapper resourcePackInfoWrapper) {

		this.resourcePackInfoWrapper = resourcePackInfoWrapper;
		this.resourcePackInfo = resourcePackInfoWrapper.getConfig();

		resourcePackInfo.addDefault(Constants.TOKEN_KEY, null);
		resourcePackInfo.options().copyDefaults(true);

		this.token = resourcePackInfo.getString(Constants.TOKEN_KEY);
		this.url = resourcePackInfo.getString(Constants.URL_KEY);
		this.hash = resourcePackInfo.getInt(Constants.HASH_KEY);

		this.connection = new GitHubConnection(token);
		this.connection.reconnect();

		out(isConnected() ? "- GitHub successfully connected" : "x - GitHub failed to connect");

	}

	public void runUpdateStage(final UpdateStage stage) throws IOException {

		if (!isConnected()) {
			out("- GitHub is not connected, aborting updater.");
			return;
		}

		out("[!] Running stage " + stage.toString());

		if ((stage == UpdateStage.CHECK || stage == UpdateStage.DOWNLOAD) && filesRepo == null) {
			final GitHub gitHub = connection.getGitHub();
			filesRepo = gitHub.getRepository(Constants.FILES_REPOSITORY);
		}

		if (stage == UpdateStage.CHECK) {

			if (!ResourcePackGitHubUtils.isUpdateRequired(filesRepo, hash)) {
				out("- Latest resource pack version already downloaded");
				return;
			}

			out("- Changes in the repository detected, proceesing to download");
			hash = ResourcePackGitHubUtils.getLatestHash(filesRepo);

			runUpdateStage(UpdateStage.DOWNLOAD);

		}

		if (stage == UpdateStage.DOWNLOAD) {

			out("- Downloading files...");

			final List<GHContent> content = ResourcePackGitHubUtils.getAllContent(filesRepo.getDirectoryContent(""));
			final File mainPackDirectory = resourcePackInfoWrapper.getFile().getParentFile();
			final File resourcePackDirectory = new File(mainPackDirectory, "resourcepack");

			ResourcePackGitHubUtils.downloadContentToDir(resourcePackDirectory, content);

			out("- All files downloaded!");

			runUpdateStage(UpdateStage.ZIP);

		}

		if (stage == UpdateStage.ZIP) {

			final File mainPackDirectory = resourcePackInfoWrapper.getFile().getParentFile();
			final File resourcePackDirectory = new File(mainPackDirectory, "resourcepack/");

			if (resourcePackDirectory == null || !resourcePackDirectory.exists()) {
				out("- Zipping stage couldn't find the resource pack folder!");
				return;
			}

			out("- Zipping the resource pack...");

			final File zippedPackFile = new File(mainPackDirectory, Constants.RESOURCE_PACK_NAME);

			if (zippedPackFile.exists()) {
				zippedPackFile.delete();
			}

			try {
				ResourcePackZipUtils.zipDirectory(zippedPackFile, resourcePackDirectory);
			} catch (IOException exception) {
				out("x - Failed to zip resource pack");
				exception.printStackTrace();
				return;
			}

			runUpdateStage(UpdateStage.UPLOAD);

		}

		if (stage == UpdateStage.UPLOAD) {

			final File mainPackDirectory = resourcePackInfoWrapper.getFile().getParentFile();
			final File zippedPackFile = new File(mainPackDirectory, Constants.RESOURCE_PACK_NAME);

			final GHRepository hostRepo = connection.getGitHub().getRepository(Constants.HOST_REPOSITORY);
			url = ResourcePackGitHubUtils.uploadPack(Constants.RESOURCE_PACK_NAME, hostRepo, zippedPackFile);

			out("- Pack successfully uploaded");

		}

		saveInfoToConfig();

	}

	public void runUpdater() throws IOException {
		runUpdateStage(UpdateStage.CHECK);
	}

	public boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	public String getUrl() {
		return url;
	}

	private void saveInfoToConfig() {
		resourcePackInfo.set(Constants.HASH_KEY, hash);
		resourcePackInfo.set(Constants.URL_KEY, url);

		resourcePackInfoWrapper.save();
	}

	private void out(String message) {
		System.out.println(message);
	}

}
