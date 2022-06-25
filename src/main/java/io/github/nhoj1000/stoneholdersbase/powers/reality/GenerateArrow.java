package io.github.nhoj1000.stoneholdersbase.powers.reality;

import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GenerateArrow implements Power {
    private static int maxArrows;

    public GenerateArrow(int maxArrows) {
        GenerateArrow.maxArrows = maxArrows;
    }

    @Override
    public boolean usePower(Player player) {
        ItemStack arrows = null;
        ItemStack comparison = GlassBow.getGlassArrow();
        for(ItemStack item: player.getInventory().getContents()) {
            if (StoneholdersBase.comparePowerItems(item, comparison)) {
                arrows = item;
            }
        }
        if (arrows == null || arrows.getAmount() < maxArrows) {
            player.getInventory().addItem(GlassBow.getGlassArrow());
            return true;
        }
        player.sendMessage(ChatColor.RED + "Maximum arrow count reached!");
        return false;
    }

    @Override
    public ItemStack getTool() {
        return GlassBow.getGlassArrow();
    }

    @Override
    public int getManaCost() {
        return 20;
    }

    public static int getMaxArrows() {
        return maxArrows;
    }
}
