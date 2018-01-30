package me.existdev.exist.module.modules.combat;

import java.util.ArrayList;

import com.darkmagician6.eventapi.EventTarget;

import me.existdev.exist.Exist;
import me.existdev.exist.events.EventPreMotionUpdates;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.TimerUtils;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class AutoArmor extends Module {

	private int[] bestArmor;
	private final TimerUtils time = new TimerUtils();
	private final TimerUtils timeDelay = new TimerUtils();
	private ArrayList<ItemStack> currentArmor = new ArrayList();
	ArrayList<String> Modes = new ArrayList<>();
	private final int[] boots = new int[] { 313, 309, 317, 305, 301 };
	private final int[] chestplate = new int[] { 311, 307, 315, 303, 299 };
	private final int[] helmet = new int[] { 310, 306, 314, 302, 298 };
	private final int[] leggings = new int[] { 312, 308, 316, 304, 300 };
	private final TimeHelper time2 = new TimeHelper();

	public AutoArmor() {
		super("AutoArmor", 0, Category.Combat);
		Modes.add("Normal");
		Modes.add("OpenInv");
		Modes.add("Gomme");
		Exist.settingManager.addSetting(new Setting(this, "Mode", "Gomme", Modes));
		Exist.settingManager.addSetting(new Setting(this, "StartDelay", 100.0D, 1.0D, 5000.0D, true));
		Exist.settingManager.addSetting(new Setting(this, "Delay", 75.0D, 1.0D, 1000.0D, true));
	}

	@Override
	public void onEnable() {
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@EventTarget
	public void onUpdate(EventPreMotionUpdates e) {
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Gomme")) {
			return;
		}
		if (!this.isToggled()) {
			return;
		}
		setMode("Gomme");
		int item;
		int[] arrayOfInt;
		int j;
		int i;
		int id;
		if (mc.currentScreen instanceof GuiInventory) {
			if (!this.time2.isDelayComplete(800.0F)) {
				return;
			}

			if (mc.thePlayer.openContainer != null && mc.thePlayer.openContainer.windowId != 0) {
				return;
			}

			item = -1;
			if (mc.thePlayer.inventory.armorInventory[0] == null) {
				arrayOfInt = this.boots;
				j = this.boots.length;

				for (i = 0; i < j; ++i) {
					id = arrayOfInt[i];
					if (this.findItem(id) != -1) {
						item = this.findItem(id);
						break;
					}
				}
			}

			if (this.armourisBetter(0, this.boots)) {
				item = 8;
			}

			if (mc.thePlayer.inventory.armorInventory[3] == null) {
				arrayOfInt = this.helmet;
				j = this.helmet.length;

				for (i = 0; i < j; ++i) {
					id = arrayOfInt[i];
					if (this.findItem(id) != -1) {
						item = this.findItem(id);
						break;
					}
				}
			}

			if (this.armourisBetter(3, this.helmet)) {
				item = 5;
			}

			if (mc.thePlayer.inventory.armorInventory[1] == null) {
				arrayOfInt = this.leggings;
				j = this.leggings.length;

				for (i = 0; i < j; ++i) {
					id = arrayOfInt[i];
					if (this.findItem(id) != -1) {
						item = this.findItem(id);
						break;
					}
				}
			}

			if (this.armourisBetter(1, this.leggings)) {
				item = 7;
			}

			if (mc.thePlayer.inventory.armorInventory[2] == null) {
				arrayOfInt = this.chestplate;
				j = this.chestplate.length;

				for (i = 0; i < j; ++i) {
					id = arrayOfInt[i];
					if (this.findItem(id) != -1) {
						item = this.findItem(id);
						break;
					}
				}
			}

			if (this.armourisBetter(2, this.chestplate)) {
				item = 6;
			}

			if (item != -1) {
				mc.playerController.windowClick(0, item, 0, 1, mc.thePlayer);
				this.time2.setLastMS((long) item);
				return;
			}
		}
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("OpenInv")) {
			this.setMode("OpenInv");
		} else if (Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("Normal")) {
			this.setMode("Normal");
		}
		if (!(mc.currentScreen instanceof GuiInventory)) {
			this.timeDelay.updateLastMS();
		}
		if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("OpenInv")
				|| mc.currentScreen instanceof GuiInventory) {
			if (!Exist.settingManager.getSetting(this, "Mode").getCurrentOption().equalsIgnoreCase("OpenInv")
					|| !(mc.currentScreen instanceof GuiInventory) || this.timeDelay.hasTimePassedM(
							(long) Exist.settingManager.getSetting(this, "StartDelay").getCurrentValue())) {
				int[][] var5;
				int var4 = (var5 = new int[][] { { 6, 1 }, { 7, 2 }, { 5, 0 }, { 8, 3 } }).length;

				for (int var3 = 0; var3 < var4; ++var3) {
					int[] i = var5[var3];
					ItemStack currentArmor = mc.thePlayer.inventoryContainer.getSlot(i[0]).getStack();
					int result = this.findBetterArmor(i[1], currentArmor);
					if (result != -1 && this.time
							.hasTimePassedM((long) Exist.settingManager.getSetting(this, "Delay").getCurrentValue())) {
						if (currentArmor != null) {
							mc.playerController.windowClick(0, i[0], 0, 4, mc.thePlayer);
						} else {
							mc.playerController.windowClick(0, result, 0, 1, mc.thePlayer);
						}

						this.time.updateLastMS();
					}
				}
			}
		}
		super.onUpdate();
	}

	private int findBetterArmor(int itemType, ItemStack current) {
		int currentProtection = 0;
		int betterArmorSlot = -1;
		if (current != null && current.getItem() != null && current.getItem() instanceof ItemArmor) {
			currentProtection = ((ItemArmor) current.getItem()).getArmorMaterial().getDamageReductionAmount(itemType)
					+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { current }, DamageSource.generic);
		}

		for (int i = 9; i < 45; ++i) {
			ItemStack slot = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
			if (slot != null && slot.getItem() != null && slot.getItem() instanceof ItemArmor
					&& ((ItemArmor) slot.getItem()).armorType == itemType) {
				int slotProtection = ((ItemArmor) slot.getItem()).getArmorMaterial().getDamageReductionAmount(itemType)
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { slot },
								DamageSource.generic);
				if (slotProtection > currentProtection) {
					currentProtection = slotProtection;
					betterArmorSlot = i;
				}
			}
		}
		return betterArmorSlot;
	}

	public boolean armourisBetter(int slot, int[] armourtype) {
		if (mc.thePlayer.inventory.armorInventory[slot] != null) {
			int currentIndex = 0;
			int finalCurrentIndex = -1;
			int invIndex = 0;
			int finalInvIndex = -1;
			int[] arrayOfInt = armourtype;
			int j = armourtype.length;

			int i;
			int armour;
			for (i = 0; i < j; ++i) {
				armour = arrayOfInt[i];
				if (Item.getIdFromItem(mc.thePlayer.inventory.armorInventory[slot].getItem()) == armour) {
					finalCurrentIndex = currentIndex;
					break;
				}

				++currentIndex;
			}

			arrayOfInt = armourtype;
			j = armourtype.length;

			for (i = 0; i < j; ++i) {
				armour = arrayOfInt[i];
				if (this.findItem(armour) != -1) {
					finalInvIndex = invIndex;
					break;
				}
				++invIndex;
			}

			if (finalInvIndex > -1) {
				if (finalInvIndex < finalCurrentIndex) {
					return true;
				}
				return false;
			}
		}
		return false;
	}

	private int findItem(int id) {
		for (int index = 9; index < 45; ++index) {
			ItemStack item = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
			if (item != null && Item.getIdFromItem(item.getItem()) == id) {
				return index;
			}
		}
		return -1;
	}

	public class TimeHelper {
		private long lastMS = 0L;

		public TimeHelper() {
		}

		public boolean isDelayComplete(float f) {
			return (float) (System.currentTimeMillis() - lastMS) >= f;
		}

		public long getCurrentMS() {
			return System.nanoTime() / 1000000L;
		}

		public void setLastMS(long lastMS) {
			lastMS = System.currentTimeMillis();
		}

		public int convertToMS(int perSecond) {
			return 1000 / perSecond;
		}

		public boolean hasReached(double d) {
			return (double) (getCurrentMS() - lastMS) >= d;
		}

		public void reset() {
			lastMS = getCurrentMS();
		}
	}
}
