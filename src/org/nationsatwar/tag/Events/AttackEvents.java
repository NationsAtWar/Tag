package org.nationsatwar.tag.Events;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.nationsatwar.tag.Tag;
import org.nationsatwar.tag.Utility.Utility;
import org.nationsatwar.toychest.Utility.ToyChestHook;

public final class AttackEvents implements Listener {
	
	protected final Tag plugin;
    
    public AttackEvents(Tag plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    	
    	if (event.getDamager() instanceof Player == false)
    		return;
    	
    	Player player = (Player) event.getDamager();
    	String itemName = player.getItemInHand().getType().name();
    	
    	if (!ToyChestHook.toyExists(itemName))
    		return;
    	
    	List<Entity> targets = gatherTargets(player, itemName);
    	
    	int customDamage = ToyChestHook.getDamage(itemName);
    	
    	if (targets == null) {
    		
    		event.setDamage(customDamage);
    		return;
    	}
    	
    	event.setCancelled(true);
    	
    	for (Entity target : targets)
    		((LivingEntity) target).damage(customDamage);
    }
    
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
    	
    	Player player = (Player) event.getPlayer();
    	String itemName = player.getItemInHand().getType().name();
    	
    	if (!ToyChestHook.toyExists(itemName))
    		return;
    	
    	List<Entity> targets = gatherTargets(player, itemName);
    	
    	int customDamage = ToyChestHook.getDamage(itemName);
    	
    	for (Entity target : targets)
    		((LivingEntity) target).damage(customDamage);
    }
    
    private List<Entity> gatherTargets(Player player, String itemName) {
    	
    	int customRange = (Integer) ToyChestHook.getCustomValue(itemName, "Range");
    	int customWidth = (Integer) ToyChestHook.getCustomValue(itemName, "Width");
    	int customHeight = (Integer) ToyChestHook.getCustomValue(itemName, "Height");
    	
    	if (customRange == 0)
    		return null;
    	
    	if (customWidth == 0 && customHeight == 0) {
        	
        	List<Entity> targets = Utility.getTargetsInLine(player, customRange, 3);
        	return targets;
    	}
    	
    	List<Entity> targets = Utility.getTargetsInBox(player, customRange, customWidth, customHeight, 3);
    	return targets;
    }
}