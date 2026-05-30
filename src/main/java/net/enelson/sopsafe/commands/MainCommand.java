package net.enelson.sopsafe.commands;

import net.enelson.sopli.lib.text.TextUtils;
import net.enelson.sopsafe.SopSafe;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor {

    private static final TextUtils TEXT_UTILS = new TextUtils();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.isOp()) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage(TEXT_UTILS.color("&e/sopsafe give <player> <safeName> [amount]"));
            return false;
        }

        if (args[0].equals("give")) {
            if (args.length < 3 || args.length > 4) {
                sender.sendMessage(TEXT_UTILS.color("&e/sopsafe give <player> <safeName> [amount]"));
                return false;
            }

            Player player = Bukkit.getPlayerExact(args[1]);
            if (player == null) {
                sender.sendMessage(TEXT_UTILS.color("&cPlayer not found."));
                return false;
            }
            int amount = 1;
            if (args.length == 4) {
                try {
                    amount = Integer.parseInt(args[3]);
                } catch (NumberFormatException ignored) {
                }
            }

            ItemStack item = SopSafe.manager.generateKey(args[2], amount);
            if (player.getInventory().addItem(item).size() != 0) {
                player.getWorld().dropItem(player.getLocation(), item);
            }

            String message = "&fIssued to &a" + player.getDisplayName() + " &f: &e" + amount + " &fx &d" + item.getItemMeta().getDisplayName();
            sender.sendMessage(TEXT_UTILS.color(message));
            return true;
        }
        return false;
    }
}
