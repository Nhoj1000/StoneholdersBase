package io.github.nhoj1000.stoneholdersbase;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.util.Vector;

import java.util.*;

public class StoneUtils {
    private static final Map<String, Stone> STONE_ID_MAP = new HashMap<>();
    private static final Map<ItemStack, Stone> STONE_ITEM_MAP = new HashMap<>();

    //Used to compare item types for powers
    public static boolean comparePowerItems(ItemStack i1, ItemStack i2) {
        if(i1 == i2) {return true;}
        if((i1 == null || i2 == null) || (i1.getType() != i2.getType())) {return false;}

        ItemMeta m1 = i1.getItemMeta();
        ItemMeta m2 = i2.getItemMeta();
        if(m1 == null && m2 == null) return true;
        if(m1 == null || m2 == null) return false;
        if(!m1.getDisplayName().equals(m2.getDisplayName())) return false;
        if(m1.getLore() != null && m2.getLore() != null && !m1.getLore().equals(m2.getLore())) return false;
        if(m1 instanceof Damageable && m2 instanceof Damageable && ((Damageable) m1).getDamage() != ((Damageable) m2).getDamage()) return false;
        return (m1.isUnbreakable() == m2.isUnbreakable());
    }

    public static ItemStack generateStoneTool(Material m, int d, String name, List<String> lore) {
        ItemStack stoneTool = new ItemStack(m);
        ItemMeta meta = stoneTool.getItemMeta();
        if(meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            damageable.setUnbreakable(true);
            damageable.setDisplayName(name);
            damageable.setLore(lore);
            damageable.setDamage(d);
            stoneTool.setItemMeta(damageable);
        }

        return stoneTool;
    }

    public static ItemStack getPlayerHead(Player player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if(meta != null) {
            meta.setOwningPlayer(player);
        }
        skull.setItemMeta(meta);
        return skull;
    }

    //Helper method to determine if one player is looking at another
    //Props to Mr.Midnight on spigotmc.org
    public static boolean isLookingAt(Player player, LivingEntity entity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = entity.getEyeLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());

        return dot > 0.99D;
    }

    public static void registerStone(Stone stone) {
        STONE_ID_MAP.put(stone.getId(), stone);
        STONE_ITEM_MAP.put(stone.getStoneItem(), stone);
    }

    public static Set<String> getAllStoneIds() {
        return STONE_ID_MAP.keySet();
    }

    public static Set<Stone> getAllStones() {
        return new HashSet<>(STONE_ID_MAP.values());
    }

    public static Stone getStoneFromItem(ItemStack item) {
        return STONE_ITEM_MAP.get(item);
    }

    public static Stone getStoneFromId(String id) {
        return STONE_ID_MAP.get(id);
    }

    //keep a given location bounded by the world's border
    public static Location worldBorderBound(Location loc) {
        World world = loc.getWorld();
        if(world == null) {return loc;}

        Location center = world.getWorldBorder().getCenter();
        double size = world.getWorldBorder().getSize();

        double xCoord = Math.max(center.getX() - size/2, Math.min(loc.getX(), center.getX() + size/2));
        double zCoord = Math.max(center.getZ() - size/2, Math.min(loc.getZ(), center.getZ() + size/2));

        return new Location(world, xCoord, loc.getY(), zCoord);
    }
}
