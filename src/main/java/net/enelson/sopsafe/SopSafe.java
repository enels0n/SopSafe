package net.enelson.sopsafe;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.reflect.ClassPath;

import net.enelson.sopsafe.commands.MainCommand;
import net.enelson.sopsafe.safes.SafeManager;

public class SopSafe extends JavaPlugin {

	public static SafeManager manager;
	public static Plugin plugin;
	
	public void onEnable() {
		plugin = this;
		manager = new SafeManager();
		
		PluginManager pluginManager = Bukkit.getPluginManager();
		try {
			String pac = "net.enelson.sopsafe.listeners";
			for (ClassPath.ClassInfo clazzInfo : ClassPath.from(getClassLoader()).getTopLevelClasses(pac)) {
				Class<?> clazz = Class.forName(clazzInfo.getName());
				if (Listener.class.isAssignableFrom(clazz)) {
					pluginManager.registerEvents((Listener) clazz.getDeclaredConstructor().newInstance(), this);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.getCommand("sopsafe").setExecutor(new MainCommand());
	}
	
	public void onDisable() {
		manager.onDisable();
	}
}


