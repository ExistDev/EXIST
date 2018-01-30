package me.existdev.exist.file.files;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.existdev.exist.Exist;
import me.existdev.exist.file.File;
import me.existdev.exist.setting.Setting;
import net.minecraft.client.Minecraft;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Module extends File {

	private final java.io.File directory = new java.io.File(
			Minecraft.getMinecraft().mcDataDir.toString() + "/" + Exist.Name + "/" + "Modules");

	public Module(Gson gson, java.io.File file) {
		super(gson, file);
	}

	@Override
	public void loadFile() throws IOException {

		makeDirecotry();

		for (me.existdev.exist.module.Module module : Exist.getMods()) {

			makeModuleFile(module);

			FileReader fr = new FileReader(getFile(module));

			JsonObject jsonObject = getGson().fromJson(fr, JsonObject.class);

			if (jsonObject == null) {
				fr.close();
				return;
			}

			if (jsonObject.has("toggled"))
				if (Boolean.parseBoolean(jsonObject.get("toggled").getAsString()))
					module.toggle();

			if (jsonObject.has("key"))
				module.setBind(Integer.parseInt(jsonObject.get("key").getAsString()));

			ArrayList<Setting> settings = Exist.settingManager.getSettingsForModule(module);

			if (settings != null && jsonObject.has("settings")) {

				JsonArray jsonArray = (JsonArray) jsonObject.get("settings");

				jsonArray.forEach(jsonElement -> settings.stream()
						.filter(setting -> jsonElement.getAsJsonObject().has(setting.getName())).forEach(setting -> {
							if (setting.isBoolean()) {
								setting.setBooleanValue(
										jsonElement.getAsJsonObject().get(setting.getName()).getAsBoolean());
							} else if (setting.isDigit()) {
								setting.setCurrentValue(
										jsonElement.getAsJsonObject().get(setting.getName()).getAsDouble());
							} else {
								setting.setCurrentOption(
										jsonElement.getAsJsonObject().get(setting.getName()).getAsString());
							}

						}));
			}
			fr.close();
		}
	}

	@Override
	public void saveFile() throws IOException {
		makeDirecotry();

		for (me.existdev.exist.module.Module module : Exist.getMods()) {

			makeModuleFile(module);

			FileWriter fw = new FileWriter(getFile(module));

			JsonObject jsonObject = new JsonObject();

			jsonObject.addProperty("toggled", module.isToggled());
			jsonObject.addProperty("key", module.getBind());

			ArrayList<Setting> settings = Exist.settingManager.getSettingsForModule(module);

			if (settings != null) {

				JsonArray jsonArray = new JsonArray();
				JsonObject jsonObject1 = new JsonObject();

				settings.forEach(setting -> {
					if (setting.isBoolean())
						jsonObject1.addProperty(setting.getName(), setting.getBooleanValue());
					else if (setting.isDigit())
						jsonObject1.addProperty(setting.getName(), setting.getCurrentValue());
					else
						jsonObject1.addProperty(setting.getName(), setting.getCurrentOption());
				});

				jsonArray.add(jsonObject1);
				jsonObject.add("settings", jsonArray);

			}

			fw.write(getGson().toJson(jsonObject));
			fw.close();

		}

	}

	private void makeDirecotry() {
		if (!directory.exists())
			directory.mkdir();
	}

	private void makeModuleFile(me.existdev.exist.module.Module module) throws IOException {
		if (!getFile(module).exists())
			getFile(module).createNewFile();
	}

	private java.io.File getFile(me.existdev.exist.module.Module module) {
		return new java.io.File(directory, module.getName() + ".json");
	}
}
