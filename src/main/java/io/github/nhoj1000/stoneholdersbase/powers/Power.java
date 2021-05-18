package io.github.nhoj1000.stoneholdersbase.powers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Power {
    int usePower(Player player);
    ItemStack getTool();
    int getManaCost();
}
