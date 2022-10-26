package tk.pandadev.essentialsp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import tk.pandadev.essentialsp.Main;
import tk.pandadev.essentialsp.utils.Configs;
import tk.pandadev.essentialsp.utils.LanguageLoader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HomeCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getCommandInstance());
            return false;
        }

        Player player = (Player) (sender);

        if (label.equalsIgnoreCase("sethome") && args.length == 1){

            if (Configs.home.getString("Homes." + player.getUniqueId() + "." + args[0].toLowerCase()) == null){
                Configs.home.set("Homes." + player.getUniqueId() + "." + args[0].toLowerCase(), player.getLocation());
                Configs.saveHomeConfig();
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("sethome_success").replace("%h", args[0].toLowerCase()));
            } else {
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("sethome_error").replace("%h", args[0].toLowerCase()));
            }

        } else if (label.equalsIgnoreCase("sethome") && args.length == 0){

            Configs.home.set("Homes." + player.getUniqueId() + ".default", player.getLocation());
            Configs.saveHomeConfig();
            player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("sethome_default_success"));

        } else if (label.equalsIgnoreCase("delhome") && args.length == 1){

            if (Configs.home.getString("Homes." + player.getUniqueId() + "." + args[0].toLowerCase()) != null){
                Configs.home.set("Homes." + player.getUniqueId() + "." + args[0].toLowerCase(), null);
                Configs.saveHomeConfig();
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("delhome_success").replace("%h", args[0].toLowerCase()));
            } else {
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("delhome_error").replace("%h", args[0].toLowerCase()));
            }

        } else if (label.equalsIgnoreCase("home") && args.length == 1){

            if (Configs.home.getString("Homes." + player.getUniqueId() + "." + args[0].toLowerCase()) != null){
                player.teleport((Location) Configs.home.get("Homes." + player.getUniqueId() + "." + args[0].toLowerCase()));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("home_success").replace("%h", args[0].toLowerCase()));
            } else {
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("home_error").replace("%h", args[0].toLowerCase()));
            }

        } else if (label.equalsIgnoreCase("home") && args.length == 0){

            if (Configs.home.getString("Homes." + player.getUniqueId() + ".default") != null){
                player.teleport((Location) Configs.home.get("Homes." + player.getUniqueId() + ".default"));
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 100, 1);
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("home_default_success"));
            } else {
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("home_default_error"));
            }

        } else {
            player.sendMessage(Main.getPrefix() + "§c/home|sethome|delhome <name>");
        }


        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> list = new ArrayList<String>();
        Player playert = (Player)(sender);

        if (Configs.home.getConfigurationSection("Homes") == null || Configs.home.getConfigurationSection("Homes").getKeys(false).isEmpty()){
            System.out.println("empty in homes");
        } else if (args.length == 1 && label.equalsIgnoreCase("home") || label.equalsIgnoreCase("delhome")) {
            list.addAll(Objects.requireNonNull(Configs.home.getConfigurationSection("Homes." + playert.getUniqueId())).getKeys(false));
        }

        ArrayList<String> completerList = new ArrayList<String>();
        String currentarg = args[args.length - 1].toLowerCase();
        for (String s : list) {
            String s1 = s.toLowerCase();
            if (!s1.startsWith(currentarg)) continue;
            completerList.add(s);
        }

        return completerList;
    }
}
