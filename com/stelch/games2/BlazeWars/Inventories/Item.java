package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Item {

    ItemStack is;
    ItemMeta im;

    public Item (Material material, String name) {
        this.is=new ItemStack(material);
        im=this.is.getItemMeta();
        im.setUnbreakable(false);
        im.setDisplayName(text.f(name));
        im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    }

    public void setAmount(int amount){
        is.setAmount(amount);
    }

    public void setEnchanted(boolean enchanted) { im.addEnchant(Enchantment.ARROW_DAMAGE,1,true);im.addItemFlags(ItemFlag.HIDE_ENCHANTS); }

    public void setLore (String[] lines){
        ArrayList<String> lines2 = new ArrayList<>();
        for(String line : lines){
            lines2.add(text.f(line));
        }
        im.setLore(lines2);
    }

    public Item setOnClick(click onClick){
        if(onClick!=null)
            Main.Actions.put(this.is,onClick);
        return this;
    }

    public ItemStack spigot() {
        this.is.setItemMeta(this.im);
        return this.is;
    }



    public static interface click {
        void run(Player param1Player);
    }

}
