package io.github.nhoj1000.stoneholdersbase;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface Power {
    int usePower(Player player);
    ItemStack getTool();
}
