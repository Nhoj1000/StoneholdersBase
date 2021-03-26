package io.github.nhoj1000.stoneholdersbase.powers.soul;

import io.github.nhoj1000.stoneholdersbase.PassivePower;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoulCollector implements PassivePower {
    private static Map<UUID, Integer> soulCount = new HashMap<>();
    private static int lvl1, lvl2, lvl3;
    private static int collectionRadius;

    public SoulCollector(int collectionRadius, int lvl1, int lvl2, int lvl3) {
        this.collectionRadius = collectionRadius;
        this.lvl1 = lvl1;
        this.lvl2 = lvl2;
        this.lvl3 = lvl3;
    }

    @Override
    public void activatePower(Player p) {
        soulCount.put(p.getUniqueId(), 0);
    }

    @Override
    public void deactivatePower(Player p) {
        soulCount.remove(p.getUniqueId());
    }

    public static void addSoul(Player p, Player soulTarget) {
        if(soulCount.containsKey(p.getUniqueId())) {
            int souls = soulCount.get(p.getUniqueId()) + 1;
            soulCount.put(p.getUniqueId(), souls);
            p.setHealth(Math.min(p.getHealth() + 4, 20));
            p.sendMessage(ChatColor.GOLD + "Soul of " + soulTarget.getName() + " collected. " + souls + " total souls.");

            if(souls == lvl1)
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
            else if(souls == lvl2)
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1));
            else if(souls == lvl3)
                p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 2));
        }
    }

    public static void clearSouls(Player p) {
        if(soulCount.containsKey(p.getUniqueId()))
            soulCount.put(p.getUniqueId(), 0);
    }

    public static int getCollectionRadius() {
        return collectionRadius;
    }
}
