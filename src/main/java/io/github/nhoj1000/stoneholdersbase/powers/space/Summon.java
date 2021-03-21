package io.github.nhoj1000.stoneholdersbase.powers.space;

import io.github.nhoj1000.stoneholdersbase.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class Summon implements Power {
    private int numZombies, spawnRange;

    public Summon(int numZombies, int spawnRange) {
        this.numZombies = numZombies;
        this.spawnRange = spawnRange;
    }

    @Override
    public int usePower(Player player) {
        Random random = new Random();
        int randX, randZ;
        for (int i = 0; i < numZombies; i++) {
            randX = random.nextInt(spawnRange);
            randZ = random.nextInt(spawnRange);
            Zombie target = (Zombie) player.getWorld().spawnEntity(player.getLocation()
                    .add(randX - spawnRange/2, 100, randZ - spawnRange/2), EntityType.ZOMBIE);
            target.getEquipment().setHelmet(new ItemStack(Material.LEATHER_HELMET));
            target.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
            target.setCustomName(getZombieName());
        }

        return 1;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.STONE_SHOVEL, 4, "Summon");
    }

    public static String getZombieName() {
        return "Space Zombie";
    }
}
