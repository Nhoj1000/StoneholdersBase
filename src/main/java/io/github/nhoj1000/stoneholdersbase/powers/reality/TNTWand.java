package io.github.nhoj1000.stoneholdersbase.powers.reality;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.inventory.ItemStack;

public class TNTWand implements Power {
    private static int range, yield;

    public TNTWand(int range, int yield) {
        this.range = range; //30
        this.yield = yield; //15
    }

    @Override
    public int usePower(Player player) {
        Block block = player.getTargetBlockExact(range);
        if(block == null) {
            player.sendMessage(ChatColor.RED + "Block out of range!");
            return 0;   //unsuccessful cooldown time
        } else {
            block.setType(Material.AIR);
            TNTPrimed tnt = (TNTPrimed) player.getWorld().spawnEntity(block.getLocation().add(0.5, 0, 0.5), EntityType.PRIMED_TNT);
            tnt.setFuseTicks(100);
            tnt.setYield(yield);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 2, 1);
            return 1;   //Successful cooldown time
        }
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.DIAMOND_SHOVEL, 2, "TNT Wand");
    }
}
