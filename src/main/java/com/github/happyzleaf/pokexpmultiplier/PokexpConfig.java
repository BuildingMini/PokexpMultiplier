package com.github.happyzleaf.pokexpmultiplier;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class PokexpConfig {
	private static PokexpConfig instance = new PokexpConfig();
	
	public static PokexpConfig getInstance() {
		return instance;
	}
	
	private ConfigurationLoader<CommentedConfigurationNode> configLoader;
	private CommentedConfigurationNode config;
	private File configFile;
	
	public void createConfig() {
		try {
			configFile.createNewFile();
			loadConfig();
			CommentedConfigurationNode players = config.getNode("config").setComment("These values will be used if none is found.");
			players.getNode("default_algorithm").setValue("multiplier");
			players.getNode("default_info").setValue("&3Your algorithm does not have a custom info.");
			
			CommentedConfigurationNode multiplier = config.getNode("algorithms", "multiplier");
			multiplier.getNode("algorithm").setComment("You can use #VALUE, #POKEMON_EXP, #VANILLA_EXP, #VANILLA_EXP_LEVEL.").setValue("#POKEMON_EXP * #VALUE");
			multiplier.getNode("default_value").setComment("If are going to use a custom #VALUE per user/group, be sure to enter a default one here, it has to be a number!").setValue(1.0f);
			multiplier.getNode("messages", "message").setComment("You can use #POKEMON, #PLAYER, #VALUE, #OLD_EXP, #NEW_EXP, #VANILLA_EXP, #VANILLA_EXP_LEVEL.").setValue("&2#PLAYER's &6#POKEMON&2 experience has been multiplied from #OLD_EXP to #NEW_EXP (multiplied by #VALUE)");
			multiplier.getNode("messages", "info").setComment("This text will be sent when a player executes /pokexp info [player], you can use #PLAYER, #VALUE, #VANILLA_EXP, #VANILLA_EXP_LEVEL.").setValue("Your experience multiplier should be #VALUE.");
			
			CommentedConfigurationNode vanillaExp = config.getNode("algorithms", "multiplier_with_vanilla_exp");
			vanillaExp.getNode("algorithm").setComment("WARNING: This returns 0 if the player has less then 1 vanilla exp level.").setValue("#POKEMON_EXP * #VANILLA_EXP_LEVEL * #VALUE");
			vanillaExp.getNode("default_value").setValue(0.5f);
			vanillaExp.getNode("messages", "message").setValue("&9#PLAYER's &6#POKEMON&9 experience has been multiplied from #OLD_EXP to #NEW_EXP thanks to his vanilla levels!");
			
			CommentedConfigurationNode noExp = config.getNode("algorithms", "no_exp");
			noExp.getNode("algorithm").setValue("0");
			noExp.getNode("messages", "message").setValue("&4No exp for you, what have you done?...");
			
			saveConfig();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> configLoader) {
		this.configLoader = configLoader;
		this.configFile = configFile;
		if (!configFile.exists()) {
			createConfig();
		} else
			loadConfig();
	}
	
	public CommentedConfigurationNode getConfig() {
		return config;
	}
	
	public void loadConfig() {
		if (!configFile.exists()) {
			createConfig();
		} else
			try {
				config = configLoader.load();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void saveConfig() {
		try {
			configLoader.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void saveAndLoad() {
		saveConfig();
		loadConfig();
	}
}

