package org.nationsatwar.tag;

import java.io.File;
import java.net.MalformedURLException;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;
import org.nationsatwar.tag.Events.AttackEvents;

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
		
		try {
			((PluginClassLoader) this.getClassLoader()).addURL(new File("plugins/ToyChest.jar").toURI().toURL());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	// Register Events
		getServer().getPluginManager().registerEvents(new AttackEvents(this), this);
    	
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