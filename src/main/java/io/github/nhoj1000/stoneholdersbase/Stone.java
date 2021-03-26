package io.github.nhoj1000.stoneholdersbase;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Stone {
    private final Map<ItemStack, Power> powerMap = new HashMap<>();
    private final Set<PassivePower> passivePowerSet = new HashSet<>();
    private final String stoneName;

    public Stone(String stoneName) {
        this.stoneName = stoneName;
    }

    public void registerPowers(Power... powers) {
        for(Power p: powers)
            powerMap.put(p.getTool(), p);
    }

    public void registerPassivePowers(PassivePower... powers) {
        for(PassivePower p: powers)
            passivePowerSet.add(p);
    }

    public Map<ItemStack, Power> getPowerMap() {
        return powerMap;
    }

    public Set<PassivePower> getPassivePowerSet() {
        return passivePowerSet;
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
