package org.nationsatwar.tag;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.nationsatwar.tag.Events.PolearmEvents;
import org.nationsatwar.tag.Events.TwoHandedEvents;

/**
 * The Tag parent class.
 * <p>
 * Custom combat functionality plugin for Minecraft
 * 
 * @author Aculem
 */
public final class Tag extends JavaPlugin {
	
	private static final Logger log = Logger.getLogger("Minecraft");

	/**
	 * Initializes the plugin on server startup.
	 */
	public void onEnable() {
		
    	// Register Events
		getServer().getPluginManager().registerEvents(new PolearmEvents(this), this);
		getServer().getPluginManager().registerEvents(new TwoHandedEvents(this), this);
    	
    	log("Tag has been enabled.");
	}

	/**
	 * Handles the plugin on server stop.
	 */
	public void onDisable() {
		
		log("Tag has been disabled.");
	}

	/**
	 * Plugin logger handler. Useful for debugging.
	 * 
	 * @param logMessage  Message to send to the console.
	 */
	public static void log(String logMessage) {
		
		log.info("Tag: " + logMessage);
	}
}