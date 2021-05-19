package io.github.nhoj1000.stoneholdersbase;

import io.github.nhoj1000.stoneholdersbase.powers.PassivePower;
import io.github.nhoj1000.stoneholdersbase.powers.Power;
import io.github.nhoj1000.stoneholdersbase.powers.UniquePower;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Stoneholder {
    private final Player player;
    private final Map<Stone, Power> stones = new HashMap<>();  //currently held stones
    private static double MANA_REGEN = 1;    //passive mana regen per second
    private double mana, maxMana;   //current mana and max mana, respectively
    BossBar manaBar = Bukkit.createBossBar("Mana", BarColor.PURPLE, BarStyle.SEGMENTED_10);
    BossBar previewBar = Bukkit.createBossBar("", BarColor.GREEN, BarStyle.SEGMENTED_10);

    private boolean manaRequired = true;  //true if mana is required, false if not

    public Stoneholder(Player player) {
        this.player = player;
        mana = 50;
        updateMaxMana();
    }

    public Player getPlayer() {
        return player;
    }

    public void useNormalPower(Stone s) {
        Power p = stones.get(s);
        if(p != null && mana >= p.getManaCost()) {
            player.setCooldown(s.getStoneItem().getType(), 10);
            if(p.usePower(player)) {
                mana -= p.getManaCost();
                updateMana();
            }
        }
    }

    public void useUniquePower(ItemStack i) {
        UniquePower p = null;
        for(Stone s: stones.keySet())
            if(s.getUniquePowerMap().containsKey(i)) {
                p = s.getUniquePowerMap().get(i);
                break;
            }
        if(p != null && mana >= p.getManaCost()) {
            player.setCooldown(i.getType(), 10);
            if(p.usePower(player)) {
                mana -= p.getManaCost();
                updateMana();
            }
        }
    }

    public void selectPowerGUI(Stone s) {
        Inventory powerGUI = Bukkit.createInventory(null, 9, ChatColor.DARK_GRAY + "Power Selection");

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exitMeta = exit.getItemMeta();
        exitMeta.setDisplayName(ChatColor.RED + "Exit");
        exit.setItemMeta(exitMeta);

        ItemStack[] menuItems = new ItemStack[9];
        int i = 0;
        for(Power p: s.getPowerSet())
            menuItems[i++] = p.getTool();
        menuItems[8] = exit;

        powerGUI.setContents(menuItems);
        player.openInventory(powerGUI);
    }

    public void setStonePower(Power p) {
        Stone stone = null;
        for(Stone s: stones.keySet())
            if(s.getPowerSet().contains(p)) {
                stones.put(s, p);
                stone = s;
                break;
            }
        if(stone != null)
            manaPreview(stone.getStoneItem());
    }

    public boolean addStone(Stone stone) {
        if(!stones.containsKey(stone)) {
            stones.put(stone, null);
            actionBarMessage("Acquired " + stone);
            for (ItemStack i : stone.getPlayerItems())
                if(!player.getInventory().contains(i)) player.getInventory().addItem(i);
            for (PassivePower p : stone.getPassivePowerSet())
                p.activatePower(player);
        } else
            return false;
        updateMaxMana();
        return true;
    }

    public boolean removeStone(Stone stone) {
        if(stones.containsKey(stone)) {
            stones.remove(stone);
            actionBarMessage("Lost " + stone);
            for (ItemStack i : stone.getPlayerItems())
                for(ItemStack pi: player.getInventory().getContents())
                    if(StoneholdersBase.comparePowerItems(i, pi)) player.getInventory().removeItem(pi);
            for (PassivePower p : stone.getPassivePowerSet())
                p.deactivatePower(player);
        } else
            return false;
        if(!isStoneholder())
            previewBar.removePlayer(player);
        updateMaxMana();
        return true;
    }

    public void clearStones() {
        Iterator<Stone> iterator = stones.keySet().iterator();
        while (iterator.hasNext()) {
            Stone stone = iterator.next();
            for (ItemStack i : stone.getPlayerItems())
                player.getInventory().removeItem(i);
            for (PassivePower p : stone.getPassivePowerSet())
                p.deactivatePower(player);
            iterator.remove();
        }
    }

    public Set<Stone> getStones() {
        return stones.keySet();
    }

    public void actionBarMessage(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public boolean isStoneholder() {
        return stones.size() > 0;
    }

    //region Mana stuff
    public void regen() {
        mana = Math.min(maxMana, mana + MANA_REGEN);
        updateMana();
    }

    public void updateMana() {
        if(!manaRequired) mana = maxMana;
        manaBar.setProgress(Math.max(0, mana)/maxMana);
    }

    public void manaPreview(ItemStack i) {
        Stone s = StoneholdersBase.getStoneFromItem(i);
        if(s == null || stones.get(s) == null) {
            UniquePower p = null;
            for(Stone stone: stones.keySet())
                if(stone.getUniquePowerMap().containsKey(i)) {
                    p = stone.getUniquePowerMap().get(i);
                    break;
                }
            if(p != null) {
                previewBar.setProgress(Math.min(1, p.getManaCost()/maxMana));
                previewBar.addPlayer(player);
            } else
                previewBar.removePlayer(player);
        } else {
            previewBar.setProgress(Math.min(1, stones.get(s).getManaCost()/maxMana));
            previewBar.addPlayer(player);
        }
    }

    private void updateMaxMana() {
        maxMana = 80 + 20 * stones.size();
        if(isStoneholder())
            manaBar.addPlayer(player);
        else
            manaBar.removePlayer(player);
    }

    public void boostManaRegen(double multiplier, int seconds) {
        double oldManaRegen = MANA_REGEN;
        MANA_REGEN *= multiplier;
        Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(), () -> MANA_REGEN = oldManaRegen, seconds * 20L);
    }

    //Toggles the need for mana
    public boolean toggleManaRequired() {
        manaRequired = !manaRequired;
        updateMana();
        return manaRequired;
    }
    //endregion
}
