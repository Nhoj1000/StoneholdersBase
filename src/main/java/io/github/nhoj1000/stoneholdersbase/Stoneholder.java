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
    private Player player;
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

    public void setPlayer(Player player) {
        this.player = player;
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
        System.out.println(stones);
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
        boolean removed = stones.keySet().remove(stones.keySet().stream()
            .filter(s -> s.equals(stone))
            .peek(s -> {
                actionBarMessage("Lost " + stone);
                stone.getPlayerItems().forEach(i -> Arrays.stream(player.getInventory().getContents())
                        .filter(pi -> StoneholdersBase.comparePowerItems(i, pi))
                        .forEach(pi -> player.getInventory().removeItem(pi)));
                s.getPassivePowerSet().forEach(pp -> pp.deactivatePower(player));
            }).findFirst().orElse(null)
        );

        if(removed) {
            manaPreview(player.getInventory().getItemInMainHand());
            updateMaxMana();
        }

        return removed;
    }

    public void clearStones() {
        stones.keySet().forEach(s -> {
            s.getPlayerItems().forEach(i -> player.getInventory().remove(i));
            removeStone(s);
        });
    }

    public Set<Stone> getStones() {
        return stones.keySet();
    }

    public boolean hasStones() {
        return stones.size() > 0;
    }

    public boolean hasStone(String stoneName) {
        return stones.containsKey(StoneholdersBase.getStoneFromName(stoneName));
    }

    public void actionBarMessage(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    //region Mana stuff
    public void regen() {
        mana = Math.min(maxMana, mana + MANA_REGEN);
        updateMana();
    }

    public void updateMana() {
        if(!manaRequired) {
            mana = maxMana;
        }
        manaBar.setProgress(Math.max(0, mana) / maxMana);
    }

    public void manaPreview(ItemStack item) {
        Stone stone = StoneholdersBase.getStoneFromItem(item);
        if(item == null || !hasStones()) {
            previewBar.removePlayer(player);
        }

        if(stone == null) { //might still be holding a unique power item
            UniquePower uniquePower = stones.keySet().stream()
                    .filter(s -> s.getUniquePowerMap().containsKey(item))
                    .map(s -> s.getUniquePowerMap().get(item))
                    .findFirst().orElse(null);
            if(uniquePower == null) {
                previewBar.removePlayer(player);
            } else {
                previewBar.setProgress(Math.min(1, uniquePower.getManaCost()/maxMana));
                previewBar.addPlayer(player);
            }
        } else if(stones.get(stone) == null) {
            previewBar.removePlayer(player);
        } else {
            previewBar.setProgress(Math.min(1, stones.get(stone).getManaCost()/maxMana));
            previewBar.addPlayer(player);
        }
    }

    public void updateMaxMana() {
        maxMana = 80 + 20 * stones.size();
        if(hasStones()) {
            manaBar.addPlayer(player);
        } else {
            manaBar.removePlayer(player);
        }
    }

    public void boostManaRegen(double multiplier, int seconds) {
        double oldManaRegen = MANA_REGEN;
        MANA_REGEN *= multiplier;
        Bukkit.getScheduler().runTaskLater(StoneholdersBase.getInstance(),
                () -> MANA_REGEN = oldManaRegen,
                seconds * 20L);
    }

    //Toggles the need for mana
    public boolean toggleManaRequired() {
        manaRequired = !manaRequired;
        updateMana();
        return manaRequired;
    }
    //endregion
}
