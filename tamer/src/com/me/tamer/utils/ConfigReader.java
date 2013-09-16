package com.me.tamer.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import sun.tools.jar.Main;



/**
 * @author Kesyttäjät
 * This class is used to read object configurations files
 * Configuration files are stored in tamer-android/assets/data/config/
 *
 */
public class ConfigReader {

	
	
	
	public void readConfiguration(){
		Properties properties = new Properties();
		try {
		  properties.load(new FileInputStream("path/filename"));
		} catch (IOException e) {
		  System.out.println(e.getMessage());
		}
	}
}
