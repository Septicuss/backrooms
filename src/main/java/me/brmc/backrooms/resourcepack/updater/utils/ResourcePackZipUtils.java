package me.brmc.backrooms.resourcepack.updater.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import me.brmc.backrooms.Constants;

public class ResourcePackZipUtils {

	public static void zipDirectory(final File outputFile, final File directory) throws IOException {

		final FileOutputStream fileOut = new FileOutputStream(outputFile);
		final ZipOutputStream zipOut = new ZipOutputStream(fileOut);

		zipFile(directory, null, zipOut);

		zipOut.close();
		fileOut.close();

	}

	public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {
		if (fileToZip.isHidden()) {
			return;
		}

		if (fileToZip.isDirectory()) {

			final String directoryName = (fileName == null ? "" : fileName + "/");

			if (fileName != null) {
				zipOut.putNextEntry(new ZipEntry(fileName + (fileName.endsWith("/") ? "" : "/")));
				zipOut.closeEntry();
			}

			for (File childFile : fileToZip.listFiles()) {
				final String childFileName = childFile.getName();
				final String fullPath = directoryName + childFileName;

				zipFile(childFile, fullPath, zipOut);
			}

			return;
		}

		final FileInputStream fileIn = new FileInputStream(fileToZip);
		final ZipEntry zipEntry = new ZipEntry(fileName);

		zipOut.putNextEntry(zipEntry);

		final boolean isJson = isJson(fileName);
		final boolean shouldCompress = (isJson && Constants.COMPRESS_JSON);

		if (shouldCompress && compressJson(fileToZip, fileIn, zipOut)) {
			return;
		}

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fileIn.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}

		fileIn.close();
	}

	/**
	 * @return Whether the compression was successful or not
	 */
	private static boolean compressJson(final File jsonFile, FileInputStream fileIn, ZipOutputStream zipOut)
			throws IOException {
		final String data = Files.readString(jsonFile.toPath());

		if (data.isEmpty() || data.isBlank()) {
			return false;
		}

		ObjectMapper mapper = new ObjectMapper();
		JsonNode jsonNode = mapper.readValue(data, JsonNode.class);

		zipOut.write(jsonNode.toString().getBytes());
		fileIn.close();
		return true;

	}

	private static boolean isJson(final String fileName) {
		if (fileName == null || fileName.isEmpty() || fileName.isBlank()) {
			return false;
		}

		return (fileName.contains("json") || fileName.contains("mcmeta"));
	}

}
