package io.github.nhoj1000.stoneholdersbase.powers.reality;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class Disguise implements Power {
    private static int range, maxTime;
    private static Map<UUID, DisguisedUser> disguisedUsers = new HashMap<>();

    public Disguise(int range, int maxTime) {
        Disguise.range = range;
        Disguise.maxTime = maxTime;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Override
    public int usePower(Player player) {
        if(!disguisedUsers.containsKey(player.getUniqueId())) {
            Block target = player.getTargetBlockExact(range);
            if(target == null) {
                player.sendMessage("Block out of range!");
                return -1;
            }
            player.setGameMode(GameMode.SPECTATOR);
            player.setFlySpeed(0);
            player.setVelocity(new Vector());
            player.getWorld().playSound(player.getLocation(), Sound.UI_TOAST_OUT, 10, 1.2f);

            DisguisedUser user = new DisguisedUser();
            user.block = player.getWorld().getBlockAt(player.getLocation());
            user.material = user.block.getType();
            user.block.setType(target.getType());
            player.teleport(user.block.getLocation().add(0.5, 0.5, 0.5)
                    .setDirection(player.getLocation().getDirection()));
            user.task = Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(),
                    () -> unDisguise(player), maxTime * 20L);
            disguisedUsers.put(player.getUniqueId(), user);
        } else {
            unDisguise(player);
        }
        return -1;
    }

    private void unDisguise(Player player) {
        DisguisedUser user = disguisedUsers.remove(player.getUniqueId());
        user.task.cancel();
        player.setGameMode(GameMode.SURVIVAL);
        player.setFlySpeed(0.1F);
        user.block.setType(user.material);
        player.getWorld().playSound(player.getLocation(), Sound.UI_TOAST_IN, 10, 1.2f);
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.GOLDEN_SHOVEL, 2, "Disguise");
    }

    private class DisguisedUser{
        public Block block;
        public Material material;
        public BukkitTask task;
    }

    @Override
    public Set<ItemStack> getItems() {
        return new HashSet<>(Collections.singletonList(getTool()));
    }
}
