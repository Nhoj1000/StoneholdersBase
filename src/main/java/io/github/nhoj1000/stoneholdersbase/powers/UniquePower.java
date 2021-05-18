package io.github.nhoj1000.stoneholdersbase.powers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public interface UniquePower {
    int usePower(Player player);
    int getManaCost();
    ItemStack getActivationItem();
    Set<ItemStack> getItems();
}
