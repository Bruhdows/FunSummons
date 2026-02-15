package com.bruhdows.funsummons.command;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.AccessoryConfig;
import com.bruhdows.funsummons.model.WandConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetItemCommand implements CommandExecutor, TabCompleter {
    private final FunSummonsPlugin plugin;
    
    public GetItemCommand(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(plugin.getMiniMessage().deserialize(
                "<red>Usage: /getitem <wand|accessory> <id></red>"
            ));
            return true;
        }
        
        String type = args[0].toLowerCase();
        String id = args[1];
        
        ItemStack item;
        
        if (type.equals("wand")) {
            WandConfig config = plugin.getConfigManager().getWand(id);
            if (config == null) {
                player.sendMessage(plugin.getMiniMessage().deserialize(
                    "<red>Wand not found: " + id + "</red>"
                ));
                return true;
            }
            item = plugin.getItemUtil().createWand(config);
            
        } else if (type.equals("accessory")) {
            AccessoryConfig config = plugin.getConfigManager().getAccessory(id);
            if (config == null) {
                player.sendMessage(plugin.getMiniMessage().deserialize(
                    "<red>Accessory not found: " + id + "</red>"
                ));
                return true;
            }
            item = plugin.getItemUtil().createAccessory(config);
            
        } else {
            player.sendMessage(plugin.getMiniMessage().deserialize(
                "<red>Invalid type! Use 'wand' or 'accessory'</red>"
            ));
            return true;
        }
        
        if (item != null) {
            player.getInventory().addItem(item);
            player.sendMessage(plugin.getMiniMessage().deserialize(
                "<green>Gave you the item!</green>"
            ));
        }
        
        return true;
    }
    
    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("wand", "accessory");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("wand")) {
                return new ArrayList<>(plugin.getConfigManager().getAllWands().keySet());
            } else if (args[0].equalsIgnoreCase("accessory")) {
                return new ArrayList<>(plugin.getConfigManager().getAllAccessories().keySet());
            }
        }
        return new ArrayList<>();
    }
}