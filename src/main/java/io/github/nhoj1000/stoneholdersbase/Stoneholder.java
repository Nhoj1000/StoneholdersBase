package io.github.nhoj1000.stoneholdersbase;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Stoneholder {
    private final Player player;
    private final List<Stone> stones = new ArrayList<>();
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
     * @return  null if user doesn't have power, returns the power otherwise
     */
    public Power checkTool(ItemStack i) {
        for(Stone s: stones)
            if(s.getPowerMap().containsKey(i))
                return s.getPowerMap().get(i);
        return null;
    }

    public void useStonePower(ItemStack i) {
        Power p = checkTool(i);
        if(p != null) {
            int cooldown = p.usePower(player);
        }
    }

    public void addStone(Stone stone) {
        stones.add(stone);
        actionBarMessage("Acquired " + stone);
        for(ItemStack i: stone.getPowerMap().keySet())  //TODO ensure no copies of items
            player.getInventory().addItem(i);
    }

    public void removeStone(Stone stone) {
        stones.remove(stone);
        actionBarMessage("Lost " + stone);
    }

    public void actionBarMessage(String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }
}
