package me.existdev.exist.gui.altchanger;

import java.io.IOException;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import me.existdev.exist.Exist;
import me.existdev.exist.utils.RenderUtils;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class GuiAltManager extends GuiScreen {
	private static Minecraft mc = Minecraft.getMinecraft();
	private GuiButton login;
	private GuiButton remove;
	private GuiButton rename;
	private AltLoginThread loginThread;
	private int offset;
	public Alt selectedAlt = null;
	private String status = "\247fWaiting...";
	private static final ResourceLocation background = new ResourceLocation("Exist/background.png");

	public GuiAltManager() {
		Exist.fileManager.saveAlts();
	}

	public void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 0:
			if (this.loginThread == null) {
				mc.displayGuiScreen(null);
			} else {
				if (!this.loginThread.getStatus().equals("\2477Logging in...")) {
					if (!this.loginThread.getStatus().equals("\2471Do not hit back! \2477Logging in...")) {
						mc.displayGuiScreen(null);
						break;
					}
				}
				this.loginThread.setStatus("\2471Do not hit back! \247eLogging in...");
			}
			break;
		case 1:
			String user = this.selectedAlt.getUsername();
			String pass = this.selectedAlt.getPassword();
			this.loginThread = new AltLoginThread(user, pass);
			this.loginThread.start();
			break;
		case 2:
			if (this.loginThread != null) {
				this.loginThread = null;
			}
			Exist.altManager.getAlts().remove(this.selectedAlt);
			this.status = "\2474Removed.";
			this.selectedAlt = null;
			Exist.fileManager.saveAlts();
			break;
		case 3:
			mc.displayGuiScreen(new GuiAddAlt(this));
			break;
		case 5:
			Alt randomAlt =

					(Alt) Exist.altManager.getAlts().get(new Random().nextInt(Exist.altManager.getAlts().size()));
			String user1 = randomAlt.getUsername();
			String pass1 = randomAlt.getPassword();
			this.loginThread = new AltLoginThread(user1, pass1);
			this.loginThread.start();
			break;
		case 7:
			Alt lastAlt = Exist.altManager.getLastAlt();
			if (lastAlt == null) {
				if (this.loginThread == null) {
					this.status = "\247eThere is no last used alt!";
				} else {
					this.loginThread.setStatus("\247eThere is no last used alt!");
				}
			} else {
				String user2 = lastAlt.getUsername();
				String pass2 = lastAlt.getPassword();
				this.loginThread = new AltLoginThread(user2, pass2);
				this.loginThread.start();
			}
			break;
		case 8:
			break;
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		if (Mouse.hasWheel()) {
			int wheel = Mouse.getDWheel();
			if (wheel < 0) {
				this.offset += 26;
				if (this.offset < 0) {
					this.offset = 0;
				}
			} else if (wheel > 0) {
				this.offset -= 26;
				if (this.offset < 0) {
					this.offset = 0;
				}
			}
		}
		drawDefaultBackground();
		renderBackground(this.width, this.height);
		Exist.fontRenderer.fontRenderer40.drawStringWithShadow(this.mc.session.getUsername(), 10, 10, 0xFFFFFFFF);
		RenderUtils.drawRect(this.width - 370, 8.0F, this.width - 270, 28, Integer.MIN_VALUE);
		Exist.fontRenderer.helvetica50.drawCenteredString("Alt Manager", this.width / 2, 10, -1);
		RenderHelper.drawRect(40.0F, 33.0F, this.width - 400, this.height - 50, Integer.MIN_VALUE);
		RenderHelper.drawRect(this.width - 380, 33.0F, this.width - 20, this.height - 50, Integer.MIN_VALUE);
		GL11.glPushMatrix();
		prepareScissorBox(0.0F, 33.0F, this.width, this.height - 50);
		GL11.glEnable(3089);
		int y = 38;
		for (Alt alt : Exist.altManager.getAlts()) {
			if (isAltInArea(y)) {
				String name;
				if (alt.getMask().equals("")) {
					name = alt.getUsername();
				} else {
					name = alt.getMask();
				}
				String pass;
				if (alt.getPassword().equals("")) {
					pass = "\247cCracked";
				} else {
					pass = alt.getPassword().replaceAll(".", "*");
				}
				if (alt == this.selectedAlt) {
					if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
						RenderHelper.drawRect(42.0F, y - this.offset - 4, this.width - 402, y - this.offset + 20,
								-2142943931);
						RenderHelper.drawRect(42.0F, y - this.offset - 4, 44.0F, y - this.offset + 20, 0xFFFFFFFF);
					} else if (isMouseOverAlt(par1, par2, y - this.offset)) {
						RenderHelper.drawRect(42.0F, y - this.offset - 4, this.width - 402, y - this.offset + 20,
								-2142088622);
						RenderHelper.drawRect(42.0F, y - this.offset - 4, 44.0F, y - this.offset + 20, 0xFFFFFFFF);
					} else {
						RenderHelper.drawRect(42.0F, y - this.offset - 4, this.width - 402, y - this.offset + 20,
								-2144259791);
						RenderHelper.drawRect(42.0F, y - this.offset - 4, 44.0F, y - this.offset + 20, 0xFFFFFFFF);
					}
				} else if ((isMouseOverAlt(par1, par2, y - this.offset)) && (Mouse.isButtonDown(0))) {
					RenderHelper.drawRect(42.0F, y - this.offset - 4, this.width - 402, y - this.offset + 20,
							-2146101995);
					RenderHelper.drawRect(42.0F, y - this.offset - 4, 44.0F, y - this.offset + 20, 0xFFFFFFFF);
				} else if (isMouseOverAlt(par1, par2, y - this.offset)) {
					RenderHelper.drawRect(42.0F, y - this.offset - 4, this.width - 402, y - this.offset + 20,
							-2145180893);
					RenderHelper.drawRect(42.0F, y - this.offset - 4, 44.0F, y - this.offset + 20, 0xFFFFFFFF);
				}
				Exist.fontRenderer.helvetica40.drawCenteredString(name, this.width / 4, y - this.offset, -1);
				Exist.fontRenderer.helvetica40.drawCenteredString(pass, this.width / 4, y - this.offset + 10, 5592405);
				try {
					AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(name), name)
							.loadTexture(Minecraft.getMinecraft().getResourceManager());
					Minecraft.getMinecraft().getTextureManager()
							.bindTexture(AbstractClientPlayer.getLocationSkin(name));
					GL11.glColor4f(1F, 1F, 1F, 1F);
					Gui.drawScaledCustomSizeModalRect(this.width / 5 - 80, y - this.offset - 2, 8, 8, 8, 8, 20, 20, 64,
							64);
				} catch (IOException e) {

				}
				if (selectedAlt != null) {
					try {
						AbstractClientPlayer
								.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(selectedAlt.getUsername()),
										selectedAlt.getUsername())
								.loadTexture(Minecraft.getMinecraft().getResourceManager());
						Minecraft.getMinecraft().getTextureManager()
								.bindTexture(AbstractClientPlayer.getLocationSkin(selectedAlt.getUsername()));
						GL11.glColor4f(1F, 1F, 1F, 1F);
						Gui.drawScaledCustomSizeModalRect(this.width - 220, 60, 8, 8, 8, 8, 40, 40, 64, 64);
					} catch (IOException e) {
					}
					RenderHelper.drawRect(this.width - 340, 126, this.width - 60, 128, 0x90000000);

					Exist.fontRenderer.helvetica60.drawCenteredString("MAIL", this.width - 200, 160, -1);
					Exist.fontRenderer.helveticaChat.drawCenteredString(selectedAlt.getUsername(), this.width - 200,
							190, -1);
					Exist.fontRenderer.helvetica60.drawCenteredString("PASS", this.width - 200, 220, -1);
					Exist.fontRenderer.helveticaChat.drawCenteredString(selectedAlt.getPassword(), this.width - 200,
							250, -1);
				}
				y += 26;
			}
		}
		GL11.glDisable(3089);
		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
		if (this.selectedAlt == null) {
			this.login.enabled = false;
			this.remove.enabled = false;
			this.rename.enabled = false;
		} else {
			this.login.enabled = true;
			this.remove.enabled = true;
			this.rename.enabled = true;
		}
		if (Keyboard.isKeyDown(200)) {
			this.offset -= 26;
			if (this.offset < 0) {
				this.offset = 0;
			}
		} else if (Keyboard.isKeyDown(208)) {
			this.offset += 26;
			if (this.offset < 0) {
				this.offset = 0;
			}
		}
	}

	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 24, 75, 20, "Cancel"));
		this.buttonList.add(this.login = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login"));
		this.buttonList.add(this.remove = new GuiButton(2, this.width / 2 - 74, this.height - 24, 70, 20, "Remove"));
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add"));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
		this.buttonList.add(new GuiButton(5, this.width / 2 - 154, this.height - 24, 70, 20, "Random"));
		this.buttonList.add(this.rename = new GuiButton(6, this.width / 2 + 4, this.height - 24, 70, 20, "Rename"));
		this.buttonList.add(new GuiButton(7, this.width / 2 - 230, this.height - 24, 70, 20, "Last Alt"));
		this.buttonList.add(new GuiButton(8, this.width / 2 - 230, this.height - 48, 70, 20, "MCLeaks"));
		this.login.enabled = false;
		this.remove.enabled = false;
		this.rename.enabled = false;
	}

	private boolean isAltInArea(int y) {
		return y - this.offset <= this.height - 50;
	}

	private boolean isMouseOverAlt(int x, int y, int y1) {
		return (x >= 42) && (y >= y1 - 4) && (x <= this.width - 402) && (y <= y1 + 20) && (x >= 0) && (y >= 33)
				&& (x <= this.width) && (y <= this.height - 50);
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		if (this.offset < 0) {
			this.offset = 0;
		}
		int y = 38 - this.offset;
		for (Alt alt : Exist.altManager.getAlts()) {
			if (isMouseOverAlt(par1, par2, y)) {
				if (alt == this.selectedAlt) {
					actionPerformed((GuiButton) this.buttonList.get(1));
					return;
				}
				this.selectedAlt = alt;
			}
			y += 26;
		}
		try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void prepareScissorBox(float x, float y, float x2, float y2) {
		ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
		int factor = sr.getScaleFactor();
		GL11.glScissor((int) (x * factor), (int) ((sr.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor),
				(int) ((y2 - y) * factor));
	}

	public void renderBackground(int par1, int par2) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.mc.getTextureManager().bindTexture(background);
		Tessellator var3 = Tessellator.instance;
		var3.getWorldRenderer().startDrawingQuads();
		var3.getWorldRenderer().addVertexWithUV(0.0D, (double) par2, -90.0D, 0.0D, 1.0D);
		var3.getWorldRenderer().addVertexWithUV((double) par1, (double) par2, -90.0D, 1.0D, 1.0D);
		var3.getWorldRenderer().addVertexWithUV((double) par1, 0.0D, -90.0D, 1.0D, 0.0D);
		var3.getWorldRenderer().addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
		var3.draw();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

}
