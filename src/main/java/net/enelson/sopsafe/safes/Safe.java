package net.enelson.sopsafe.safes;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

import net.enelson.sopsafe.utils.Utils;

public class Safe {
	private Location location;
	private int durability;
	private Long time;

	Safe(Block block, int durability) {
		this.location = block.getLocation();
		this.time = System.currentTimeMillis();
		Chest chest1 = (Chest) block.getState();
		Inventory inventory = chest1.getInventory();
		if (inventory instanceof DoubleChestInventory) {
			DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
			this.location = doubleChest.getLocation().getBlock().getLocation();
		}
		this.durability = durability;
	}

	Safe(Block block, Long time, int durability) {
		this.location = block.getLocation();
		this.time = time;
		Chest chest1 = (Chest) block.getState();
		Inventory inventory = chest1.getInventory();
		if (inventory instanceof DoubleChestInventory) {
			DoubleChest doubleChest = (DoubleChest) inventory.getHolder();
			this.location = doubleChest.getLocation().getBlock().getLocation();
		}
		this.durability = durability;
	}

	public Location getLocation() {
		return this.location;
	}

	public Long getTime() {
		return this.time;
	}

	public int getDurability() {
		return this.durability;
	}

	public boolean checkInventory(Inventory inv) {
		Chest chest1 = (Chest) this.getLocation().getBlock().getState();
		Inventory inventory = chest1.getInventory();
		return inventory.equals(inv);
	}

	public String getCode() {
		return this.time + ":" + Utils.getSerializedLocation(this.location);
	}

	public void removeDurability(int durability) {
		this.durability = (this.durability - durability < 0 ? 0 : this.durability - durability);
	}
}


