package io.github.nhoj1000.stoneholdersbase;

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
import java.util.stream.Collectors;

import static io.github.nhoj1000.stoneholdersbase.StoneConstants.SELECT_POWER_GUI_TITLE;

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
                updateManaBar();
            }
        }
    }

    public void useUniquePower(ItemStack item) {
        UniquePower p = null;
        for(Stone s: getStones()) {
            p = s.getUniquePowerByItem(item);
            if(p != null) {
                break;
            }
        }
        if(p != null && mana >= p.getManaCost()) {
            player.setCooldown(item.getType(), 10);
            if(p.usePower(player)) {
                mana -= p.getManaCost();
                updateManaBar();
            }
        }
    }

    public void selectPowerGUI(Stone s) {
        Inventory powerGUI = Bukkit.createInventory(null, 9, SELECT_POWER_GUI_TITLE);

        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta exitMeta = exit.getItemMeta();
        if(exitMeta != null) {
            exitMeta.setDisplayName(ChatColor.RED + "Exit");
        }
        exit.setItemMeta(exitMeta);

        ItemStack[] menuItems = new ItemStack[9];
        int i = 0;
        for(Power p: s.getPowerSet())
            menuItems[i++] = p.getTool();
        menuItems[8] = exit;

        powerGUI.setContents(menuItems);
        player.openInventory(powerGUI);
    }

    public void setStonePower(Power power) {
        for(Stone s: getStones()) {
            if (s.getPowerSet().contains(power)) {
                stones.put(s, power);
                updateManaPreviewBar(s.getStoneItem());
                break;
            }
        }
    }

    public boolean addStone(Stone stone) {
        boolean alreadyHasStone = hasStone(stone);
        if(!alreadyHasStone) {
            stones.put(stone, null);
            actionBarMessage("Acquired " + stone.getDisplayName());
            stone.getPlayerItems().stream()
                    .filter(i -> !player.getInventory().contains(i))
                    .forEach(i -> player.getInventory().addItem(i));
            stone.getPassivePowerSet().forEach(pp -> pp.activatePower(player));
        }
        updateMaxMana();
        return !alreadyHasStone;
    }

    public boolean removeStone(Stone stone) {
        boolean alreadyHasStone = hasStone(stone);
        if(alreadyHasStone) {
            stones.remove(stone);
            actionBarMessage("Lost " + stone.getDisplayName());
            stone.getPlayerItems().forEach(i -> Arrays.stream(player.getInventory().getContents())
                    .filter(pi -> StoneholdersBase.comparePowerItems(i, pi))
                    .forEach(pi -> player.getInventory().removeItem(pi)));
            stone.getPassivePowerSet().forEach(pp -> pp.deactivatePower(player));
            updateManaPreviewBar(player.getInventory().getItemInMainHand());
            updateMaxMana();
        }
        return alreadyHasStone;
    }

    public void clearStones() {
        List<String> stoneIds = getStones().stream().map(Stone::toString).collect(Collectors.toList());
        for(String stoneName: stoneIds) { //to avoid ConcurrentModificationException
            removeStone(StoneholdersBase.getStoneFromId(stoneName));
        }
    }

    public Set<Stone> getStones() {
        return stones.keySet();
    }

    public boolean hasStones() {
        return stones.size() > 0;
    }

    public boolean hasStone(String stoneName) {
        return hasStone(StoneholdersBase.getStoneFromId(stoneName));
    }

    public boolean hasStone(Stone stone) {
        return stones.containsKey(stone);
    }

    public void actionBarMessage(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    //region Mana stuff
    public void regen() {
        mana = Math.min(maxMana, mana + MANA_REGEN);
        updateManaBar();
    }

    public void updateManaBar() {
        if(!manaRequired) {
            mana = maxMana;
        }
        manaBar.setProgress(Math.max(0, mana) / maxMana);
    }

    public void updateManaPreviewBar(ItemStack item) {
        Stone stone = StoneholdersBase.getStoneFromItem(item);
        if(item == null || !hasStones()) {
            previewBar.removePlayer(player);
        }

        if(stone == null) { //might still be holding a unique power item
            UniquePower uniquePower = stones.keySet().stream()
                    .filter(s -> s.getUniquePowerByItem(item) != null)
                    .map(s -> s.getUniquePowerByItem(item))
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

    public boolean toggleManaRequired() {
        manaRequired = !manaRequired;
        updateManaBar();
        return manaRequired;
    }
    //endregion
}
