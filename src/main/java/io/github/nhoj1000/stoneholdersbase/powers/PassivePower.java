package io.github.nhoj1000.stoneholdersbase.powers;

import org.bukkit.entity.Player;

public interface PassivePower {
    void activatePower(Player p);
    void deactivatePower(Player p);
}
