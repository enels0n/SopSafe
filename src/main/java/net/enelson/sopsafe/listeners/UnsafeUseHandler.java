package net.enelson.sopsafe.listeners;

import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.enelson.sopsafe.SopSafe;
import net.enelson.sopsafe.utils.Utils;

public class UnsafeUseHandler implements Listener {
	@EventHandler
	public void onPrepareItemEnchant(PrepareItemEnchantEvent e) {
		if (Utils.getType(e.getItem()) != null) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (Utils.getType(e.getItemInHand()) != null) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPrepareItemAnvil(InventoryClickEvent e) {
		if (e.getInventory().getType() == InventoryType.ANVIL && ((Utils.getType(e.getInventory().getItem(0)) != null
				&& Utils.getCode(e.getInventory().getItem(0)) == null)
				|| (Utils.getType(e.getInventory().getItem(2)) != null
						&& Utils.getCode(e.getInventory().getItem(2)) == null))) {
			ItemMeta oldMeta = e.getInventory().getItem(0).getItemMeta();
			ItemMeta meta = e.getCurrentItem().getItemMeta();
			if (meta.hasDisplayName()) {
				if (!meta.getDisplayName().equals(oldMeta.getDisplayName())) {
					this.dontRename((Player) e.getWhoClicked(), e.getCurrentItem(), meta, oldMeta);
				}
			} else if (meta.hasDisplayName() && !oldMeta.hasDisplayName()) {
				this.dontRename((Player) e.getWhoClicked(), e.getCurrentItem(), meta, oldMeta);
			}
		}
	}

	private void dontRename(Player p, ItemStack item, ItemMeta meta, ItemMeta oldMeta) {
		meta.setDisplayName(oldMeta.getDisplayName());
		item.setItemMeta(meta);
	}
	
	@EventHandler
	public void onHopperMove(InventoryMoveItemEvent e) {
		if(!(e.getSource().getLocation().getBlock().getState() instanceof Chest))
			return;
		
		if(SopSafe.manager.getSafe(e.getSource().getLocation().getBlock()) != null)
			e.setCancelled(true);
	}
}


