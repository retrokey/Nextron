package tk.pandadev.nextron.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import tk.pandadev.nextron.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EnderchestCommand implements CommandExecutor, TabCompleter {

    public static ArrayList<UUID> enderchest = new ArrayList();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {sender.sendMessage(Main.getCommandInstance()); return false;}

        Player player = (Player) (sender);

        if (args.length == 0) {

            player.openInventory(player.getEnderChest());

        } else if (args.length == 1) {
            if (!player.hasPermission("nextron.enderchest.other")){ player.sendMessage(Main.getNoPerm()); return false;}
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) { player.sendMessage(Main.getInvalidPlayer()); return false;}

            player.openInventory(target.getEnderChest());
            enderchest.contains(player.getUniqueId());
        } else {
            player.sendMessage(Main.getPrefix() + "§c/enderchest <player>");
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        Player playert = (Player)(sender);

        if (args.length == 1 && playert.hasPermission("nextron.enderchest.other")){
            for (Player player : Bukkit.getOnlinePlayers()) {
                list.add(player.getName());
            }
        }

        return list;
    }
}
