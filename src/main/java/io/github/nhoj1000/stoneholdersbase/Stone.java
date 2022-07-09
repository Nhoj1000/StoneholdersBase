package io.github.nhoj1000.stoneholdersbase;

import io.github.nhoj1000.stoneholdersbase.powers.PassivePower;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.powers.UniquePower;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Stone {
    private final String stoneId;
    private final ChatColor colorCode;
    private final int damageID;

    private final Set<Power> powerSet = new HashSet<>();
    private final Map<ItemStack, UniquePower> uniquePowerMap = new HashMap<>();
    private final Set<PassivePower> passivePowerSet = new HashSet<>();

    public Stone(String stoneId, ChatColor colorCode, int damageID) {
        this.stoneId = stoneId;
        this.colorCode = colorCode;
        this.damageID = damageID;
    }

    //returns kit of items for player to receive
    public Set<ItemStack> getPlayerItems() {
        Set<ItemStack> items = new HashSet<>();
        items.add(getStoneItem());
        getUniquePowerSet().forEach(up -> items.addAll(up.getItems()));
        return items;
    }

    //returns physical item representing the stone
    public ItemStack getStoneItem() {
        return StoneUtils.generateStoneTool(Material.WOODEN_SWORD, damageID, getDisplayName(), Collections.singletonList(""));
    }

    //region Power registry
    public Stone registerPowers(Power... powers) {
        powerSet.addAll(Arrays.asList(powers));
        return this;
    }

    public Stone registerUniquePowers(UniquePower... powers) {
        Arrays.stream(powers).forEach(up -> uniquePowerMap.put(up.getActivationItem(), up));
        return this;
    }

    public Stone registerPassivePowers(PassivePower... powers) {
        passivePowerSet.addAll(Arrays.asList(powers));
        return this;
    }
    //endregion

    //region Power getters
    public Set<Power> getPowerSet() {
        return powerSet;
    }

    public Set<UniquePower> getUniquePowerSet() {
        return new HashSet<>(uniquePowerMap.values());
    }

    public Set<PassivePower> getPassivePowerSet() {
        return passivePowerSet;
    }

    public UniquePower getUniquePowerByItem(ItemStack item) {
        return uniquePowerMap.get(item);
    }
    //endregion

    //region Display bits
    public String getDisplayName() {
        return colorCode + stoneId + " Stone";
    }

    public String getId() {
        return stoneId;
    }
    //endregion


    @Override
    public int hashCode() {
        return stoneId.hashCode() + getPlayerItems().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }

}
