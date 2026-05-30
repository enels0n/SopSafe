package net.enelson.sopsafe.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.enelson.sopsafe.SopSafe;
import net.md_5.bungee.api.ChatColor;

public class MainCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!sender.isOp())
			return false;

		if(args.length == 0) { 
			sender.sendMessage("/sopsafe give <player> <safeName> [amount]");
			return false;
		}
		
		// /sopsafe give <player> <safeName> [amount]
		if(args[0].equals("give")) { 
			if(args.length < 3 || args.length > 4) {
				sender.sendMessage("/sopsafe give <player> <safeName> [amount]");
				return false;
			}
			
			Player player = Bukkit.getPlayerExact(args[1]);
			if(player == null) {
				sender.sendMessage("РРіСЂРѕРє РЅРµ РЅР°Р№РґРµРЅ");
				return false;
			}
			int amount = 1;
			if(args.length == 4) {
				try { amount = Integer.parseInt(args[3]); }
				catch(NumberFormatException ex) { }
			}
			
			ItemStack item = SopSafe.manager.generateKey(args[2], amount);
			if(player.getInventory().addItem(item).size() != 0) {
				player.getWorld().dropItem(player.getLocation(), item);
			}
			
			String message = "&fРРіСЂРѕРєСѓ &a" + player.getDisplayName() + " &fРІС‹РґР°РЅРѕ &e" + amount + " &fx &d" + item.getItemMeta().getDisplayName();
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
			return true;
		}
		return false;
	}

}


