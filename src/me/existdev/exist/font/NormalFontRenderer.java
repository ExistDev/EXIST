package me.existdev.exist.font;

import java.awt.Font;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class NormalFontRenderer {

	public static Minecraft mc = Minecraft.getMinecraft();

	public static final FontRenderer defaultFontRenderer = new FontRenderer(mc.gameSettings,
			new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), false);

	// ------------------------------------------------------------------------------------------------------------

	public static final NormalFontManager fontRenderer70 = new NormalFontManager(getFont(70), true, 8);
	public static final NormalFontManager fontRenderer60 = new NormalFontManager(getFont(60), true, 8);
	public static final NormalFontManager fontRenderer50 = new NormalFontManager(getFont(50), true, 8);
	public static final NormalFontManager fontRenderer40 = new NormalFontManager(getFont(40), true, 8);
	public static final NormalFontManager fontRenderer30 = new NormalFontManager(getFont(30), true, 8);
	public static final NormalFontManager fontRenderer20 = new NormalFontManager(getFont(20), true, 8);
	public static final NormalFontManager fontRenderer10 = new NormalFontManager(getFont(10), true, 8);
	// -------------------------------------------------------------------------------------------------------------

	public static final NormalFontManager comfortaa70 = new NormalFontManager(getComfortaaFont(70), true, 8);
	public static final NormalFontManager comfortaa60 = new NormalFontManager(getComfortaaFont(60), true, 8);
	public static final NormalFontManager comfortaa50 = new NormalFontManager(getComfortaaFont(50), true, 8);
	public static final NormalFontManager comfortaa40 = new NormalFontManager(getComfortaaFont(40), true, 8);
	public static final NormalFontManager comfortaa30 = new NormalFontManager(getComfortaaFont(30), true, 8);
	public static final NormalFontManager comfortaa20 = new NormalFontManager(getComfortaaFont(20), true, 8);
	public static final NormalFontManager comfortaa10 = new NormalFontManager(getComfortaaFont(10), true, 8);

	// ---------------------------------------------------------------------------------------------------------------

	public static final NormalFontManager helvetica70 = new NormalFontManager(getHelveticaFont(70), true, 8);
	public static final NormalFontManager helvetica60 = new NormalFontManager(getHelveticaFont(60), true, 8);
	public static final NormalFontManager helvetica50 = new NormalFontManager(getHelveticaFont(50), true, 8);
	public static final NormalFontManager helvetica40 = new NormalFontManager(getHelveticaFont(40), true, 8);
	public static final NormalFontManager helvetica30 = new NormalFontManager(getHelveticaFont(30), true, 8);
	public static final NormalFontManager helvetica20 = new NormalFontManager(getHelveticaFont(20), true, 8);
	public static final NormalFontManager helvetica10 = new NormalFontManager(getHelveticaFont(10), true, 8);

	// ---------------------------------------------------------------------------------------------------------------

	public static final NormalFontManager fontRendererArrayList = new NormalFontManager(getFont(35), true, 8);
	public static final NormalFontManager fontRendererTabGUI = new NormalFontManager(getFont(35), true, 8);
	public static final NormalFontManager comfortaaArrayList = new NormalFontManager(getComfortaaFont(35), true, 8);
	public static final NormalFontManager comfortaaTabGUI = new NormalFontManager(getComfortaaFont(35), true, 8);
	public static final NormalFontManager helveticaArrayList = new NormalFontManager(getHelveticaFont(35), true, 8);
	public static final NormalFontManager helveticaTabGUI = new NormalFontManager(getHelveticaFont(35), true, 8);
	public static final NormalFontManager helveticaChat = new NormalFontManager(getHelveticaFont(34), true, 8);
	public static final NormalFontManager helveticaTextField = new NormalFontManager(getHelveticaFont(34), true, 8);

	// ---------------------------------------------------------------------------------------------------------------

	public static final NormalFontManager title = new NormalFontManager(getButtonFont(200), true, 8);

	private static Font getFont(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Exist/MainLarge.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, size);
		}
		return font;
	}

	private static Font getComfortaaFont(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Exist/Comfortaa_Regular.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, size);
		}
		return font;
	}

	private static Font getHelveticaFont(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Exist/_H_HelveticaNeueInterface_2.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, size);
		}
		return font;
	}

	private static Font getButtonFont(int size) {
		Font font = null;
		try {
			InputStream is = Minecraft.getMinecraft().getResourceManager()
					.getResource(new ResourceLocation("Exist/ButtonFont.ttf")).getInputStream();
			font = Font.createFont(0, is);
			font = font.deriveFont(0, size);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Error loading font");
			font = new Font("default", 0, size);
		}
		return font;
	}

}
