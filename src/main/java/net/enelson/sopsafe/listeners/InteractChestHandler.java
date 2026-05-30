package net.enelson.sopsafe.listeners;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.enelson.sopsafe.SopSafe;
import net.enelson.sopsafe.safes.Safe;
import net.enelson.sopsafe.utils.Utils;

public class InteractChestHandler implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onClick(PlayerInteractEvent e) {		
		if(e.getClickedBlock() == null || !(e.getClickedBlock().getState() instanceof Chest) || !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
			return;

		Safe safe = SopSafe.manager.getSafe(e.getClickedBlock());
		
		if(safe != null) {
			if(safe.getDurability() == 0) {
				SopSafe.manager.removeSafe(safe);
				return;
			}

			if(this.canOpen(safe, (Player)e.getPlayer())) {
				e.setUseInteractedBlock(Result.ALLOW);
				e.setUseItemInHand(Result.ALLOW);
			} else {
				e.setUseInteractedBlock(Result.DENY);
				e.setUseItemInHand(Result.DENY);
			}
		}
		else if(e.getItem() != null && !e.useInteractedBlock().equals(Result.DENY)) {
			String type = Utils.getType(e.getItem());
			if(type == null || Utils.getCode(e.getItem()) != null)
				return;
			
			safe = SopSafe.manager.createSafe(type, e.getClickedBlock());

			ItemStack item = Utils.setCode(e.getItem(), safe.getCode(), false);
			e.getItem().setAmount(e.getItem().getAmount()-1);
			if(e.getPlayer().getInventory().addItem(item).size() != 0) {
				e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), item);
			}
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void test(InventoryOpenEvent e) {
		InventoryHolder holder = e.getInventory().getHolder();
		if (!(holder instanceof BlockState) || !(holder instanceof Chest))
			return;
		
		Block block = ((BlockState)holder).getBlock();
		
		Safe safe = SopSafe.manager.getSafe(block);
		if(safe != null) {
			if(this.canOpen(safe, (Player)e.getPlayer())) {
				e.setCancelled(false);
			} else {
				e.setCancelled(true);
			}
		}
	}
	
	private boolean canOpen(Safe safe, Player player) {
		if(player.isPermissionSet("sopsafe.admin")) {
			return true;
		}

		String code1 =  Utils.getCode(player.getInventory().getItemInMainHand());
		String code2 =  Utils.getCode(player.getInventory().getItemInOffHand());
		
		if(code1 == null && code2 == null) {
			return false;
		}
		
		if((code1 != null && safe.getCode().equals(code1)) || (code2 != null && safe.getCode().equals(code2)))
			return true;
		
		return false;
	}
}


