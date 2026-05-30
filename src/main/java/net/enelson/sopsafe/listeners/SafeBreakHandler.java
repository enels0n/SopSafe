package net.enelson.sopsafe.listeners;

import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.EntityEquipment;

import net.enelson.sopsafe.SopSafe;
import net.enelson.sopsafe.safes.Safe;
import net.enelson.sopsafe.utils.Utils;

public class SafeBreakHandler implements Listener {

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if(e.isCancelled())
			return;
		
		if(!(e.getBlock().getState() instanceof Chest))
			return;
		
		Safe safe = SopSafe.manager.getSafe(e.getBlock());
		
		if(safe == null)
			return;
		
		EntityEquipment equipment = e.getPlayer().getEquipment();
		
		if((equipment.getItemInMainHand() != null && safe.getCode().equals(Utils.getCode(equipment.getItemInMainHand())))
			|| (equipment.getItemInOffHand() != null && safe.getCode().equals(Utils.getCode(equipment.getItemInOffHand())))) {
			SopSafe.manager.removeSafe(safe);
			return;
		}
		
		e.setCancelled(true);
	}
}


