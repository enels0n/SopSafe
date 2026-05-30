package net.enelson.sopsafe.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import net.enelson.sopsafe.SopSafe;
import net.enelson.sopsafe.utils.Utils;

public class DublicateCreateHandler implements Listener {
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e) {
		if(e.getRecipe() == null || !(e.getRecipe() instanceof ShapedRecipe))
			return;
		
		ShapedRecipe src = (ShapedRecipe) e.getRecipe();
		if(!src.getKey().equals(SopSafe.manager.getDublicateRecipe().getKey()))
			return;

		ItemStack[] items = new ItemStack[2];
		int i = 0;
		for (ItemStack item : e.getInventory()) {
			if(i==0) {
				i++;
				continue;
			}
			if (item != null && !item.getType().equals(Material.AIR)) {
				if(i>2) {
					e.getInventory().setResult(null);
					return;
				}
				if(Utils.getType(item) != null) {
					items[i-1] = item;
					i++;
					continue;
				}
				e.getInventory().setResult(null);
				i++;
			}
		}

		ItemStack original = null;
		ItemStack template = null;

		if(Utils.getCode(items[0]) != null && !Utils.isDublicate(items[0]) && Utils.getCode(items[1]) == null) {
			original = items[0];
			template = items[1];
		}
		else if(Utils.getCode(items[1]) != null && !Utils.isDublicate(items[1]) && Utils.getCode(items[0]) == null) {
			original = items[1];
			template = items[0];
		}
		else {
			e.getInventory().setResult(null);
			return;
		}

		String type = Utils.getType(original);
		if(!type.equals(Utils.getType(template))) {
			e.getInventory().setResult(null);
			return;
		}

		e.getInventory().setResult(SopSafe.manager.generateDublicate(type, Utils.getCode(original)));
	}

	@EventHandler
	public void onCraft(CraftItemEvent e) {
		if(e.getRecipe() == null || !(e.getRecipe() instanceof ShapedRecipe))
			return;
		
		ShapedRecipe src = (ShapedRecipe) e.getRecipe();
		if(!src.getKey().equals(SopSafe.manager.getDublicateRecipe().getKey()))
			return;
		
		if(e.isShiftClick()) {
			e.setCancelled(true);
			return;
		}
		
		for(ItemStack item : e.getInventory()) {
			if(Utils.getCode(item) != null && !Utils.isDublicate(item)) {
				item.setAmount(item.getAmount()+1);
				break;
			}
		}
	}
}


