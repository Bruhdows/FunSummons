package com.bruhdows.funsummons.command;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.gui.AccessoryGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class AccessoriesCommand implements CommandExecutor {
    private final FunSummonsPlugin plugin;
    
    public AccessoriesCommand(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        new AccessoryGUI(plugin, player).open();
        
        return true;
    }
}