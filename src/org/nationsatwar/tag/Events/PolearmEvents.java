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

public final class PolearmEvents implements Listener {
	
	protected final Tag plugin;
    
    public PolearmEvents(Tag plugin) {
    	
        this.plugin = plugin;
    }
    
    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    	
    	if (event.getDamager() instanceof Player == false)
    		return;
    	
    	Player player = (Player) event.getDamager();
    	
    	if (!player.getItemInHand().getType().equals(Material.IRON_SWORD))
    		return;
    	
    	List<Entity> targets = Utility.getTargetsInLine(player, 10, 2);
    	
    	for (Entity target : targets)
    		((LivingEntity) target).damage(4);
    }
    
    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
    	
    	// Returns if event is not an iron_sword left_click
    	if (!event.getMaterial().name().equals(Material.IRON_SWORD.name()))
    		return;
    	
    	List<Entity> targets = Utility.getTargetsInLine(event.getPlayer(), 10, 2);
    	
    	for (Entity target : targets)
    		((LivingEntity) target).damage(4, event.getPlayer());
    }
}