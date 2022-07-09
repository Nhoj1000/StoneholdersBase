package io.github.nhoj1000.stoneholdersbase.powers.reality;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
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
    private final int range, maxTime;
    private static final Map<UUID, DisguisedUser> disguisedUsers = new HashMap<>();

    private static class DisguisedUser{
        public Block block;
        public Material material;
        public BukkitTask task;
    }

    public Disguise(int range, int maxTime) {
        this.range = range;
        this.maxTime = maxTime;
    }

    @Override
    public boolean usePower(Player player) {
        if(!disguisedUsers.containsKey(player.getUniqueId())) {
            Block target = player.getTargetBlockExact(range);
            if(target == null) {
                player.sendMessage("Block out of range!");
                return false;
            }
            player.setGameMode(GameMode.SPECTATOR);
            player.setFlySpeed(0);
            player.setVelocity(new Vector());
            player.getWorld().playSound(player.getLocation(), Sound.UI_TOAST_OUT, 10, 1.2f);

            DisguisedUser disguisedUser = new DisguisedUser();
            disguisedUser.block = player.getWorld().getBlockAt(player.getLocation());
            disguisedUser.material = disguisedUser.block.getType();
            disguisedUser.block.setType(target.getType());
            player.teleport(disguisedUser.block.getLocation().add(0.5, 0.5, 0.5)
                    .setDirection(player.getLocation().getDirection()));
            disguisedUser.task = Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(),
                    () -> unDisguise(player), maxTime * 20L);
            disguisedUsers.put(player.getUniqueId(), disguisedUser);
            return true;
        } else {
            unDisguise(player);
            return false;
        }
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
        return StoneUtils.generateStoneTool(Material.GOLDEN_SHOVEL, 2, "Disguise", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 35;
    }
}
