package tk.pandadev.essentialsp.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import tk.pandadev.essentialsp.Main;
import tk.pandadev.essentialsp.guis.featuretoggle.FeatureGui;
import tk.pandadev.essentialsp.utils.Configs;
import tk.pandadev.essentialsp.utils.LanguageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FeatureCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.getCommandInstance());
            return false;
        }

        Player player = (Player) (sender);

        List<String> validValues = Arrays.asList("warp_system", "home_system", "rank_system", "tpa_system");

        if (args.length == 0){
            if (!player.isOp()) {player.sendMessage(Main.getNoPerm()); return false;}
            new FeatureGui().open(player);
            player.playSound(player.getLocation(), Sound.BLOCK_BARREL_OPEN, 100, 1);
        } else if (args.length == 2){
            if (!validValues.contains(args[1])) {player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("feature_validvalues")); return false;}
            if (args[0].equalsIgnoreCase("enable")){
                Configs.feature.set(args[1], true);
                Configs.saveFeatureConfig();
                if (Objects.equals(args[1].replace("_system", ""), "tpa")) {player.addAttachment(Main.getInstance()).setPermission("essentialsp.tpaccept", Configs.feature.getBoolean(args[1]));}
                player.addAttachment(Main.getInstance()).setPermission("essentialsp." + args[1].replace("_system", ""), Configs.feature.getBoolean(args[1]));
                player.playSound(player.getLocation(), Configs.feature.getBoolean("warp_system") ? Sound.BLOCK_BEACON_DEACTIVATE : Sound.BLOCK_BEACON_ACTIVATE, 100, 1);
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("feature_enable").replace("%n", args[1].replace("_system", "")));
            }
            if (args[0].equalsIgnoreCase("disable")){
                Configs.feature.set(args[1], false);
                Configs.saveFeatureConfig();
                if (Objects.equals(args[1].replace("_system", ""), "tpa")) {player.addAttachment(Main.getInstance()).setPermission("essentialsp.tpaccept", Configs.feature.getBoolean(args[1]));}
                player.addAttachment(Main.getInstance()).setPermission("essentialsp." + args[1].replace("_system", ""), Configs.feature.getBoolean(args[1]));
                player.playSound(player.getLocation(), Configs.feature.getBoolean(args[1]) ? Sound.BLOCK_BEACON_DEACTIVATE : Sound.BLOCK_BEACON_ACTIVATE, 100, 1);
                player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("feature_disable").replace("%n", args[1].replace("_system", "")));
            }
        } else {
            player.sendMessage(Main.getPrefix() + "§c/features"); return false;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player playert = (Player) (sender);
        ArrayList<String> list = new ArrayList<String>();

        if (args.length == 1){
            list.add("enable");
            list.add("disable");
        } else if (args.length == 2){
            list.add("warp_system");
            list.add("home_system");
            list.add("rank_system");
            list.add("tpa_system");
        }

        return list;
    }

}