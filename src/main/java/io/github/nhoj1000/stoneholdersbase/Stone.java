package io.github.nhoj1000.stoneholdersbase;

import io.github.nhoj1000.stoneholdersbase.powers.PassivePower;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.powers.UniquePower;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Stone {
    private final String stoneName;
    private final int damageID;

    private final Set<Power> powerSet = new HashSet<>();
    private final Map<ItemStack, UniquePower> uniquePowerMap = new HashMap<>();
    private final Set<PassivePower> passivePowerSet = new HashSet<>();

    public Stone(String stoneName, int damageID) {
        this.stoneName = stoneName;
        this.damageID = damageID;
    }

    //returns kit of items for player to receive
    public Set<ItemStack> getPlayerItems() {
        Set<ItemStack> items = new HashSet<>();
        items.add(getStoneItem());
        for(UniquePower power: getUniquePowerMap().values())
            items.addAll(power.getItems());
        return items;
    }

    //returns physical item representing the stone
    public ItemStack getStoneItem() {
        return Stone.generateStoneTool(Material.WOODEN_SWORD, damageID, stoneName, Collections.singletonList(""));
    }

    public void registerPowers(Power... powers) {
        powerSet.addAll(Arrays.asList(powers));
    }

    public void registerUniquePowers(UniquePower... powers) {
        for(UniquePower up: powers)
            uniquePowerMap.put(up.getActivationItem(), up);
    }

    public void registerPassivePowers(PassivePower... powers) {
        Collections.addAll(passivePowerSet, powers);
    }

    public Set<Power> getPowerSet() {
        return powerSet;
    }

    public Map<ItemStack, UniquePower> getUniquePowerMap() {
        return uniquePowerMap;
    }

    public Set<PassivePower> getPassivePowerSet() {
        return passivePowerSet;
    }

    @Override
    public String toString() {
        return stoneName;
    }

    @Override
    public int hashCode() {
        return stoneName.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Stone)
            return stoneName.equals(((Stone) obj).stoneName);
        return false;
    }

    public static ItemStack generateStoneTool(Material m, int d, String name, List<String> lore) {
        ItemStack stoneTool = new ItemStack(m);
        ItemMeta meta = stoneTool.getItemMeta();
        meta.setUnbreakable(true);
        meta.setDisplayName(name);
        meta.setLore(lore);
        if(meta instanceof Damageable)
            ((Damageable)meta).setDamage(d);
        stoneTool.setItemMeta(meta);

        return stoneTool;
    }
}
