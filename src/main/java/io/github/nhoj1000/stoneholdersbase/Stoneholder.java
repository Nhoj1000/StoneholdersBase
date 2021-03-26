package io.github.nhoj1000.stoneholdersbase;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Stoneholder {
    private final Player player;
    private final Set<Stone> stones = new HashSet<>();
    private final List<Integer> cooldowns = new ArrayList<>();    //TODO add cooldown class

    public Stoneholder(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     *  Small check to see if the user has access to a certain power associated with a tool
     * @param i Tool to check
     * @param special false if you want a normal active power, true otherwise (shield, glass bow, etc.)
     * @return  null if user doesn't have power, returns the power otherwise
     */
    private Power checkTool(ItemStack i, boolean special) {
        for(Stone s: stones) {
            Power temp = s.getPowerMap().get(i);
            if(temp != null && temp.isSpecial() == special)
                return temp;
        }
        return null;
    }

    public boolean useStonePower(ItemStack i) {
        Power p = checkTool(i, false);
        if(p != null) {
            int cooldown = p.usePower(player);
            return true;
        }
        return false;
    }

    public void useSpecialPower(ItemStack i) {
        Power p = checkTool(i, true);
        if(p != null) {
            int cooldown = p.usePower(player);
        }
    }

    public void addStone(Stone stone) {
        stones.add(stone);
        actionBarMessage("Acquired " + stone);
        for(ItemStack i: stone.getPowerMap().keySet())  //TODO ensure no copies of items
            player.getInventory().addItem(i);
        for(PassivePower p: stone.getPassivePowerSet())
            p.activatePower(player);
    }

    public void removeStone(Stone stone) {
        stones.remove(stone);
        actionBarMessage("Lost " + stone);
        for(ItemStack i: stone.getPowerMap().keySet())
            player.getInventory().removeItem(i);
        for(PassivePower p: stone.getPassivePowerSet())
            p.deactivatePower(player);
    }

    public void clearStones() {
        for(Stone s: stones)
            removeStone(s);
    }

    public void actionBarMessage(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public boolean isStoneholder() {
        return stones.size() > 0;
    }
}
