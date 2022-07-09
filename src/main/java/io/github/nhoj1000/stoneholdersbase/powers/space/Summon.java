package io.github.nhoj1000.stoneholdersbase.powers.space;

import io.github.nhoj1000.stoneholdersbase.StoneUtils;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class Summon implements Power {
    private final int numZombies, spawnRange;
    private final Random random = new Random();

    private static final String ZOMBIE_NAME = "Space Zombie";

    public Summon(int numZombies, int spawnRange) {
        this.numZombies = numZombies;
        this.spawnRange = spawnRange;
    }

    @Override
    public boolean usePower(Player player) {
        World world = player.getWorld();
        Location playerLoc = player.getLocation();
        for (int i = 0; i < numZombies; i++) {
            int randX = playerLoc.getBlockX() - spawnRange/2 + random.nextInt(spawnRange);
            int randZ = playerLoc.getBlockZ() - spawnRange/2 + random.nextInt(spawnRange);
            Location randLoc = new Location(world, randX, playerLoc.getY() + 100, randZ);

            Zombie target = (Zombie) player.getWorld().spawnEntity(StoneUtils.worldBorderBound(randLoc), EntityType.ZOMBIE);
            target.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
            target.setCustomName(ZOMBIE_NAME);
        }
        return true;
    }

    @Override
    public ItemStack getTool() {
        return StoneUtils.generateStoneTool(Material.STONE_SHOVEL, 4, "Summon", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 65;
    }

    public static String getZombieName() {
        return ZOMBIE_NAME;
    }
}
