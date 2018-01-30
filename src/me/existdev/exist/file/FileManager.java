package me.existdev.exist.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.existdev.exist.Exist;
import me.existdev.exist.file.files.Module;
import me.existdev.exist.gui.altchanger.Alt;
import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

	private ArrayList<File> files;
	private Gson gson;
	private java.io.File directory;
	private static final java.io.File ALT = getConfigFile("Alts");
	private static final java.io.File LASTALT = getConfigFile("LastAlt");

	public FileManager() {
		files = new ArrayList<File>();
		gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
		directory = new java.io.File(Minecraft.getMinecraft().mcDataDir.toString() + "/" + Exist.Name);
		makeDirectory();
		registerFiles();
	}

	private void makeDirectory() {
		if (!directory.exists())
			directory.mkdir();
	}

	private void registerFiles() {
		files.add(new Module(gson, null));
	}

	public void loadFiles() {

		for (File file : files) {
			try {
				file.loadFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void loadLastAlt() {
		try {
			String s;
			if (!LASTALT.exists()) {
				PrintWriter printWriter = new PrintWriter(new FileWriter(LASTALT));
				printWriter.println();
				printWriter.close();
			} else if (LASTALT.exists()) {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(LASTALT));
				while ((s = bufferedReader.readLine()) != null) {
					if (s.contains("\t")) {
						s = s.replace("\t", "    ");
					}
					if (s.contains("    ")) {
						String[] parts = s.split("    ");
						String[] account = parts[1].split(":");
						if (account.length == 2) {
							Exist.altManager.setLastAlt(new Alt(account[0], account[1], parts[0]));
						} else {
							String pw = account[1];
							for (int i = 2; i < account.length; i++) {
								pw = pw + ":" + account[i];
							}
							Exist.altManager.setLastAlt(new Alt(account[0], pw, parts[0]));
						}
					} else {
						String[] account = s.split(":");
						if (account.length == 1) {
							Exist.altManager.setLastAlt(new Alt(account[0], ""));
						} else if (account.length == 2) {
							Exist.altManager.setLastAlt(new Alt(account[0], account[1]));
						} else {
							String pw = account[1];
							for (int i = 2; i < account.length; i++) {
								pw = pw + ":" + account[i];
							}
							Exist.altManager.setLastAlt(new Alt(account[0], pw));
						}
					}
				}
				bufferedReader.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void saveLastAlt() {
		try {
			PrintWriter printWriter = new PrintWriter(LASTALT);
			Alt alt = Exist.altManager.getLastAlt();
			if (alt != null) {
				if (alt.getMask().equals("")) {
					printWriter.println(alt.getUsername() + ":" + alt.getPassword());
				} else {
					printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void loadAlts() {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(ALT));
			String s;
			if (!ALT.exists()) {
				PrintWriter printWriter = new PrintWriter(new FileWriter(ALT));
				printWriter.println();
				printWriter.close();
			} else if (ALT.exists()) {
				while ((s = bufferedReader.readLine()) != null) {
					if (s.contains("\t")) {
						s = s.replace("\t", "    ");
					}
					if (s.contains("    ")) {
						String[] parts = s.split("    ");
						String[] account = parts[1].split(":");
						if (account.length == 2) {
							Exist.altManager.getAlts().add(new Alt(account[0], account[1], parts[0]));
						} else {
							String pw = account[1];
							for (int i = 2; i < account.length; i++) {
								pw = pw + ":" + account[i];
							}
							Exist.altManager.getAlts().add(new Alt(account[0], pw, parts[0]));
						}
					} else {
						String[] account = s.split(":");
						if (account.length == 1) {
							Exist.altManager.getAlts().add(new Alt(account[0], ""));
						} else if (account.length == 2) {
							try {
								Exist.altManager.getAlts().add(new Alt(account[0], account[1]));
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							String pw = account[1];
							for (int i = 2; i < account.length; i++) {
								pw = pw + ":" + account[i];
							}
							Exist.altManager.getAlts().add(new Alt(account[0], pw));
						}
					}
				}
			}
			bufferedReader.close();
		} catch (Exception e) {

		}
	}

	public static void saveAlts() {
		try {
			PrintWriter printWriter = new PrintWriter(ALT);
			for (Alt alt : Exist.altManager.getAlts()) {
				if (alt.getMask().equals("")) {
					printWriter.println(alt.getUsername() + ":" + alt.getPassword());
				} else {
					printWriter.println(alt.getMask() + "    " + alt.getUsername() + ":" + alt.getPassword());
				}
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static List<String> read(final java.io.File inputFile) {
		final ArrayList<String> readContent = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
			String currentReadLine2;
			while ((currentReadLine2 = reader.readLine()) != null) {
				readContent.add(currentReadLine2);
			}
		} catch (FileNotFoundException currentReadLine3) {
		} catch (IOException currentReadLine4) {
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException ex) {
			}
		}
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (IOException ex2) {
		}
		return readContent;
	}

	public static void write(final java.io.File outputFile, final List<String> writeContent,
			final boolean overrideContent) {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(outputFile, !overrideContent));
			for (final String outputLine : writeContent) {
				writer.write(outputLine);
				writer.flush();
				writer.newLine();
			}
		} catch (IOException outputLine2) {
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException ex) {
			}
		}
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (IOException ex2) {
		}
	}

	public static java.io.File getConfigDir() {
		final java.io.File file = new java.io.File(Minecraft.getMinecraft().mcDataDir, "Exist");
		if (!file.exists()) {
			file.mkdir();
		}
		return file;
	}

	public static java.io.File getConfigFile(final String name) {
		final java.io.File file = new java.io.File(getConfigDir(), String.format("%s.txt", name));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException ex) {
			}
		}
		return file;
	}

	public void saveFiles() {

		for (File file : files) {
			try {
				file.saveFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
