package me.brmc.backrooms.resourcepack.updater.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHContentBuilder;
import org.kohsuke.github.GHContentUpdateResponse;
import org.kohsuke.github.GHRepository;

public class ResourcePackGitHubUtils {

	/**
	 * @return Whether download was successful
	 */
	public static boolean downloadContentToDir(final File directory, final List<GHContent> contentList) {

		if (directory == null || !directory.exists()) {
			directory.mkdirs();
		}

		for (GHContent content : contentList) {

			try {

				if (content.isDirectory()) {
					new File(directory, content.getPath()).mkdirs();
					continue;
				}

				File file = new File(directory, content.getPath());
				FileUtils.copyInputStreamToFile(content.read(), file);

			} catch (IOException exception) {
				out("- Failed to download file \"" + (content == null ? "null" : content.getName()) + "\"");
				exception.printStackTrace();
				return false;
			}
		}

		return true;

	}

	/**
	 * @return URL String to download resource pack
	 */
	public static String uploadPack(final String fileName, final GHRepository hostRepo, final File zippedPack) {

		try {

			final String resourcePackFileName = fileName + ".zip";
			boolean oldPackExists = true;

			try {
				hostRepo.getFileContent(resourcePackFileName);
			} catch (FileNotFoundException exception) {
				oldPackExists = false;
			}

			final byte[] fileBytes = Files.readAllBytes(zippedPack.toPath());

			GHContentBuilder builder = hostRepo.createContent();
			builder.content(fileBytes);

			if (oldPackExists) {
				final GHContent oldPack = hostRepo.getFileContent(resourcePackFileName);
				final String oldSha = oldPack.getSha();

				builder.sha(oldSha);
			}

			builder.path(resourcePackFileName);
			builder.message("Pack Update");

			final GHContentUpdateResponse response = builder.commit();
			return response.getContent().getDownloadUrl();

		} catch (IOException exception) {
			out("x - Pack upload failed");
			exception.printStackTrace();
			return null;
		}

	}

	public static boolean isUpdateRequired(final GHRepository filesRepo, final int oldHash) {
		final int newHash = getLatestHash(filesRepo);
		return (newHash != oldHash);
	}

	public static int getLatestHash(final GHRepository repo) {

		try {
			List<GHCommit> commits = repo.listCommits().toList();
			final String lastCommitDateString = commits.get(0).getCommitDate().toString();

			final int hash = getHashFromString(lastCommitDateString);
			return hash;
		} catch (IOException e) {
			out("x - Failed to get hash");
			e.printStackTrace();
		}

		return 0;

	}

	public static List<GHContent> getAllContent(final Iterable<GHContent> iterable) throws IOException {

		final List<GHContent> contentList = new ArrayList<>();

		for (GHContent content : iterable) {
			if (content.isDirectory()) {
				contentList.addAll(getAllContent(content.listDirectoryContent()));
				continue;
			}

			contentList.add(content);
		}

		return contentList;

	}

	private static int getHashFromString(final String str) {

		int hash = 7;
		for (int i = 0; i < str.length(); i++) {
			hash = hash * 31 + str.charAt(i);
		}
		return hash;

	}

	private static void out(String message) {
		System.out.println(message);
	}

}
