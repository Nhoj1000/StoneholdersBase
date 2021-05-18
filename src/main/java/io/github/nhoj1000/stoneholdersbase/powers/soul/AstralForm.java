package io.github.nhoj1000.stoneholdersbase.powers.soul;

import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.Stone;
import io.github.nhoj1000.stoneholdersbase.StoneholdersBase;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collections;


public class AstralForm implements Power {
    private static int astralTime;

    public AstralForm(int astralTime) {
        AstralForm.astralTime = astralTime;
    }

    @Override
    public int usePower(Player player) {
        Zombie place = (Zombie) player.getWorld().spawnEntity(player.getLocation(), EntityType.ZOMBIE);
        place.setCustomName(player.getName());
        place.setCustomNameVisible(true);
        place.getEquipment().setArmorContents(player.getEquipment().getArmorContents());
        place.getEquipment().setHelmet(StoneholdersBase.getPlayerHead(player));
        place.setVelocity(player.getVelocity());
        place.setAware(false);
        place.setSilent(true);
        place.setBaby();
        place.setHealth(player.getHealth());
        place.getEquipment().setItemInMainHand(player.getEquipment().getItemInMainHand());
        place.getEquipment().setItemInOffHand(player.getEquipment().getItemInOffHand());
        place.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        player.sendMessage(ChatColor.GOLD + "Astral form active for " + astralTime + " seconds!");
        player.setGameMode(GameMode.SPECTATOR);

        place.getLocation().getChunk().load(true);

        Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(), () -> {
            player.sendMessage(ChatColor.GOLD + "Leaving Astral form!");
            player.teleport(place);
            player.setGameMode(GameMode.SURVIVAL);
            player.setVelocity(place.getVelocity());
            player.setHealth(place.getHealth());
            place.remove();
        }, astralTime * 20L);

        return 0;
    }

    @Override
    public ItemStack getTool() {
        return Stone.generateStoneTool(Material.GOLDEN_SHOVEL, 3, "Astral Form", Collections.singletonList(""));
    }

    @Override
    public int getManaCost() {
        return 60;
    }
}
