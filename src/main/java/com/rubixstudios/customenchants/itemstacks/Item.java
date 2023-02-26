package com.rubixstudios.customenchants.itemstacks;

import org.bukkit.inventory.ItemStack;

public abstract class Item {
    private final String name;
    public abstract ItemStack getItemStack();

    public Item(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

