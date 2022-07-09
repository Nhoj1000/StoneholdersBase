package io.github.nhoj1000.stoneholdersbase.powers.reality;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GenerateArrow implements Power {
    private static int maxArrows;

    public GenerateArrow(int maxArrows) {
        GenerateArrow.maxArrows = maxArrows;
    }

    @Override
    public boolean usePower(Player player) {
        ItemStack comparison = GlassBow.getGlassArrow();
        ItemStack arrows = Arrays.stream(player.getInventory().getContents())
                .filter(item -> StoneUtils.comparePowerItems(item, comparison))
                .findFirst().orElse(null);

        if(arrows != null && arrows.getAmount() >= maxArrows) {
            player.sendMessage(ChatColor.RED + "Maximum arrow count reached!");
            return false;
        }

        player.getInventory().addItem(GlassBow.getGlassArrow());
        return true;
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
