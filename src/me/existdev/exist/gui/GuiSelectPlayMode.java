package me.existdev.exist.gui;

import java.io.IOException;

import me.existdev.exist.Exist;
import me.existdev.exist.gui.button.MainMenuButton;
import me.existdev.exist.gui.button.PlayModeButton;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiSelectPlayMode extends GuiScreen {

	Minecraft mc = Minecraft.getMinecraft();
	private ResourceLocation background = new ResourceLocation("Exist/background.png");

	@Override
	public void initGui() {
		super.initGui();
		String SinglePlayer = I18n.format("SinglePlayer", new Object[0]);
		String MultiPlayer = I18n.format("MultiPlayer", new Object[0]);
		String Back = I18n.format("Back", new Object[0]);
		int initHeight = this.height / 2 + 40;
		int objHeight = 17;
		int objWidth = 63;
		int xMid = this.width / 2 - 75;

		this.buttonList.add(new PlayModeButton(0, 0, initHeight, objWidth, objHeight, SinglePlayer));
		this.buttonList.add(new PlayModeButton(1, 0, initHeight + 25, objWidth, objHeight, MultiPlayer));
		this.buttonList.add(new PlayModeButton(2, 0, initHeight + 100, objWidth, objHeight, Back));
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}
		if (button.id == 1) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}
		if (button.id == 2) {
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
		RenderHelper.drawRect(0, 0, sr.getScaledWidth() / 5, sr.getScaledHeight(), Integer.MIN_VALUE);
		GlStateManager.enableBlend();
		GlStateManager.disableBlend();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
