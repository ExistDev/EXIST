package me.existdev.exist.gui;

import java.io.IOException;

import me.existdev.exist.Exist;
import me.existdev.exist.gui.altchanger.GuiAltManager;
import me.existdev.exist.gui.button.MainMenuButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiStartMenu extends GuiScreen {

	Minecraft mc = Minecraft.getMinecraft();
	private ResourceLocation background = new ResourceLocation("Exist/background.png");

	@Override
	public void initGui() {
		super.initGui();
		String start = I18n.format("Start!", new Object[0]);
		int initHeight = this.height / 4 + 20;
		int objHeight = 17;
		int objWidth = 63;
		int xMid = this.width / 2 - 75;

		this.buttonList.add(new MainMenuButton(0, xMid, initHeight + 100, objWidth, objHeight, start));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiMainMenu());
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int w = sr.getScaledWidth();
		int h = sr.getScaledHeight();
		this.mc.getTextureManager().bindTexture(this.background);
		GuiMainMenu.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, w + 2, h, w + 2, h, w + 2, h);
		GlStateManager.enableBlend();
		GlStateManager.disableBlend();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
