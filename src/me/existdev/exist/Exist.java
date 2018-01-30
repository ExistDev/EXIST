package me.existdev.exist;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;

import com.mojang.realmsclient.gui.ChatFormatting;

import me.existdev.exist.command.CommandManager;
import me.existdev.exist.file.FileManager;
import me.existdev.exist.font.NormalFontRenderer;
import me.existdev.exist.gui.TabGUI;
import me.existdev.exist.gui.altchanger.AltManager;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.Module.Category;
import me.existdev.exist.module.modules.combat.AntiBot;
import me.existdev.exist.module.modules.combat.AutoArmor;
import me.existdev.exist.module.modules.combat.ChestSteal;
import me.existdev.exist.module.modules.combat.KillAura;
import me.existdev.exist.module.modules.combat.Velocity;
import me.existdev.exist.module.modules.movement.FastLadder;
import me.existdev.exist.module.modules.movement.FastStair;
import me.existdev.exist.module.modules.movement.Flight;
import me.existdev.exist.module.modules.movement.Glide;
import me.existdev.exist.module.modules.movement.HighJump;
import me.existdev.exist.module.modules.movement.InvMove;
import me.existdev.exist.module.modules.movement.Jesus;
import me.existdev.exist.module.modules.movement.Longjump;
import me.existdev.exist.module.modules.movement.NoSlowdown;
import me.existdev.exist.module.modules.movement.NoWeb;
import me.existdev.exist.module.modules.movement.SlimeJump;
import me.existdev.exist.module.modules.movement.Speed;
import me.existdev.exist.module.modules.movement.Spider;
import me.existdev.exist.module.modules.movement.Sprint;
import me.existdev.exist.module.modules.movement.Step;
import me.existdev.exist.module.modules.movement.WaterSpeed;
import me.existdev.exist.module.modules.player.AntiCactus;
import me.existdev.exist.module.modules.player.FastEat;
import me.existdev.exist.module.modules.player.GodMode;
import me.existdev.exist.module.modules.player.InvCleaner;
import me.existdev.exist.module.modules.player.NoFall;
import me.existdev.exist.module.modules.render.Chams;
import me.existdev.exist.module.modules.render.ChestESP;
import me.existdev.exist.module.modules.render.ESP;
import me.existdev.exist.module.modules.render.Fullbright;
import me.existdev.exist.module.modules.render.HUD;
import me.existdev.exist.module.modules.render.NoBob;
import me.existdev.exist.module.modules.world.BedFucker;
import me.existdev.exist.module.modules.world.ScaffoldWalk;
import me.existdev.exist.module.modules.world.Tower;
import me.existdev.exist.setting.SettingManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Exist {

	public static String Name = "Exist";
	public static double Version = 0.3;

	private static ArrayList<Module> mods;
	public static ArrayList<Module> sorted = new ArrayList<Module>();
	public static ArrayList<Module> sortedarray = new ArrayList<Module>();
	// Manager
	public static SettingManager settingManager;
	public static FileManager fileManager;
	public static NormalFontRenderer fontRenderer;
	public static CommandManager commandManager;
	public static AltManager altManager;
	public static int clientColor = 0xFF4286f4;
	public static TabGUI tabGui;

	public Exist() {
		Display.setTitle(Name + " " + Version + " ExistDev");
		mods = new ArrayList<Module>();
		tabGui = new TabGUI();
		settingManager = new SettingManager();
		fileManager = new FileManager();
		fontRenderer = new NormalFontRenderer();
		commandManager = new CommandManager();
		altManager = new AltManager();
		altManager.setupAlts();
		fileManager.loadFiles();
		commandManager.loadCommands();

		// addMod
		addMod(new KillAura());
		addMod(new AntiBot());
		addMod(new AutoArmor());
		addMod(new ChestSteal());
		addMod(new Velocity());
		addMod(new FastLadder());
		addMod(new Flight());
		addMod(new Glide());
		addMod(new Jesus());
		addMod(new Longjump());
		addMod(new NoSlowdown());
		addMod(new NoWeb());
		addMod(new Speed());
		addMod(new InvMove());
		addMod(new InvCleaner());
		addMod(new NoFall());
		addMod(new ESP());
		addMod(new ChestESP());
		addMod(new ScaffoldWalk());
		addMod(new Sprint());
		addMod(new Fullbright());
		addMod(new FastStair());
		addMod(new Step());
		addMod(new BedFucker());
		addMod(new FastEat());
		addMod(new HUD());
		addMod(new WaterSpeed());
		addMod(new NoBob());
		addMod(new AntiCactus());
		addMod(new Chams());
		addMod(new Spider());
		addMod(new SlimeJump());
		addMod(new HighJump());
		addMod(new Tower());
		addMod(new GodMode());

		sort();
	}

	private void sort() {
		sorted.clear();
		for (Module m : mods) {
			addBiggestMod();
		}
	}

	private static void addBiggestMod() {
		Module modname = null;
		for (final Module mod : mods) {
			if (!sortedarray.contains(mod)) {
				try {
					if (modname == null) {
						modname = mod;
					} else if (Exist.fontRenderer.helveticaArrayList
							.getStringWidth(mod.getDisplayName()) > Exist.fontRenderer.helveticaArrayList
									.getStringWidth(modname.getDisplayName())) {
						modname = mod;
					}
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		}
		sortedarray.add(modname);
	}

	public static void sortArray() {
		sortedarray.clear();
		for (Module m : mods) {
			addBiggestMod();
		}
	}

	public static int getClientColor() {
		return clientColor;
	}

	public void addMod(Module m) {
		mods.add(m);
	}

	public static ArrayList<Module> getMods() {
		return mods;
	}

	public static void onUpdate() {
		for (Module m : mods) {
			m.onUpdate();
		}
	}

	public static void onRender() {
		for (Module m : mods) {
			m.onRender();
		}
	}

	public static void onKeyPressed(int bind) {
		for (Module m : mods) {
			if (m.getBind() == bind) {
				m.toggle();
			}
		}
	}

	public static Module getModule() {
		return getModule();
	}

	public static List<Module> getModsInCategory(Category cat) {
		List<Module> mods = new ArrayList();
		for (Module m : getMods()) {
			if (m.getCategory() == cat) {
				mods.add(m);
			}
		}
		return mods;
	}

	public static Module getModuleByName(String name) {
		for (Module m : mods) {
			if (!m.getName().equalsIgnoreCase(name)) {
				continue;
			}
			return m;
		}
		return null;
	}

	public static ArrayList<Module> getModulesInCategory(Module.Category cat) {
		ArrayList<Module> modsInCat = new ArrayList();
		for (Module mod : getMods()) {
			if (mod.getCategory().equals(cat)) {
				modsInCat.add(mod);
			}
		}
		return modsInCat;
	}

	public static Module getModules(Class<? extends Module> clazz) {
		for (Module mod : getMods()) {
			if (mod.getClass() == clazz) {
				return mod;
			}
		}
		return null;
	}

	public static void tellPlayer(String text) {
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(
				ChatFormatting.WHITE + "[" + ChatFormatting.AQUA + Name + ChatFormatting.WHITE + "] " + text));
	}
}
