package me.ori.main.models;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class ItemManager {

    public static ItemStack rocketsCrossbow;



    public static void init(){
        createCrossbow();

    }


    public static void createCrossbow(){
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        ItemMeta meta = crossbow.getItemMeta();
        meta.setLore(Arrays.asList(ChatColor.GRAY + "This crossbow shoots rockets", ChatColor.GRAY + "with 3 seconds cooldown!"));
        meta.addEnchant(Enchantment.QUICK_CHARGE,3,false);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        crossbow.setItemMeta(meta);
        rocketsCrossbow = crossbow;

    }
}
