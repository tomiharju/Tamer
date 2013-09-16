package com.me.tamer.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author tharju
 * Utility class that handles the environment creation
 * Is responsible for creating gameobjects via GameObjectFactory
 * Reads level data from level[x].properties
 */
public class EnvironmentCreator {
	
	Properties props = null;
	
	
	public void create(int level_number){
		Properties properties = new Properties();
		try {
		  properties.load(new FileInputStream("config.properties"));
		  int lvl_number = Integer.parseInt(properties.getProperty("number"));
		  int num_worms = Integer.parseInt(properties.getProperty("num_worms"));
		  int num_quicksand = Integer.parseInt(properties.getProperty("num_quicksand"));
		  
		  System.out.println(lvl_number +" "+ num_worms +" "+num_quicksand);
		  
		  
		  
		  
		  
		  
		  
		  
		  
		} catch (IOException e) {
		  System.out.println(e.getMessage());
		}
	
	}

}
