package org.rammex.fmapgui.events;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.perms.Relation;
import com.massivecraft.factions.perms.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.rammex.fmapgui.FMapGUI;
import org.rammex.fmapgui.commands.FMapCommand;
import org.rammex.fmapgui.utils.ItemBuilder;

public class FMapClickEvent implements Listener {
    FMapGUI plugin;
    public FMapClickEvent(FMapGUI plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onUiClick(InventoryClickEvent event) {
        final Player playerWhoClicked = (Player) event.getWhoClicked();
        final String inventoryTitle = event.getView().getTitle();
        ItemStack clickedItem = event.getCurrentItem();
        if (inventoryTitle.equalsIgnoreCase("Factions Map")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            if(clickedItem.getType() == Material.YELLOW_WOOL){
                openFactionNeutralGestion(playerWhoClicked,clickedItem.getItemMeta().getDisplayName());
            }
            if(clickedItem.getType() == Material.GREEN_WOOL){
                openFactionAllyGestion(playerWhoClicked,clickedItem.getItemMeta().getDisplayName());
            }
            if(clickedItem.getType() == Material.RED_WOOL){
                openFactionEnemyGestion(playerWhoClicked,clickedItem.getItemMeta().getDisplayName());
            }
        }
        if (inventoryTitle.contains("Gestion")) {
            event.setCancelled(true);
            event.setResult(InventoryClickEvent.Result.DENY);
            if(clickedItem.getType() == Material.RED_CONCRETE){
                FPlayer fplayer = FPlayers.getInstance().getByPlayer(playerWhoClicked);

                if(fplayer.hasFaction() && fplayer.getRole() == Role.ADMIN || fplayer.getRole() == Role.COLEADER) {
                    Faction playerFaction = fplayer.getFaction();
                    Faction targetFaction = Factions.getInstance().getByTag(inventoryTitle.replace(" Gestion",""));
                    playerFaction.setRelationWish(targetFaction, Relation.ENEMY);
                }
                playerWhoClicked.closeInventory();
                playerWhoClicked.sendMessage("§cVous avez déclaré la guerre à "+inventoryTitle.replace(" Gestion",""));
            }
            if(clickedItem.getType() == Material.GREEN_CONCRETE){
                FPlayer fplayer = FPlayers.getInstance().getByPlayer(playerWhoClicked);
                if(fplayer.hasFaction() && fplayer.getRole() == Role.ADMIN || fplayer.getRole() == Role.COLEADER) {
                    Faction playerFaction = fplayer.getFaction();
                    Faction targetFaction = Factions.getInstance().getByTag(inventoryTitle.replace(" Gestion",""));
                    playerFaction.setRelationWish(targetFaction, Relation.ALLY);
                }
                playerWhoClicked.closeInventory();
                playerWhoClicked.sendMessage("§aVous ête maintenant allier avec la faction "+inventoryTitle.replace(" Gestion",""));
            }
            if(clickedItem.getType() == Material.WHITE_CONCRETE){
                FPlayer fplayer = FPlayers.getInstance().getByPlayer(playerWhoClicked);
                if(fplayer.hasFaction() && fplayer.getRole() == Role.ADMIN || fplayer.getRole() == Role.COLEADER) {
                    Faction playerFaction = fplayer.getFaction();
                    Faction targetFaction = Factions.getInstance().getByTag(inventoryTitle.replace(" Gestion",""));
                    playerFaction.setRelationWish(targetFaction, Relation.NEUTRAL);
                }
                playerWhoClicked.closeInventory();
                playerWhoClicked.sendMessage("§aVous ête maintenant neutre avec la faction "+inventoryTitle.replace(" Gestion",""));
            }
            if(clickedItem.getType() == Material.RED_DYE){
                playerWhoClicked.closeInventory();
                FMapCommand.openMapUI(playerWhoClicked);
            }
        }
    }


    public void openFactionNeutralGestion(Player player,String factionName) {
        Inventory inv = Bukkit.createInventory(null, 27, factionName.replace("§eNeutre : ","")+" Gestion");


        inv.setItem(12, ItemBuilder.getSimpleMaterialItem(Material.RED_CONCRETE, ChatColor.RED+"Faire la guerre"));
        inv.setItem(14, ItemBuilder.getSimpleMaterialItem(Material.GREEN_CONCRETE, ChatColor.GREEN+"Faire une alliance"));
        inv.setItem(18, ItemBuilder.getSimpleMaterialItem(Material.RED_DYE, ChatColor.RED+"Retour"));


        player.openInventory(inv);
    }

    public void openFactionAllyGestion(Player player,String factionName) {
        Inventory inv = Bukkit.createInventory(null, 27, factionName.replace("§2Allier : ","")+" Gestion");


        inv.setItem(12, ItemBuilder.getSimpleMaterialItem(Material.RED_CONCRETE, ChatColor.RED+"Faire la guerre"));
        inv.setItem(14, ItemBuilder.getSimpleMaterialItem(Material.WHITE_CONCRETE, ChatColor.GREEN+"Devenir Neutre"));
        inv.setItem(18, ItemBuilder.getSimpleMaterialItem(Material.RED_DYE, ChatColor.RED+"Retour"));


        player.openInventory(inv);
    }

    public void openFactionEnemyGestion(Player player,String factionName) {
        Inventory inv = Bukkit.createInventory(null, 27, factionName.replace("§4Ennemis : ","")+" Gestion");


        inv.setItem(12, ItemBuilder.getSimpleMaterialItem(Material.WHITE_CONCRETE, ChatColor.RED+"Devenir Neutre"));
        inv.setItem(14, ItemBuilder.getSimpleMaterialItem(Material.GREEN_CONCRETE, ChatColor.GREEN+"Faire une alliance"));
        inv.setItem(18, ItemBuilder.getSimpleMaterialItem(Material.RED_DYE, ChatColor.RED+"Retour"));


        player.openInventory(inv);
    }
}
