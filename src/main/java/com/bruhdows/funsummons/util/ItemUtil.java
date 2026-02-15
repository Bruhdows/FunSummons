package com.bruhdows.funsummons.util;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.AccessoryConfig;
import com.bruhdows.funsummons.model.WandConfig;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    private final FunSummonsPlugin plugin;
    private final NamespacedKey wandKey;
    private final NamespacedKey accessoryKey;
    
    public ItemUtil(FunSummonsPlugin plugin) {
        this.plugin = plugin;
        this.wandKey = new NamespacedKey(plugin, "wand_id");
        this.accessoryKey = new NamespacedKey(plugin, "accessory_id");
    }
    
    public ItemStack createWand(WandConfig config) {
        Material material = Material.valueOf(config.getMaterial());
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(plugin.getMiniMessage().deserialize(noItalic(config.getName())));
        
        List<Component> lore = new ArrayList<>();
        for (String line : config.getLore()) {
            lore.add(plugin.getMiniMessage().deserialize(noItalic(line)));
        }
        meta.lore(lore);
        
        if (config.getCustomModelData() > 0) {
            meta.setCustomModelData(config.getCustomModelData());
        }
        
        meta.getPersistentDataContainer().set(wandKey, PersistentDataType.STRING, config.getId());
        
        item.setItemMeta(meta);
        return item;
    }
    
    public ItemStack createAccessory(AccessoryConfig config) {
        if (config == null) return null;
        
        Material material = Material.valueOf(config.getMaterial());
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        meta.displayName(plugin.getMiniMessage().deserialize(noItalic(config.getName())));

        List<Component> lore = new ArrayList<>();
        for (String line : config.getLore()) {
            lore.add(plugin.getMiniMessage().deserialize(noItalic(line)));
        }
        meta.lore(lore);
        
        if (config.getCustomModelData() > 0) {
            meta.setCustomModelData(config.getCustomModelData());
        }
        
        meta.getPersistentDataContainer().set(accessoryKey, PersistentDataType.STRING, config.getId());
        
        item.setItemMeta(meta);
        return item;
    }
    
    public String getWandId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(wandKey, PersistentDataType.STRING);
    }
    
    public String getAccessoryId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        return item.getItemMeta().getPersistentDataContainer().get(accessoryKey, PersistentDataType.STRING);
    }
    
    public boolean isHoldingSummonerItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        return getWandId(item) != null;
    }

    public static String noItalic(String text) {
        return "<!i>" + text;
    }

    public static List<String> noItalic(List<String> lines) {
        List<String> result = new ArrayList<>();
        for (String line : lines) {
            result.add(noItalic(line));
        }
        return result;
    }
}