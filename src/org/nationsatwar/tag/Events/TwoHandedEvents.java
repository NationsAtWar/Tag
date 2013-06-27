package org.nationsatwar.tag.Events;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.nationsatwar.tag.Tag;
import org.nationsatwar.tag.Utility.Utility;

public final class TwoHandedEvents implements Listener {
	
	protected final Tag plugin;
    
    public TwoHandedEvents(Tag plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    	
    	if (event.getDamager() instanceof Player == false)
    		return;
    	
    	Player player = (Player) event.getDamager();
    	
    	if (!player.getItemInHand().getType().equals(Material.DIAMOND_SWORD))
    		return;
    	
    	List<Entity> targets = Utility.getTargetsInBox(player, 20, 10, 10, 3);
    	
    	for (Entity target : targets)
    		((LivingEntity) target).damage(5);
    }
    
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
    	
    	// Returns if event is not an iron_sword left_click
    	if (!event.getMaterial().name().equals(Material.DIAMOND_SWORD.name()))
    		return;
    	
    	List<Entity> targets = Utility.getTargetsInBox(event.getPlayer(), 20, 10, 10, 3);
    	
    	for (Entity target : targets)
    		((LivingEntity) target).damage(5, event.getPlayer());
    }
}