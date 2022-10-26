package tk.pandadev.essentialsp.guis.mainextend;

import games.negative.framework.gui.GUI;
import games.negative.framework.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tk.pandadev.essentialsp.guis.MainGui;
import tk.pandadev.essentialsp.utils.Configs;
import tk.pandadev.essentialsp.utils.LanguageLoader;
import tk.pandadev.essentialsp.utils.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WarpGui extends GUI {

    public WarpGui(){
        super("Warps", 5);

        for (String warp : Configs.warp.getConfigurationSection("Warps").getKeys(false)){
            addItemClickEvent(player1 -> new ItemBuilder(Material.NETHER_STAR)
                    .setName(warp)
                    .addLoreLine("")
                    .addLoreLine(LanguageLoader.translationMap.get("warpgui_leftclick"))
                    .addLoreLine(LanguageLoader.translationMap.get("warpgui_rightclick"))
                    .build(), ((player1, event) -> {
                if (event.getClick().isRightClick()) {
                    player1.teleport((Location) Objects.requireNonNull(Configs.warp.get("Warps." + warp)));
                    player1.playSound(player1.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    player1.closeInventory();
                } else if (event.getClick().isLeftClick()){
                    new WarpSettginsGui(warp).open(player1);
                }
            }));
        }

        setItemClickEvent(36, player -> new ItemBuilder(Utils.createSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmQ2OWUwNmU1ZGFkZmQ4NGU1ZjNkMWMyMTA2M2YyNTUzYjJmYTk0NWVlMWQ0ZDcxNTJmZGM1NDI1YmMxMmE5In19fQ=="))
                .setName("§fBack")
                .build(), (player, event) -> {
            new MainGui(player).open(player);
        });
    }

}
