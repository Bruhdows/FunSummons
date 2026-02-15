package com.bruhdows.funsummons.command;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.gui.AccessoryGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AccessoriesCommand implements CommandExecutor {
    private final FunSummonsPlugin plugin;
    
    public AccessoriesCommand(FunSummonsPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this com.bruhdows.funsummons.command!");
            return true;
        }
        
        Player player = (Player) sender;
        new AccessoryGUI(plugin, player).open();
        
        return true;
    }
}