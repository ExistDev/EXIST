package me.existdev.exist.module.modules.player;

import java.util.ArrayList;
import java.util.HashMap;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.collect.Maps;

import me.existdev.exist.Exist;
import me.existdev.exist.module.Module;
import me.existdev.exist.setting.Setting;
import me.existdev.exist.utils.InvUtils;
import me.existdev.exist.utils.helper.TimeHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0BPacketEntityAction.Action;
import net.minecraft.network.play.client.C0DPacketCloseWindow;

public class InvCleaner extends Module {

	private InvUtils invhelper = new InvUtils();
	private TimeHelper delay = new TimeHelper();
	private TimeHelper delay1 = new TimeHelper();
	private TimeHelper startdelay = new TimeHelper();
	public boolean cleaned = false;
	private int slot = -1;
	private ArrayList<Integer> usefullitems;

	private HashMap<Item, Integer> ticksExisted = Maps.newHashMap();

	public InvCleaner() {
		super("InvCleaner", 0, Category.Player);
		Exist.settingManager.addSetting(new Setting(this, "SortSlotSword", 1.0D, 1.0D, 9.0D, true));
		Exist.settingManager.addSetting(new Setting(this, "SortSlotBow", 2.0D, 1.0D, 9.0D, true));
		Exist.settingManager.addSetting(new Setting(this, "Delay", 5.0D, 0.0D, 100.0D, true));
		Exist.settingManager.addSetting(new Setting(this, "Ticks", 20, 0, 200, true));
		Exist.settingManager.addSetting(new Setting(this, "AAC", false));
		Exist.settingManager.addSetting(new Setting(this, "SortItems", false));
		Exist.settingManager.addSetting(new Setting(this, "OpenInv", true));
		Exist.settingManager.addSetting(new Setting(this, "NoMove", false));
		Exist.settingManager.addSetting(new Setting(this, "Packet", false));

		this.usefullitems = new ArrayList();
		this.usefullitems.add(264);
		this.usefullitems.add(266);
		this.usefullitems.add(265);
		this.usefullitems.add(336);
		this.usefullitems.add(345);
	}

	@Override
	public void onUpdate() {
		if (!this.isToggled()) {
			return;
		}
		if (!(mc.currentScreen instanceof GuiInventory)) {
			return;
		}
		for (int i = 0; i < this.mc.thePlayer.inventory.getSizeInventory() - 4; i++) {
			if (!isUsefullItem(i)) {
				if (invhelper.getStackInSlot(i) != null) {
					Item ia = invhelper.getStackInSlot(i).getItem();
					if (this.ticksExisted.containsKey(ia)) {
						int ticks = getTicksExisted(ia);
						this.ticksExisted.put(ia, ticks + 1);

					} else {
						this.ticksExisted.put(ia, 1);
					}
				}
			}
		}
		boolean aac = Exist.settingManager.getSetting(this, "AAC").getBooleanValue();

		if (aac && Exist.settingManager.getSetting(this, "NoMove").getBooleanValue() && (mc.thePlayer.isMoving())) {
			return;
		}

		if (this.mc.currentScreen == null && Exist.settingManager.getSetting(this, "OpenInv").getBooleanValue()
				&& !Exist.settingManager.getSetting(this, "Packet").getBooleanValue()) {
			this.startdelay.reset();
			return;
		}
		if (!this.startdelay.hasReached(110L) || !(this.mc.currentScreen instanceof GuiInventory)
				&& Exist.settingManager.getSetting(this, "OpenInv").getBooleanValue()
				&& !Exist.settingManager.getSetting(this, "Packet").getBooleanValue()) {
			return;
		}
		if (mc.currentScreen instanceof GuiContainer && mc.thePlayer.openContainer != mc.thePlayer.inventoryContainer) {
			return;
		}
		this.cleaned = true;
		try {
			for (int i = 0; i < this.mc.thePlayer.inventory.getSizeInventory() - 4; i++) {
				if (!isUsefullItem(i)) {
					this.cleaned = false;
					if (this.delay.hasReached((long) Exist.settingManager.getSetting(this, "Delay").getCurrentValue()
							* (aac && (mc.thePlayer.isMoving()) ? 3 : 1))) {
						if (aac) {
							if (invhelper.getStackInSlot(i) != null) {
								Item ia = invhelper.getStackInSlot(i).getItem();
								if (this.getTicksExisted(ia) < Exist.settingManager.getSetting(this, "Ticks")
										.getCurrentValue())
									break;
							}
						}
						if (mc.currentScreen == null) {
							if (Exist.settingManager.getSetting(this, "Packet").getBooleanValue()) {
								mc.thePlayer.sendQueue
										.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.OPEN_INVENTORY));

							}
						}
						dropItem(i < 9 ? i + 36 : i);
						if (Exist.settingManager.getSetting(this, "Packet").getBooleanValue()) {
							mc.thePlayer.sendQueue
									.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));

						}

						this.delay.reset();

					}
				}
			}
			if (Exist.settingManager.getSetting(this, "SortItems").getBooleanValue()) {
				for (int i = 0; i < this.mc.thePlayer.inventory.getSizeInventory() - 4; i++) {
					if ((this.delay1.hasReached(15L)) && (this.cleaned)) {
						this.delay1.reset();
						if (mc.currentScreen == null) {
							if (Exist.settingManager.getSetting(this, "Packet").getBooleanValue()) {
								mc.thePlayer.sendQueue
										.addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, Action.OPEN_INVENTORY));

							}
						}
						sort();
						if (Exist.settingManager.getSetting(this, "Packet").getBooleanValue()) {
							mc.thePlayer.sendQueue
									.addToSendQueue(new C0DPacketCloseWindow(mc.thePlayer.openContainer.windowId));

						}
					}
				}
			}
		} catch (Exception e) {
		}
		super.onUpdate();
	}

	@Override
	public void onEnable() {
		this.ticksExisted.clear();
		super.onEnable();
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	private void dropItem(int slot) {
		int windowId = new GuiInventory(this.mc.thePlayer).inventorySlots.windowId;
		this.mc.playerController.windowClick(windowId, slot, 1, 4, this.mc.thePlayer);
	}

	private boolean isUsefullItem(int i1) {
		if (this.invhelper.getStackInSlot(i1) != null && this.invhelper.getStackInSlot(i1).getItem() != null) {
			ItemStack is = this.invhelper.getStackInSlot(i1);

			Item item = is.getItem();

			if (item == null) {
				return true;
			}
			if (item instanceof ItemFishingRod && this.invhelper.getFirstItem(item) == i1) {
				return true;
			}
			if (item instanceof ItemBow && this.invhelper.getFirstItem(item) == i1) {
				return true;
			}
			if (this.usefullitems.contains(Item.getIdFromItem(item))) {
				return true;
			}
			if (item instanceof ItemBlock) {
				return true;
			}
			if (item instanceof ItemFood) {
				return true;
			}
			if (item instanceof ItemSword && this.invhelper.isBestSword(i1)) {
				return true;
			}
			if (Item.getIdFromItem(item) == 262) {
				return true;
			}
			if (item instanceof ItemArmor) {
				ItemArmor armor = (ItemArmor) item;
				if (armor.armorType == 0 && this.invhelper.isBestHelmet(i1)) {
					return true;
				}
				if (armor.armorType == 1 && this.invhelper.isBestChest(i1)) {
					return true;
				}
				if (armor.armorType == 2 && this.invhelper.isBestLeggings(i1)) {
					return true;
				}
				if (armor.armorType == 3 && this.invhelper.isBestBoots(i1)) {
					return true;
				}
			}
			if (is.getItem() instanceof ItemTool && this.invhelper.getFirstItem(is.getItem()) == i1) {
				return true;
			}
			if (is.getItem() instanceof ItemPotion) {
				return true;
			}
			if (is.getItem() instanceof ItemFlintAndSteel) {
				return true;
			}
			if (is.getItem() instanceof ItemEnderPearl) {
				return true;
			}
		} else {
			return true;
		}
		return false;
	}

	private void sort() {
		for (int i1 = 0; i1 < 36; i1++) {
			if (this.invhelper.getStackInSlot(i1) != null && this.invhelper.getStackInSlot(i1).getItem() != null) {
				int i = i1;

				Item item = this.invhelper.getStackInSlot(i1).getItem();
				if (item instanceof ItemSword) {
					if (this.invhelper.isBestSword(i)) {
						this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId,
								i < 9 ? i + 36 : i,
								(int) (Exist.settingManager.getSetting(this, "SortSlotSword").getCurrentValue() - 1.0D),
								2, this.mc.thePlayer);
					}
				} else if (item instanceof ItemBow) {

				}
			}
		}
	}

	private int getTicksExisted(Item ia) {
		return this.ticksExisted.get(ia);
	}

}
