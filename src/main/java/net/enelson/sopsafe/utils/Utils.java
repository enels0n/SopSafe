package net.enelson.sopsafe.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.enelson.sopli.lib.text.TextUtils;
import net.enelson.sopsafe.SopSafe;
import net.minecraft.nbt.NBTTagCompound;

public class Utils {
	private static final TextUtils TEXT_UTILS = new TextUtils();

	public static Location getDeserializedLocation(String s) {
		final String[] split = s.split(",");
		return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]),
				Double.parseDouble(split[3]));
	}

	public static String getSerializedLocation(Location loc) {
		return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
	}

	public static ItemStack setType(ItemStack item, String safeName) {
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = stack.w();
		tag.a("SopSafe", safeName);
		stack.c(tag);
		return CraftItemStack.asBukkitCopy(stack);
	}

	public static String getType(ItemStack item) {
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = stack.w();

		if (tag == null)
			return null;

		if (tag.e("SopSafe")) {
			return tag.l("SopSafe");
		}

		return null;
	}

	public static String getCode(ItemStack item) {
		if(item == null)
			return null;
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = stack.w();

		if (tag == null)
			return null;

		if (tag.e("SopSafe-code")) {
			return tag.l("SopSafe-code");
		}

		return null;
	}

	public static boolean isDublicate(ItemStack item) {
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = stack.w();

		return (tag != null && tag.e("SopSafe-dublicate"));
	}

	public static ItemStack setCode(ItemStack item, String code, boolean dublicate) {
		net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = stack.w();
		tag.a("SopSafe-code", code);
		if(dublicate)
			tag.a("SopSafe-dublicate", true);
		stack.c(tag);
		ItemStack item1 = CraftItemStack.asBukkitCopy(stack);
		item1.setAmount(1);
		ItemMeta meta  = item1.getItemMeta();
		meta.setDisplayName(TEXT_UTILS.color(SopSafe.manager.getLocale("safe_key_name")));
		if(dublicate) {
			List<String> lore = new ArrayList<String>();
			lore.add(TEXT_UTILS.color(SopSafe.manager.getLocale("dublicate")));
			meta.setLore(lore);
		}
		else
			meta.setLore(null);
		item1.setItemMeta(meta);
		return item1;
	}
}


