package io.github.nhoj1000.stoneholdersbase.powers.time;

import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

public class Pause implements Power {
    private static final Set<Entity> frozenEntities = new HashSet<>();

    private final int radius, time;

    public Pause(int radius, int time) {
        this.radius = radius;
        this.time = time;
    }

    @Override
    public boolean usePower(Player player) {
        player.sendMessage(ChatColor.GREEN + "Time Pause activated for " + time + " seconds!");
        List<Entity> entities = player.getNearbyEntities(radius, radius, radius);
        Map<Entity, Vector> speeds = new HashMap<>();

        for (Entity e : entities) {
            if (e instanceof LivingEntity)
                ((LivingEntity) e).setAI(false);
            if(e instanceof Player)
                ((Player) e).setAllowFlight(true);
            frozenEntities.add(e);
            speeds.put(e, e.getVelocity());
            e.setVelocity(new Vector(0, 0, 0));
            e.setGravity(false);
        }

        Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(), () -> {
            for (Entity e : entities) {
                e.setGravity(true);
                if (e instanceof LivingEntity)
                    ((LivingEntity) e).setAI(true);
                if(e instanceof Player)
                    ((Player) e).setAllowFlight(((Player) e).getGameMode() != GameMode.SURVIVAL);
                frozenEntities.remove(e);
                e.setVelocity(speeds.remove(e));
            }
            player.sendMessage(ChatColor.GREEN + "Time Pause deactivated!");
        }, time * 20L);
        return true;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.GOLDEN_SHOVEL, 5, "Pause", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 65;
    }

    public static void setFrozen(Entity e, boolean frozen) {
        if(frozen)
            frozenEntities.add(e);
        else
            frozenEntities.remove(e);
    }

    public static boolean isFrozen(Entity e) {
        return frozenEntities.contains(e);
    }
}
