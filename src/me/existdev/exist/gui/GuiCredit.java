package me.existdev.exist.gui;

import java.io.IOException;

import me.existdev.exist.Exist;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiCredit extends GuiScreen {

	Minecraft mc = Minecraft.getMinecraft();
	private ResourceLocation background = new ResourceLocation("Exist/background.png");

	@Override
	public void initGui() {
		super.initGui();
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		String Back = "Back";
		int initHeight = GuiMainMenu.height / 2;
		int objHeight = 17;
		int objWidth = 63;
		int xMid = GuiMainMenu.width / 5;

		if (mc.gameSettings.guiScale == 0) {
			// Auto
			this.buttonList.add(new GuiButton(0, xMid + 120, initHeight + 100, objWidth, objHeight, Back));
		} else if (mc.gameSettings.guiScale == 1) {
			// Small
			this.buttonList.add(new GuiButton(0, xMid + 210, initHeight + 150, objWidth, objHeight, Back));
		} else if (mc.gameSettings.guiScale == 2) {
			// Normal
			this.buttonList.add(new GuiButton(0, xMid + 250, initHeight + 190, objWidth, objHeight, Back));
		} else if (mc.gameSettings.guiScale == 3) {
			// Large
			this.buttonList.add(new GuiButton(0, xMid + 170, initHeight + 150, objWidth, objHeight, Back));

		}
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
		Exist.fontRenderer.fontRenderer50.drawStringWithShadow("Credit", this.width / 5 + 170, height / 5 - 50,
				0xFFFFFFFF);
		GlStateManager.enableBlend();
		GlStateManager.disableBlend();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
