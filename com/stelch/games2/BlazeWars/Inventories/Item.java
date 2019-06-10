package com.stelch.games2.BlazeWars.Inventories;

import com.stelch.games2.BlazeWars.Main;
import com.stelch.games2.BlazeWars.Utils.text;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {

    ItemStack is;
    ItemMeta im;

    public Item (Material material, String name) {
        this.is=new ItemStack(material);
        im=this.is.getItemMeta();
        im.setUnbreakable(false);
        im.setDisplayName(text.f(name));
    }

    public void setOnClick(click onClick){
        Main.Actions.put(this.is,onClick);
    }

    public ItemStack spigot() {
        this.is.setItemMeta(this.im);
        return this.is;
    }



    public static interface click {
        void run(Player param1Player);
    }

}
