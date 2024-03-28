package org.rammex.fmapgui.commands;

import com.massivecraft.factions.*;
import com.massivecraft.factions.perms.Relation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rammex.fmapgui.FMapGUI;
import org.rammex.fmapgui.utils.ItemBuilder;

public class FMapCommand implements CommandExecutor {
    static FMapGUI plugin;
    public FMapCommand(FMapGUI plugin) {
        FMapCommand.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Player player = (Player) sender;
        openMapUI(player);
        return false;
    }

    public static void openMapUI(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Factions Map");

        int playerChunkX = player.getLocation().getChunk().getX();
        int playerChunkZ = player.getLocation().getChunk().getZ();

        inventory.setItem(53,ItemBuilder.getMaterialLoreItem(Material.BOOK, plugin.getConfig().getString("book.name"), plugin.getConfig().getList("book.lore")));

        for (int dx = -height(); dx <= height(); dx++) {
            for (int dz = -width(); dz <= width(); dz++) {
                int chunkX = playerChunkX + dx;
                int chunkZ = playerChunkZ + dz;

                String relation = String.valueOf(getRelation(player, chunkX, chunkZ));

                ItemStack block;
                Material woolColor = Material.GRAY_WOOL;

                if (relation.equals(Relation.MEMBER.toString())) {
                    woolColor = Material.LIME_WOOL;
                } else if (relation.equals(Relation.ALLY.toString())) {
                    woolColor = Material.GREEN_WOOL;
                } else if (relation.equals(Relation.ENEMY.toString())) {
                    woolColor = Material.RED_WOOL;
                } else if (relation.equals(Relation.NEUTRAL.toString())) {
                    if(isWilderness(player, chunkX, chunkZ)) {
                        woolColor = Material.WHITE_WOOL;
                    } else if (isSafeZone(player, chunkX, chunkZ)) {
                        woolColor = Material.BROWN_WOOL;
                    } else if (isWarZone(player,chunkX,chunkZ)) {
                        woolColor = Material.BLACK_WOOL;
                    } else {
                        woolColor = Material.YELLOW_WOOL;
                    }
                }

                if (dx == 0 && dz == 0) {
                    block = new ItemStack(Material.PLAYER_HEAD, 1);
                } else {
                    block = new ItemStack(woolColor, 1);
                }

                ItemMeta blockMeta = block.getItemMeta();



                if (relation.equals(Relation.MEMBER.toString())) {
                    blockMeta.setDisplayName("§aMembre : "+getFactionName(player, chunkX, chunkZ));
                } else if (relation.equals(Relation.ALLY.toString())) {
                    blockMeta.setDisplayName("§2Allier : "+getFactionName(player, chunkX, chunkZ));
                } else if (relation.equals(Relation.ENEMY.toString())) {
                    blockMeta.setDisplayName("§4Ennemis : "+getFactionName(player, chunkX, chunkZ));
                } else if (relation.equals(Relation.NEUTRAL.toString())) {
                    if(isWilderness(player, chunkX, chunkZ)) {
                        blockMeta.setDisplayName(getFactionName(player, chunkX, chunkZ));
                    } else if (isSafeZone(player, chunkX, chunkZ)) {
                        blockMeta.setDisplayName(getFactionName(player, chunkX, chunkZ));
                    } else if (isWarZone(player,chunkX,chunkZ)) {
                        blockMeta.setDisplayName(getFactionName(player, chunkX, chunkZ));
                    } else {
                        blockMeta.setDisplayName("§eNeutre : "+getFactionName(player, chunkX, chunkZ));
                    }
                }
                block.setItemMeta(blockMeta);

                int inventoryIndex = (dx + 1) * 9 + dz + 13;


                inventory.setItem(inventoryIndex, block);
            }
        }

        player.openInventory(inventory);
    }

    private static Relation getRelation(Player player, int x, int z) {
        FPlayer fplayer = FPlayers.getInstance().getByPlayer(player);

        String worldName = player.getWorld().getName();

        FLocation flocation = new FLocation(worldName, x, z);
        Faction faction = Board.getInstance().getFactionAt(flocation);
        return fplayer.getRelationTo(faction);
    }

    private static String getFactionName(Player player, int x, int z) {
        String worldName = player.getWorld().getName();

        FLocation flocation = new FLocation(worldName, x, z);
        Faction faction = Board.getInstance().getFactionAt(flocation);
        return faction.getTag();
    }

    private static Boolean isWilderness(Player player, int x, int z) {
        String worldName = player.getWorld().getName();

        FLocation flocation = new FLocation(worldName, x, z);
        Faction faction = Board.getInstance().getFactionAt(flocation);
        return faction.isWilderness();
    }

    private static Boolean isSafeZone(Player player, int x,int z){
        String worldName = player.getWorld().getName();

        FLocation flocation = new FLocation(worldName, x, z);
        Faction faction = Board.getInstance().getFactionAt(flocation);
        return faction.isSafeZone();
    }

    private static Boolean isWarZone(Player player, int x,int z){
        String worldName = player.getWorld().getName();

        FLocation flocation = new FLocation(worldName, x, z);
        Faction faction = Board.getInstance().getFactionAt(flocation);
        return faction.isWarZone();
    }

    private static Integer width() {
        if(plugin.getConfig().getInt("MapSize.width") <= 4){
            return plugin.getConfig().getInt("MapSize.width");
        } else {
            return 4;
        }
    }

    private static Integer height() {
        if(plugin.getConfig().getInt("MapSize.height") <= 2){
            return plugin.getConfig().getInt("MapSize.height");
        } else {
            return 2;
        }
    }
}
