package io.github.nhoj1000.stoneholdersbase.powers.time;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;

public class Checkpoint implements Power {
    private  int recallTime;
    private StoneholdersBase plugin;

    public Checkpoint(StoneholdersBase plugin, int recallTime) {
        this.recallTime = recallTime;
        this.plugin = plugin;
    }

    @Override
    public int usePower(Player player) {
        Location loc = player.getLocation();
        double health = player.getHealth();
        int food = player.getFoodLevel();
        int airTime = player.getRemainingAir();
        Collection<PotionEffect> potions = player.getActivePotionEffects();

        player.sendMessage(ChatColor.GREEN + "Checkpoint set. Returning in " + recallTime + " seconds");
        player.addAttachment(plugin).setPermission("stoneholders.immortal", true);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.teleport(loc);
            player.setGameMode(GameMode.SURVIVAL);
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1, 255, false, false));
            player.setHealth(health);
            player.setFoodLevel(food);
            player.setRemainingAir(airTime);
            player.sendMessage(ChatColor.GREEN + "Returned to checkpoint!");
            player.addAttachment(plugin).setPermission("stoneholders.immortal", false); //TODO more nasty perms to get rid of
            player.addAttachment(plugin).setPermission("stoneholders.frozen", false);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setFireTicks(0);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                for (PotionEffect effect : potions)
                    player.addPotionEffect(effect);
            }, 1L);
        }, recallTime * 20);

        return 1;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.DIAMOND_SHOVEL, 5, "Checkpoint");
    }
}
