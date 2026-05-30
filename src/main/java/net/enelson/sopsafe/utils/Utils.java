package net.enelson.sopsafe.utils;

import net.enelson.sopli.lib.SopLib;
import net.enelson.sopli.lib.external.ItemNBTUtils;
import net.enelson.sopli.lib.item.ItemUtils;
import net.enelson.sopli.lib.text.TextUtils;
import net.enelson.sopsafe.SopSafe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {
    private static final TextUtils TEXT_UTILS = new TextUtils();
    private static final String KEY_TYPE = "SopSafe";
    private static final String KEY_CODE = "SopSafe-code";
    private static final String KEY_DUBLICATE = "SopSafe-dublicate";

    private static ItemUtils itemUtils() {
        return SopLib.getInstance().getItemUtils();
    }

    public static Location getDeserializedLocation(String s) {
        final String[] split = s.split(",");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                Double.parseDouble(split[3]));
    }

    public static String getSerializedLocation(Location loc) {
        return loc.getWorld().getName() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }

    public static ItemStack setType(ItemStack item, String safeName) {
        ItemStack updated = item.clone();
        ItemNBTUtils.setTags(updated, Arrays.asList(KEY_TYPE + "::" + safeName));
        return updated;
    }

    public static String getType(ItemStack item) {
        return itemUtils().getNBT(item, KEY_TYPE, String.class);
    }

    public static String getCode(ItemStack item) {
        if (item == null) {
            return null;
        }
        return itemUtils().getNBT(item, KEY_CODE, String.class);
    }

    public static boolean isDublicate(ItemStack item) {
        String value = itemUtils().getNBT(item, KEY_DUBLICATE, String.class);
        return value != null && Boolean.parseBoolean(value);
    }

    public static ItemStack setCode(ItemStack item, String code, boolean dublicate) {
        ItemStack updated = item.clone();
        updated.setAmount(1);

        List<String> tags = new ArrayList<String>();
        String safeType = getType(updated);
        if (safeType != null) {
            tags.add(KEY_TYPE + "::" + safeType);
        }
        tags.add(KEY_CODE + "::" + code);
        if (dublicate) {
            tags.add(KEY_DUBLICATE + "::true");
        }
        ItemNBTUtils.setTags(updated, tags);

        ItemMeta meta = updated.getItemMeta();
        meta.setDisplayName(TEXT_UTILS.color(SopSafe.manager.getLocale("safe_key_name")));
        if (dublicate) {
            List<String> lore = new ArrayList<String>();
            lore.add(TEXT_UTILS.color(SopSafe.manager.getLocale("dublicate")));
            meta.setLore(lore);
        } else {
            meta.setLore(null);
        }
        updated.setItemMeta(meta);
        return updated;
    }
}
