package me.existdev.exist.file;

import com.google.gson.Gson;

import java.io.IOException;

public abstract class File {

	private Gson gson;
	private java.io.File file;

	public File(Gson gson, java.io.File file) {
		this.gson = gson;
		this.file = file;
		makeDirectory();
	}

	private void makeDirectory() {
		if (file != null && !file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ignored) {
			}
		}
	}

	public abstract void loadFile() throws IOException;

	public abstract void saveFile() throws IOException;

	public java.io.File getFile() {
		return file;
	}

	public Gson getGson() {
		return gson;
	}
}