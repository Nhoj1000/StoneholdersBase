package io.github.nhoj1000.stoneholdersbase;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;

public class Stone {
    private final Map<ItemStack, Power> powerMap = new HashMap<>();
    private String stoneName;

    public Stone(String stoneName) {
        this.stoneName = stoneName;
    }

    public void registerPowers(Power... powers) {
        for(Power p: powers)
            powerMap.put(p.getTool(), p);
    }

    public Map<ItemStack, Power> getPowerMap() {
        return powerMap;
    }

    public String toString() {
        return stoneName;
    }

    public static ItemStack generateStoneTool(Material m, int d, String name) {
        ItemStack stoneTool = new ItemStack(m);
        ItemMeta meta = stoneTool.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(name);
        if(meta instanceof Damageable)
            ((Damageable)meta).setDamage(d);
        stoneTool.setItemMeta(meta);

        return stoneTool;
    }
}
