package me.ori.main.models;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SelectInventory {
    public static Inventory createInventory(){
        Inventory inv = Bukkit.createInventory(null,45, ChatColor.GOLD + "Select Team");
        ItemStack[] defaultSlots = new ItemStack[45];
        for(int i = 0; i < 45; i++){
            defaultSlots[i] = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        }
        inv.setContents(defaultSlots);
        inv.setItem(20,new ItemStack(Material.BLUE_WOOL));
        inv.setItem(24,new ItemStack(Material.RED_WOOL));

        return inv;


    }
}
