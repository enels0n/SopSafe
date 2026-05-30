package net.enelson.sopsafe.safes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import net.enelson.sopsafe.SopSafe;
import net.enelson.sopsafe.utils.Utils;

public class SafeManager {
	private File fileConfig;
	private YamlConfiguration config;
	private File fileSafes;
	private YamlConfiguration configSafes;
	private List<Safe> safes;
	private BukkitTask task;
	private ShapedRecipe recipe;
	private NamespacedKey key;

	public SafeManager() {
		this.fileConfig = new File(SopSafe.plugin.getDataFolder(), "config.yml");
		if (!this.fileConfig.exists())
			SopSafe.plugin.saveResource("config.yml", true);
		this.config = YamlConfiguration.loadConfiguration(this.fileConfig);

		this.fileSafes = new File(SopSafe.plugin.getDataFolder(), "safes.yml");
		if (!this.fileSafes.exists())
			SopSafe.plugin.saveResource("safes.yml", true);
		this.configSafes = YamlConfiguration.loadConfiguration(this.fileSafes);

		this.safes = new ArrayList<>();
		for(String id : this.configSafes.getConfigurationSection("safes").getKeys(false)) {
			Location location = Utils.getDeserializedLocation(id);
			if(!location.getBlock().getType().equals(Material.CHEST)) {
				this.configSafes.set("safes." + id, null);
				continue;
			}
			Long time = this.configSafes.getLong("safes."+id+".time");
			int durability = this.configSafes.getInt("safes."+id+".durability");
			Safe safe = new Safe(location.getBlock(), time, durability);
			this.safes.add(safe);
		}
		this.task = Bukkit.getScheduler().runTaskTimer(SopSafe.plugin, new Runnable() {
			@Override
			public void run() {
				for(Safe safe : safes) {
					addToConfig(safe);
				}
				saveAll();
			}
		}, 120, 120);
		

		ItemStack result = new ItemStack(Material.TRIPWIRE_HOOK);
		ItemMeta metaResult = result.getItemMeta();
		metaResult.setDisplayName("Dublicate of key");
		result.setItemMeta(metaResult);
		this.key = new NamespacedKey(SopSafe.plugin, "sopsafe_dublicate_key2");
		this.recipe = new ShapedRecipe(this.key, result);
		this.recipe.shape("OE ");
		
		this.recipe.setIngredient('O', Material.TRIPWIRE_HOOK);
		this.recipe.setIngredient('E', Material.TRIPWIRE_HOOK);

		Bukkit.addRecipe(this.recipe);
	}

	public ItemStack generateKey(String safeName, int amount) {
		ItemStack item = Utils.setType(
				new ItemStack(Material.valueOf(this.config.getString("safes." + safeName + ".item.type"))), safeName);
		item.setAmount(amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.config.getString("safes." + safeName + ".item.name"));
		meta.setLore(this.config.getStringList("safes." + safeName + ".item.lore"));
		meta.setCustomModelData(this.config.getInt("safes." + safeName + ".item.model"));
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack generateDublicate(String safeName, String code) {
		ItemStack item = Utils.setCode(SopSafe.manager.generateKey(safeName, 1), code, true);
		return item;
	}

	public Safe getSafe(Block block) {
		Chest chest1 = (Chest) block.getState();
		Inventory inventory = chest1.getInventory();
		if (inventory instanceof DoubleChestInventory) {
			DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
			return this.safes.stream().filter(s -> s.getLocation().equals(doubleChest.getLocation().getBlock().getLocation())).findFirst().orElse(null);
		}
		return this.safes.stream().filter(s -> s.getLocation().equals(block.getLocation())).findFirst().orElse(null);
	}

	public boolean checkSafeInventory(Inventory inv) {
		return this.safes.stream().filter(s -> s.checkInventory(inv)).findFirst().orElse(null) != null;
	}

	public Safe createSafe(String type, Block block) {
		Safe safe;
		int durability = this.config.getInt("safes."+type+".durability");
		safe = new Safe(block, durability);
		
		this.safes.add(safe);
		this.addToConfig(safe);
		this.saveAll();
		return safe;
	}
	
	public void removeSafe(Safe safe) {
		this.configSafes.set("safes." + Utils.getSerializedLocation(safe.getLocation()), null);
		this.safes.remove(safe);
	}
	
	private void addToConfig(Safe safe) {
		this.configSafes.set("safes." + Utils.getSerializedLocation(safe.getLocation()) + ".time", safe.getTime());
		this.configSafes.set("safes." + Utils.getSerializedLocation(safe.getLocation()) + ".durability", safe.getDurability());
	}
	
	private void saveAll() {
		try {
			this.configSafes.save(this.fileSafes);
		} catch (IOException e) {
		}
	}
	
	public ShapedRecipe getDublicateRecipe() {
		return this.recipe;
	}
	
	public void onDisable() {
		this.task.cancel();
		Bukkit.removeRecipe(this.key);
	}
	
	public String getLocale(String path) {
		return this.config.getString("locale."+path);
	}
}


