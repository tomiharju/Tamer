package com.me.tamer.physics;


import java.util.LinkedList;


public class ContactPool {
	static LinkedList<Contact> contactPool = new LinkedList<Contact>();
	static int SIZE;
	
	public static void createPool(int size){
		SIZE = size;
		for(int i = 0 ; i < SIZE ; i++)
			contactPool.push(new Contact());
	
	}
	public static Contact obtain(){
		if(contactPool.size() < 5){
			for(int i = 0 ; i < 5 ; i++)
				contactPool.push(new Contact());
			System.err.println("Warning, running out of contacts");
		}
		if(contactPool.size() > SIZE )
			contactPool.pop();
		
		return contactPool.pop();
	}
	public static void restore(Contact c){
		c.reset();
		contactPool.push(c);
	}
	
	

}
