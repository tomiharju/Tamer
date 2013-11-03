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
    	
    	
    	 // create the level 2
        Level level3 = new Level( 3 );
        level3.setName( "Episode 4" );
    	
    	
        //create the level 2
        Level level2 = new Level( 2 );
        level2.setName( "Episode 3" );
        level2.setNextLevel( level2 );
        
        
        // create the level 1
        Level level1 = new Level( 1 );
        level1.setName( "Episode 2" );
        level1.setNextLevel( level2 );
        

        // create the level 0
        Level level0 = new Level( 0 );
        level0.setName( "Episode 1" );
        level0.setNextLevel( level0 );

        // register the levels
        levels = new ArrayList<Level>( 3 );
        
        
        
        levels.add( level0 );
        //levels.add( level1 );
        //levels.add( level2 );
        //levels.add( level3 );
        
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
