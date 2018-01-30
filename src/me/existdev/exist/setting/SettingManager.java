package me.existdev.exist.setting;

import java.util.ArrayList;

import me.existdev.exist.module.Module;

public class SettingManager {

	private ArrayList<Setting> settings;

	public SettingManager() {
		this.settings = new ArrayList();
	}

	public void addSetting(Setting s) {
		this.settings.add(s);
	}

	public Setting getSetting(Module m, String name) {
		for (Setting s : this.settings) {
			if (s.getModule().equals(m) && s.getName().equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}

	public ArrayList<Setting> getSettings() {
		return this.settings;
	}
	
	public Setting getSettingByName(String name) {
		for (Setting set : this.settings) {
			if (set.getName().equalsIgnoreCase(name)) {
				return set;
			}
		}
		return null;
	}

	public ArrayList<Setting> getSettingsForModule(Module m) {
		ArrayList<Setting> settings = new ArrayList();

		for (Setting s : this.settings) {
			if (s.getModule().equals(m)) {
				settings.add(s);
			}
		}

		if (settings.isEmpty()) {
			return null;
		}

		return settings;
	}

}

