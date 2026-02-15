package com.bruhdows.funsummons.gui;

import com.bruhdows.funsummons.FunSummonsPlugin;
import com.bruhdows.funsummons.model.PlayerData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MenuType;

public class AccessoryGUI implements Listener {
    private final FunSummonsPlugin plugin;
    private final Player player;
    private InventoryView view;

    public AccessoryGUI(FunSummonsPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings("UnstableApiUsage")
    public void open() {
        view = MenuType.HOPPER.builder()
                .title(plugin.getMiniMessage().deserialize("<blue>Accessories</blue>"))
                .build(player);

        loadAccessories();
        view.open();
    }

    private void loadAccessories() {
        PlayerData data = plugin.getManaManager().getPlayerData(player);
        for (int slot = 0; slot < 5; slot++) {
            String accId = data.getAccessories().get(slot);
            if (accId != null) {
                ItemStack item = plugin.getItemUtil().createAccessory(plugin.getConfigManager().getAccessory(accId));
                if (item != null) {
                    view.getTopInventory().setItem(slot, item);
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().equals(view)) return;
        if (!event.getWhoClicked().equals(player)) return;

        event.setCancelled(true);

        int slot = event.getRawSlot();
        ItemStack clicked = event.getCurrentItem();

        if (slot >= 0 && slot < 5) {
            if (clicked != null && clicked.getType() != Material.AIR) {
                view.getTopInventory().setItem(slot, null);
                player.getInventory().addItem(clicked);
                player.sendMessage(plugin.getMiniMessage().deserialize("<yellow>Removed accessory!</yellow>"));
            }
        }
        else if (slot >= 5) {
            if (clicked != null && clicked.getType() != Material.AIR) {
                String accId = plugin.getItemUtil().getAccessoryId(clicked);

                if (accId != null) {
                    int emptySlot = -1;
                    for (int i = 0; i < 5; i++) {
                        ItemStack slotItem = view.getTopInventory().getItem(i);
                        if (slotItem == null || slotItem.getType() == Material.AIR) {
                            emptySlot = i;
                            break;
                        }
                    }

                    if (emptySlot != -1) {
                        ItemStack singleItem = clicked.clone();
                        singleItem.setAmount(1);
                        view.getTopInventory().setItem(emptySlot, singleItem);

                        clicked.setAmount(clicked.getAmount() - 1);
                        if (clicked.getAmount() <= 0) {
                            Inventory clickedInventory = event.getClickedInventory();
                            if (clickedInventory != null) clickedInventory.setItem(event.getSlot(), null);
                        }

                        player.sendMessage(plugin.getMiniMessage().deserialize("<green>Equipped accessory!</green>"));
                    } else {
                        player.sendMessage(plugin.getMiniMessage().deserialize("<red>No empty accessory slots!</red>"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getView().equals(view)) return;
        if (!event.getPlayer().equals(player)) return;

        PlayerData data = plugin.getManaManager().getPlayerData(player);
        data.getAccessories().clear();

        for (int slot = 0; slot < 5; slot++) {
            ItemStack item = view.getTopInventory().getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                String accId = plugin.getItemUtil().getAccessoryId(item);
                if (accId != null) {
                    data.getAccessories().put(slot, accId);
                }
            }
        }

        plugin.getAccessoryManager().recalculateStats(player);
        plugin.getManaManager().savePlayerData(player.getUniqueId());

        view = null;
    }
}