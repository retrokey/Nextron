package tk.pandadev.essentialsp.guis;

import games.negative.framework.gui.GUI;
import games.negative.framework.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import tk.pandadev.essentialsp.Main;
import tk.pandadev.essentialsp.utils.Configs;
import tk.pandadev.essentialsp.utils.LanguageLoader;
import tk.pandadev.essentialsp.utils.Utils;

import java.util.Objects;

public class HomeSettingsGui extends GUI {

    public HomeSettingsGui(String home) {
        super(home, 3);

        setItemClickEvent(12, player -> new ItemBuilder(Material.ENDER_PEARL).setName("§x§0§1§5§9§5§6Teleport").build(), ((player, event) -> {
            player.teleport((Location) Objects.requireNonNull(Configs.home.get("Homes." + player.getUniqueId() + "." + home)));
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
            player.closeInventory();
        }));
        setItemClickEvent(13, player -> new ItemBuilder(Material.YELLOW_DYE).setName("§eRename").build(), ((player, event) -> {
            player.closeInventory();
            player.sendMessage(Main.getPrefix() + LanguageLoader.translationMap.get("homesettingsgui_feature"));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            new HomeSettingsGui(home).open(player);
        }));
        setItemClickEvent(14, player -> new ItemBuilder(Material.RED_DYE).setName("§cDelete").build(), ((player, event) -> {
            Configs.home.set("Homes." + player.getUniqueId() + "." + home, null);
            Main.getInstance().saveConfig();
            if (Configs.home.getConfigurationSection("Homes." + player.getUniqueId()).getKeys(false).isEmpty()) {
                player.closeInventory();
            } else {
                new HomeGui(player).open(player);
            }
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        }));

        setItemClickEvent(18, player1 -> new ItemBuilder(Utils.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ==")).setName("§fBack").build(), (player1, event) -> {
            new HomeGui(player1).open(player1);
        });
    }
}
