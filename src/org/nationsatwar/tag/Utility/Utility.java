package org.nationsatwar.tag.Utility;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_5_R3.AxisAlignedBB;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.nationsatwar.tag.Tag;

public class Utility {
	
	/**
	 * Gets all targets in line of sight
	 * 
	 * @param player  The player to collect from line of sight
	 * @param range  How far to check for targets
	 * 
	 * @return  The list of entities within' the player's direct line of sight
	 */
	public static List<Entity> getTargetsInLine(Player player, int range, int maxAmount) {
		
		List<Entity> targetList = getTargets(player, range, 0, 0, true);
		
		targetList = getClosestEntities(targetList, player, maxAmount);
		
		return targetList;
	}
	
	/**
	 * Gets all targets within line of sight inside a box
	 * 
	 * @param player  The player to collect from line of sight
	 * @param range  How far to check for targets
	 * @param width  How wide the box to check for targets
	 * @param height  How tall the box to check for targets
	 * 
	 * @return  The list of entities within' the player's boxed line of sight
	 */
	public static List<Entity> getTargetsInBox(Player player, int range, int width, int height, int maxAmount) {
		
		List<Entity> targetList = getTargets(player, range, width, height, false);
		
		targetList = getClosestEntities(targetList, player, maxAmount);
		
		return targetList;
	}
	
	private static List<Entity> getTargets(Player player, int range, int width, int height, boolean requireDirect) {
		
		List<Entity> nearbyEntities = player.getNearbyEntities(range, range, range);
		List<Entity> targetList = new ArrayList<Entity>();
		
		Vector playerVector = ((CraftPlayer) player).getEyeLocation().toVector();
		
		Vector checkVector = new Vector();
		
		for (Entity entity : nearbyEntities) {
			
			// Continue if entity is not alive or not valid
			if (entity instanceof LivingEntity == false || !entity.isValid())
				continue;
			
			// Reset the vector for every entity and get the new entityVector
			Vector entityVector = entity.getLocation().toVector();
			checkVector.copy(playerVector);
			
			// The default bounding box is actually a bit smaller than the entity's normal hit box
			// TODO: Figure out an algorithm that makes this accurate for any entity type
			AxisAlignedBB entityHitbox = ((CraftLivingEntity) entity).getHandle().boundingBox;
			
			// This will generate an attackHitbox based on your eye as the origin
			AxisAlignedBB attackHitbox = ((CraftLivingEntity) player).getHandle().boundingBox;
			attackHitbox = attackHitbox.grow(width / 2, height / 2, width / 2);
			
			double distance = checkVector.distance(entityVector);
			
			// If 'requireDirect' is enabled, then check if a single vector hits the enemy hitbox
			if (requireDirect) {
				
				// Gets the player direction vector
				Vector direction = player.getLocation().getDirection().multiply(0.2f);
				
				Vector vector1 = new Vector(entityHitbox.a, entityHitbox.b, entityHitbox.c);
				Vector vector2 = new Vector(entityHitbox.d, entityHitbox.e, entityHitbox.f);
				
				while (checkVector.distance(playerVector) < range) {
					
					// If vector is inside entity, then we have a hit, return entity
					if (checkVector.isInAABB(vector1, vector2)) {
						
						targetList.add(entity);
						break;
					}
					
					// Otherwise, increment the vector and break if distance increases
					checkVector.add(direction);
					
					if (checkVector.distance(entityVector) > distance || 
							!checkVector.toLocation(player.getWorld()).getBlock().isEmpty())
						break;
					
					distance = checkVector.distance(entityVector);
				}
			} else {
				
				// Gets the player direction vector
				Vector direction = player.getLocation().getDirection().multiply(width);
				
				// Moves the hitbox to directly in front of the player
				attackHitbox.d(direction.getX(), direction.getY(), direction.getZ());
				
				// Reduces the direction increment rate
				direction = direction.normalize().multiply(0.2f);

				while (checkVector.distance(playerVector) < range) {
				
					// If entityHitbox is inside attackHitbox, then we have a hit, return entity
					if (attackHitbox.a(entityHitbox)) {
						
						targetList.add(entity);
						break;
					}
					
					// Otherwise, increment the vector and break if distance increases
					checkVector.add(direction);
					attackHitbox.d(direction.getX(), direction.getY(), direction.getZ());
					
					if (checkVector.distance(entityVector) > distance || 
							!checkVector.toLocation(player.getWorld()).getBlock().isEmpty())
						break;
					
					distance = checkVector.distance(entityVector);
				}
			}
		}
		
		Tag.log(targetList.size() + "");
		
		return targetList;
	}
	
	private static List<Entity> getClosestEntities(List<Entity> entityList, Player player, int maxAmount) {

		Location playerLocation = player.getLocation();
		List<Entity> closestEntities = new ArrayList<Entity>();
		
		int debug = 0;
		
		while (closestEntities.size() < entityList.size() && closestEntities.size() < maxAmount) {
			
			double closestDistance = Double.POSITIVE_INFINITY;
			Entity closestEntity = null;
			
			for (Entity entity : entityList) {
				
				Location entityLocation = entity.getLocation();
				double distance = playerLocation.distance(entityLocation);
				
				if (distance < closestDistance) {
					
					closestDistance = distance;
					closestEntity = entity;
				}
			}
			
			if (closestEntity != null) {
				
				closestEntities.add(closestEntity);
				entityList.remove(closestEntity);
			}
			
			debug++;
			
			if (debug > 20)
				break;
		}
		
		return closestEntities;
	}
}

