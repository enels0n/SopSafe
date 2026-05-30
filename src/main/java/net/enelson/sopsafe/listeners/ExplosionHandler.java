package net.enelson.sopsafe.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import net.enelson.sopsafe.SopSafe;
import net.enelson.sopsafe.safes.Safe;


public class ExplosionHandler implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExploison(EntityExplodeEvent e) {
		if(e.isCancelled())
			return;
		
		List<Block> restore = new ArrayList<>();
		
		for(Block block : e.blockList()) {
			if(!(block.getState() instanceof Chest))
				continue;
			
			Safe safe = SopSafe.manager.getSafe(block);
			if(safe != null) {
				safe.removeDurability(1);
				restore.add(block);
                block.getState().update(true);
			}
		}
		
		e.blockList().removeAll(restore);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onExploison(BlockExplodeEvent e) {
		if(e.isCancelled())
			return;
		
		for(Block block : e.blockList()) {
			if(!(block.getState() instanceof Chest))
				continue;
			
			Safe safe = SopSafe.manager.getSafe(block);
			if(safe != null) {
				safe.removeDurability(1);
                e.blockList().remove(block); // РЈРґР°Р»СЏРµРј Р±Р»РѕРє РёР· СЃРїРёСЃРєР° СЂР°Р·СЂСѓС€РµРЅРЅС‹С… Р±Р»РѕРєРѕРІ
                block.getState().update(true); // Р’РѕСЃСЃРѕР·РґР°РµРј Р±Р»РѕРє
			}
		}
	}
}


