package io.github.nhoj1000.stoneholdersbase.powers.reality;

import io.github.nhoj1000.stoneholdersbase.PassivePower;
import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class GlassBow implements Power, PassivePower {
    private static HashMap<UUID, Tagged> targetMap = new HashMap<>();
    private static int decayDelay;

    public GlassBow(int decayDelay) {
        GlassBow.decayDelay = decayDelay;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public int usePower(Player player) {
        Tagged temp = targetMap.get(player.getUniqueId());
        if(temp != null) {
            Entity targetEntity = temp.entity;
            temp.task.cancel();
            targetMap.put(player.getUniqueId(), null);
            generateSphere(targetEntity.getLocation(), 4, Material.GLASS);
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 1);

            if(targetEntity instanceof Arrow)
                targetEntity.remove();
            return 1;
        }
        return -1;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.BOW, 2, "Glass Bow");
    }

    public static void setTarget(Player shooter, Entity target, boolean verbose) {
        if(targetMap.containsKey(shooter.getUniqueId())) {
            Tagged temp = targetMap.get(shooter.getUniqueId());
            if(temp == null)
                temp = new Tagged();
            else
                temp.task.cancel();
            temp.entity = target;
            temp.task = Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(),
                    () -> clearTarget(shooter, true), 20L * decayDelay);
            targetMap.put(shooter.getUniqueId(), temp);

            if(verbose)
                if (target instanceof Player)
                    shooter.sendMessage(ChatColor.DARK_RED + "Tagged " + target.getName());
                else
                    shooter.sendMessage(ChatColor.DARK_RED + "Tagged a " + target.getType());
        }


    }

    public static void clearTarget(Player shooter, boolean verbose) {
        targetMap.put(shooter.getUniqueId(), null);
        if(verbose)
            shooter.sendMessage(ChatColor.DARK_RED + "Tag expired");
    }

    public static void generateSphere(Location loc, int radius, Material material) {
        World world = loc.getWorld();
        int x1 = loc.getBlockX();
        int y1 = loc.getBlockY();
        int z1 = loc.getBlockZ();

        for (int x = x1 - radius; x <= x1 + radius; x++)
            for (int y = y1 - radius; y <= y1 + radius; y++)
                for (int z = z1 - radius; z <= z1 + radius; z++) {
                    int temp = (x1 - x) * (x1 - x) + (y1 - y) * (y1 - y) + (z1 - z) * (z1 - z);
                    if (temp <= radius * radius - 1)
                        if (temp >= radius * (radius - 1.8))
                            new Location(world, x, y, z).getBlock().setType(material);
                }

    }

    @Override
    public void activatePower(Player p) {
        targetMap.put(p.getUniqueId(), null);
    }

    @Override
    public void deactivatePower(Player p) {
        targetMap.remove(p.getUniqueId());
    }

    private static class Tagged {
        public BukkitTask task;
        public Entity entity;
    }

    @Override
    public Set<ItemStack> getItems() {
        return new HashSet<>(Arrays.asList(getTool(), new ItemStack(Material.ARROW, 10)));
    }
}
