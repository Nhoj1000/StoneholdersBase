package io.github.nhoj1000.stoneholdersbase.powers.reality;

import io.github.nhoj1000.stoneholdersbase.*;
import io.github.nhoj1000.stoneholdersbase.powers.UniquePower;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GlassBow implements UniquePower {
    private static final HashMap<UUID, Tagged> targetMap = new HashMap<>();
    private static int decayDelay;

    private static class Tagged {
        public BukkitTask task;
        public Entity entity;
    }

    public GlassBow(int decayDelay) {
        GlassBow.decayDelay = decayDelay;
    }

    @Override
    public boolean usePower(Player player) {
        Tagged temp = targetMap.get(player.getUniqueId());
        if(temp != null) {
            Entity targetEntity = temp.entity;
            temp.task.cancel();
            targetMap.remove(player.getUniqueId());
            generateSphere(targetEntity.getLocation(), 4, Material.GLASS);
            targetEntity.getWorld().playSound(targetEntity.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 1);

            if(targetEntity instanceof Arrow) {
                targetEntity.remove();
            }
            return true;
        }
        return false;
    }

    public static void setTarget(Player shooter, Entity target, boolean verbose) {
        Tagged temp = targetMap.get(shooter.getUniqueId());
        if(temp == null) {
            temp = new Tagged();
        } else {
            temp.task.cancel();
        }
        temp.entity = target;
        temp.task = Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(),
                () -> clearTarget(shooter, true), 20L * decayDelay);
        targetMap.put(shooter.getUniqueId(), temp);

        if(verbose) {
            shooter.sendMessage(ChatColor.DARK_RED + "Tagged " +
                    ((target instanceof Player)
                            ? target.getName()
                            : target.getType()));
        }
    }

    public static void clearTarget(Player shooter, boolean verbose) {
        targetMap.put(shooter.getUniqueId(), null);
        if(verbose) {
            shooter.sendMessage(ChatColor.DARK_RED + "Tag expired");
        }
    }

    public static void generateSphere(Location loc, int radius, Material material) {
        World world = loc.getWorld();
        int x1 = loc.getBlockX();
        int y1 = loc.getBlockY();
        int z1 = loc.getBlockZ();

        for (int x = x1 - radius; x <= x1 + radius; x++) {
            for (int y = y1 - radius; y <= y1 + radius; y++) {
                for (int z = z1 - radius; z <= z1 + radius; z++) {
                    int temp = (x1 - x) * (x1 - x) + (y1 - y) * (y1 - y) + (z1 - z) * (z1 - z);
                    if (temp <= radius * radius - 1) {
                        if (temp >= radius * (radius - 1.8)) {
                            new Location(world, x, y, z).getBlock().setType(material);
                        }
                    }
                }
            }
        }
    }


    @Override
    public Set<ItemStack> getItems() {
        Set<ItemStack> items = new HashSet<>();
        items.add(getActivationItem());
        ItemStack glassArrows = getGlassArrow();
        items.add(glassArrows);
        return items;
    }

    @Override
    public int getManaCost() {
        return 30;
    }

    @Override
    public ItemStack getActivationItem() {
        return getGlassBow();
    }

    public static ItemStack getGlassBow() {
        return StoneUtils.generateStoneTool(Material.BOW, 2, "Glass Bow", Collections.singletonList(""));
    }

    public static ItemStack getGlassArrow() {
        ItemStack glassArrow = new ItemStack(Material.TIPPED_ARROW);
        PotionMeta glassMeta = (PotionMeta) glassArrow.getItemMeta();
        glassMeta.addCustomEffect(new PotionEffect(PotionEffectType.UNLUCK, 1, 0, false, false), true);
        glassMeta.setDisplayName("Glass Arrow");
        glassMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        glassArrow.setItemMeta(glassMeta);

        return glassArrow;
    }
}
