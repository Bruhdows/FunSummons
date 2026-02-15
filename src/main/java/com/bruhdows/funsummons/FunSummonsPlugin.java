package com.bruhdows.funsummons;

import com.bruhdows.funsummons.command.AccessoriesCommand;
import com.bruhdows.funsummons.command.GetItemCommand;
import com.bruhdows.funsummons.listener.PlayerListener;
import com.bruhdows.funsummons.listener.SummonListener;
import com.bruhdows.funsummons.manager.AccessoryManager;
import com.bruhdows.funsummons.manager.ConfigManager;
import com.bruhdows.funsummons.manager.ManaManager;
import com.bruhdows.funsummons.manager.SummonManager;
import com.bruhdows.funsummons.util.ItemUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

@Getter
public class FunSummonsPlugin extends JavaPlugin {
    
    private Gson gson;
    private MiniMessage miniMessage;
    private ItemUtil itemUtil;

    private ConfigManager configManager;
    private ManaManager manaManager;
    private SummonManager summonManager;
    private AccessoryManager accessoryManager;
    
    @Override
    public void onEnable() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        this.miniMessage = MiniMessage.builder().build();
        this.itemUtil = new ItemUtil(this);
        
        this.configManager = new ConfigManager(this);
        this.manaManager = new ManaManager(this);
        this.summonManager = new SummonManager(this);
        this.accessoryManager = new AccessoryManager(this);
        
        configManager.loadAll();
        
        Objects.requireNonNull(getCommand("getitem")).setExecutor(new GetItemCommand(this));
        Objects.requireNonNull(getCommand("accessories")).setExecutor(new AccessoriesCommand(this));
        
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new SummonListener(this), this);
    }
    
    @Override
    public void onDisable() {
        summonManager.removeAllSummons();
        manaManager.clearAll();
    }
}