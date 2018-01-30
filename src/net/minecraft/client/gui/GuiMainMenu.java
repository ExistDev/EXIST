package net.minecraft.client.gui;

import java.io.IOException;

import me.existdev.exist.gui.GuiChangeLog;
import me.existdev.exist.gui.GuiCredit;
import me.existdev.exist.gui.GuiSelectPlayMode;
import me.existdev.exist.gui.altchanger.GuiAltManager;
import me.existdev.exist.gui.button.MainMenuButton;
import me.existdev.exist.gui.particle.Particle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiMainMenu extends GuiScreen {

	private ResourceLocation background = new ResourceLocation("Exist/background.png");
	Minecraft mc = Minecraft.getMinecraft();

	@Override
	public void initGui() {
		super.initGui();
		String Play = I18n.format("PLAY", new Object[0]);
		String AltChanger = "ALTCHANGER";
		String Setting = I18n.format("COSTOMIZE", new Object[0]);
		String ChangeLog = "CHANGELOG";
		String Credit = "CREDIT";
		int initHeight = GuiMainMenu.height / 2;
		int objHeight = 17;
		int objWidth = 63;
		int xMid = GuiMainMenu.width / 5;

		if (mc.gameSettings.guiScale == 0) {
			// Auto
			this.buttonList.add(new MainMenuButton(0, xMid - 50, initHeight + 100, objWidth, objHeight, Play));
			this.buttonList.add(new MainMenuButton(1, xMid + 35, initHeight + 100, objWidth, objHeight, AltChanger));
			this.buttonList.add(new MainMenuButton(2, xMid + 120, initHeight + 100, objWidth, objHeight, Setting));
			this.buttonList.add(new MainMenuButton(3, xMid + 205, initHeight + 100, objWidth, objHeight, ChangeLog));
			this.buttonList.add(new MainMenuButton(4, xMid + 290, initHeight + 100, objWidth, objHeight, Credit));
		} else if (mc.gameSettings.guiScale == 1) {
			// Small
			this.buttonList.add(new MainMenuButton(0, xMid + 40, initHeight + 150, objWidth, objHeight, Play));
			this.buttonList.add(new MainMenuButton(1, xMid + 125, initHeight + 150, objWidth, objHeight, AltChanger));
			this.buttonList.add(new MainMenuButton(2, xMid + 210, initHeight + 150, objWidth, objHeight, Setting));
			this.buttonList.add(new MainMenuButton(3, xMid + 295, initHeight + 150, objWidth, objHeight, ChangeLog));
			this.buttonList.add(new MainMenuButton(4, xMid + 380, initHeight + 150, objWidth, objHeight, Credit));
		} else if (mc.gameSettings.guiScale == 2) {
			// Normal
			this.buttonList.add(new MainMenuButton(0, xMid + 80, initHeight + 190, objWidth, objHeight, Play));
			this.buttonList.add(new MainMenuButton(1, xMid + 165, initHeight + 190, objWidth, objHeight, AltChanger));
			this.buttonList.add(new MainMenuButton(2, xMid + 250, initHeight + 190, objWidth, objHeight, Setting));
			this.buttonList.add(new MainMenuButton(3, xMid + 335, initHeight + 190, objWidth, objHeight, ChangeLog));
			this.buttonList.add(new MainMenuButton(4, xMid + 420, initHeight + 190, objWidth, objHeight, Credit));
		} else if (mc.gameSettings.guiScale == 3) {
			// Large
			this.buttonList.add(new MainMenuButton(0, xMid, initHeight + 150, objWidth, objHeight, Play));
			this.buttonList.add(new MainMenuButton(1, xMid + 85, initHeight + 150, objWidth, objHeight, AltChanger));
			this.buttonList.add(new MainMenuButton(2, xMid + 170, initHeight + 150, objWidth, objHeight, Setting));
			this.buttonList.add(new MainMenuButton(3, xMid + 255, initHeight + 150, objWidth, objHeight, ChangeLog));
			this.buttonList.add(new MainMenuButton(4, xMid + 340, initHeight + 150, objWidth, objHeight, Credit));
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiSelectPlayMode());
		} else if (button.id == 2) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		} else if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiAltManager());
		} else if (button.id == 3) {
			this.mc.displayGuiScreen(new GuiChangeLog());
		} else if (button.id == 4) {
			this.mc.displayGuiScreen(new GuiCredit());
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int w = sr.getScaledWidth();
		int h = sr.getScaledHeight();
		this.mc.getTextureManager().bindTexture(background);
		GuiMainMenu.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, w + 2, h, w + 2, h, w + 2, h);
		GlStateManager.enableBlend();
		GlStateManager.disableBlend();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
