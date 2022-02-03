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

public class ResourcePackZipUtils {

	private static final boolean COMPRESS_JSON = true;

	public static void zipDirectory(final File outputFile, final File directory) throws IOException {

		final FileOutputStream fileOut = new FileOutputStream(outputFile);
		final ZipOutputStream zipOut = new ZipOutputStream(fileOut);

		try {
			zipFile(directory, null, zipOut);
		} catch (IOException exception) {
			exception.printStackTrace();
		}

		zipOut.close();
		fileOut.close();

	}

	public static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut) throws IOException {

		if (fileToZip.isHidden()) {
			return;
		}

		if (fileToZip.isDirectory()) {

			if (fileName != null) {
				zipOut.putNextEntry(new ZipEntry(fileName + (fileName.endsWith("/") ? "" : "/")));
				zipOut.closeEntry();
			}

			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				zipFile(childFile, (fileName == null ? ("") : (fileName + "/")) + childFile.getName(), zipOut);
			}

			return;
		}

		final boolean isJson = fileName.contains("json") || fileName.contains("mcmeta");

		final FileInputStream fileIn = new FileInputStream(fileToZip);
		final ZipEntry zipEntry = new ZipEntry(fileName);

		zipOut.putNextEntry(zipEntry);

		if (isJson && COMPRESS_JSON) {

			final String data = Files.readString(fileToZip.toPath());

			if (!data.isEmpty() && !data.isBlank()) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readValue(data, JsonNode.class);

				zipOut.write(jsonNode.toString().getBytes());
				fileIn.close();
				return;
			}
		}

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fileIn.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}

		fileIn.close();
	}

}
