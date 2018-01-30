package me.existdev.exist.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;

public class InvUtils {
	private Minecraft mc = Minecraft.getMinecraft();

	public void dropSlot(final int slot) {
		final int windowId = new GuiInventory(mc.thePlayer).inventorySlots.windowId;
		mc.playerController.windowClick(windowId, slot, 1, 4, mc.thePlayer);
	}

	public ItemStack getStackInSlot(int slot) {
		return mc.thePlayer.inventory.getStackInSlot(slot);
	}

	public boolean isBestChest(int slot) {

		if (getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null
				&& getStackInSlot(slot).getItem() instanceof ItemArmor) {
			int slotProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);

			if (mc.thePlayer.inventory.armorInventory[2] != null) {
				ItemArmor ia = (ItemArmor) mc.thePlayer.inventory.armorInventory[2].getItem();
				ItemStack is = mc.thePlayer.inventory.armorInventory[2];

				ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

				int otherProtection = ((ItemArmor) is.getItem()).getArmorMaterial()
						.getDamageReductionAmount(getItemType(is))
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
				if (otherProtection > slotProtection || otherProtection == slotProtection) {
					return false;
				}

			}

			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor) {
					int otherProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem())
							.getArmorMaterial()
							.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
							+ EnchantmentHelper.getEnchantmentModifierDamage(
									new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);

					ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

					ItemArmor ia2 = (ItemArmor) getStackInSlot(i).getItem();

					if (ia1.armorType == 1 && ia2.armorType == 1) {
						if (otherProtection > slotProtection) {
							return false;
						}
					}
				}

			}
		}

		return true;
	}

	public boolean isBestHelmet(int slot) {
		if (getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null
				&& getStackInSlot(slot).getItem() instanceof ItemArmor) {
			int slotProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);

			if (mc.thePlayer.inventory.armorInventory[3] != null) {
				ItemArmor ia = (ItemArmor) mc.thePlayer.inventory.armorInventory[3].getItem();
				ItemStack is = mc.thePlayer.inventory.armorInventory[3];

				ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

				int otherProtection = ((ItemArmor) is.getItem()).getArmorMaterial()
						.getDamageReductionAmount(getItemType(is))
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
				if (otherProtection > slotProtection || otherProtection == slotProtection) {
					return false;
				}

			}

			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor) {
					int otherProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem())
							.getArmorMaterial()
							.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
							+ EnchantmentHelper.getEnchantmentModifierDamage(
									new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);

					ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

					ItemArmor ia2 = (ItemArmor) getStackInSlot(i).getItem();

					if (ia1.armorType == 0 && ia2.armorType == 0) {
						if (otherProtection > slotProtection) {
							return false;
						}
					}

				}

			}
		}

		return true;
	}

	public boolean isBestLeggings(int slot) {

		if (getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null
				&& getStackInSlot(slot).getItem() instanceof ItemArmor) {
			int slotProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);
			if (mc.thePlayer.inventory.armorInventory[1] != null) {
				ItemArmor ia = (ItemArmor) mc.thePlayer.inventory.armorInventory[1].getItem();
				ItemStack is = mc.thePlayer.inventory.armorInventory[1];

				ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

				int otherProtection = ((ItemArmor) is.getItem()).getArmorMaterial()
						.getDamageReductionAmount(getItemType(is))
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
				if (otherProtection > slotProtection || otherProtection == slotProtection) {
					return false;
				}

			}

			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor) {
					int otherProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem())
							.getArmorMaterial()
							.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
							+ EnchantmentHelper.getEnchantmentModifierDamage(
									new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
					ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

					ItemArmor ia2 = (ItemArmor) getStackInSlot(i).getItem();

					if (ia1.armorType == 2 && ia2.armorType == 2) {
						if (otherProtection > slotProtection) {
							return false;
						}
					}
				}

			}
		}

		return true;
	}

	public boolean isBestBoots(int slot) {

		if (getStackInSlot(slot) != null && getStackInSlot(slot).getItem() != null
				&& getStackInSlot(slot).getItem() instanceof ItemArmor) {
			int slotProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(slot).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(slot)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(slot) }, DamageSource.generic);

			if (mc.thePlayer.inventory.armorInventory[0] != null) {
				ItemArmor ia = (ItemArmor) mc.thePlayer.inventory.armorInventory[0].getItem();
				ItemStack is = mc.thePlayer.inventory.armorInventory[0];

				ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

				int otherProtection = ((ItemArmor) is.getItem()).getArmorMaterial()
						.getDamageReductionAmount(getItemType(is))
						+ EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[] { is }, DamageSource.generic);
				if (otherProtection > slotProtection || otherProtection == slotProtection) {
					return false;
				}

			}

			for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
				if (getStackInSlot(i) == null) {
					continue;
				}
				if (mc.thePlayer.inventory.getStackInSlot(i).getItem() instanceof ItemArmor) {
					int otherProtection = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem())
							.getArmorMaterial()
							.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
							+ EnchantmentHelper.getEnchantmentModifierDamage(
									new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);

					ItemArmor ia1 = (ItemArmor) getStackInSlot(slot).getItem();

					ItemArmor ia2 = (ItemArmor) getStackInSlot(i).getItem();

					if (ia1.armorType == 3 && ia2.armorType == 3) {
						if (otherProtection > slotProtection) {
							return false;
						}
					}
				}

			}
		}

		return true;
	}

	public boolean isBestSword(int slotIn) {
		return getBestWeapon() == slotIn;
	}

	public int getItemType(ItemStack itemStack) {
		if ((itemStack.getItem() instanceof ItemArmor)) {
			ItemArmor armor = (ItemArmor) itemStack.getItem();

			return armor.armorType;
		}
		return -1;
	}

	public float getItemDamage(ItemStack itemStack) {
		Multimap multimap = itemStack.getAttributeModifiers();
		Iterator iterator;
		if (!multimap.isEmpty() && (iterator = multimap.entries().iterator()).hasNext()) {
			Entry entry = (Entry) iterator.next();
			AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
			double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2
					? attributeModifier.getAmount() : attributeModifier.getAmount() * 100.0D;
			return attributeModifier.getAmount() > 1.0D ? 1.0F + (float) damage : 1.0F;
		} else {
			return 1.0F;
		}
	}

	public boolean hasItemMoreTimes(int slotIn) {

		boolean has = false;
		ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
		stacks.clear();

		for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
			if (!stacks.contains(getStackInSlot(i))) {
				stacks.add(getStackInSlot(i));
			} else {
				if (getStackInSlot(i) == getStackInSlot(slotIn)) {
					return true;
				}
			}

		}

		// for (ItemStack stack : stacks) {
		// if (stack == getStackInSlot(slotIn)) {
		// if (has) {
		// return true;
		// } else {
		// has = true;
		// }
		// }
		// }

		return false;
	}

	public int getBestWeaponInHotbar() {
		int originalSlot = mc.thePlayer.inventory.currentItem;
		byte weaponSlot = -1;
		float weaponDamage = 1.0F;

		for (byte slot = 0; slot < 9; ++slot) {
			ItemStack itemStack = mc.thePlayer.inventory.getStackInSlot(slot);
			if (itemStack != null) {
				float damage = getItemDamage(itemStack);
				if ((damage += EnchantmentHelper.func_152377_a(itemStack,
						EnumCreatureAttribute.UNDEFINED)) > weaponDamage) {
					weaponDamage = damage;
					weaponSlot = slot;
				}
			}
		}

		if (weaponSlot != -1) {
			return weaponSlot;
		} else {
			return originalSlot;
		}
	}

	public int getBestWeapon() {
		int originalSlot = mc.thePlayer.inventory.currentItem;
		byte weaponSlot = -1;
		float weaponDamage = 1.0F;

		for (byte slot = 0; slot < mc.thePlayer.inventory.getSizeInventory(); ++slot) {

			if (getStackInSlot(slot) == null) {
				continue;
			}
			ItemStack itemStack = getStackInSlot(slot);

			if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemSword) {
				float damage = getItemDamage(itemStack);
				if ((damage += EnchantmentHelper.func_152377_a(itemStack,
						EnumCreatureAttribute.UNDEFINED)) > weaponDamage) {
					weaponDamage = damage;
					weaponSlot = slot;
				}
			}
		}

		if (weaponSlot != -1) {
			return weaponSlot;
		} else {
			return originalSlot;
		}
	}

	public int getArmorProt(int i) {
		int armorprot = -1;
		if (getStackInSlot(i) != null && getStackInSlot(i).getItem() != null
				&& getStackInSlot(i).getItem() instanceof ItemArmor) {
			armorprot = ((ItemArmor) mc.thePlayer.inventory.getStackInSlot(i).getItem()).getArmorMaterial()
					.getDamageReductionAmount(getItemType(mc.thePlayer.inventory.getStackInSlot(i)))
					+ EnchantmentHelper.getEnchantmentModifierDamage(
							new ItemStack[] { mc.thePlayer.inventory.getStackInSlot(i) }, DamageSource.generic);
		}

		return armorprot;
	}

	public int getFirstItem(Item i1) {
		for (int i = 0; i < mc.thePlayer.inventory.getSizeInventory(); i++) {
			if (getStackInSlot(i) != null && getStackInSlot(i).getItem() != null) {
				if (getStackInSlot(i).getItem() == i1) {
					return i;
				}
			}
		}
		return -1;
	}

}
