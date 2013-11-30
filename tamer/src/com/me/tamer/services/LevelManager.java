package com.me.tamer.services;

import java.util.ArrayList;
import java.util.List;

import com.me.tamer.core.Level;

public class LevelManager {
    private final List<Level> levels;
    private int currentLevel;

    public LevelManager()
    {
    	currentLevel = 0;
    	
    	// create the level 4
        Level level9 = new Level( 9 );
        level9.setName( "Episode 10" );
    	
    	// create the level 4
        Level level8 = new Level( 8 );
        level8.setName( "Episode 9" );
    	
    	// create the level 4
        Level level7 = new Level( 7 );
        level7.setName( "Episode 8" );
    	
    	// create the level 4
        Level level6 = new Level( 6 );
        level6.setName( "Episode 7" );
    	
    	// create the level 4
        Level level5 = new Level( 5 );
        level5.setName( "Episode 6" );
    	
    	// create the level 4
        Level level4 = new Level( 4 );
        level4.setName( "Episode 5" );
    	
    	 // create the level 3
        Level level3 = new Level( 3 );
        level3.setName( "Episode 4" );
    	
        //create the level 2
        Level level2 = new Level( 2 );
        level2.setName( "Episode 3" );
        level2.setNextLevel( level3 );
        
        // create the level 1
        Level level1 = new Level( 1 );
        level1.setName( "Episode 2" );
        level1.setHelpText("Use your spears before \n beasts  dive in to the swamp!");
        
        // create the level 0
        Level level0 = new Level( 0 );
        level0.setName( "Episode 1" );
        level0.setHelpText("help test");

        // register the levels
        levels = new ArrayList<Level>( 10 );

        levels.add( level0 );
        levels.add( level1 );
        levels.add( level2 );
        levels.add( level3 );
        levels.add( level4 );
        levels.add( level5 );
        levels.add( level6 );
        levels.add( level7 );
        levels.add( level8 );
        levels.add( level9 );
        
    }
    
    public List<Level> getLevels()
    {
        return levels;
    }
    
    public void setCurrentLevel(int id){
    	this.currentLevel = id;
    }
    
    public Level getCurrentLevel(){
 
        return levels.get( currentLevel );
    }
    
    public Level findLevelById( int id )
    {
        if( id < 0 || id >= levels.size() ) {
            return null;
        }
        return levels.get( id );
    }
}
