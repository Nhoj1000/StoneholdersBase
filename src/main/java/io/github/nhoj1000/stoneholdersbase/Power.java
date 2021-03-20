package io.github.nhoj1000.stoneholdersbase;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public interface Power {
    int cooldown = 0;
    Material tool = Material.STICK;
    
    void usePower(Player player);
    int getCooldown();
    int getTool();
}
