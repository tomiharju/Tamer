package com.me.tamer.physics;


import java.util.LinkedList;


public class ContactPool {
	static LinkedList<Contact> contactPool = new LinkedList<Contact>();

	
	public static void createPool(int size){
		for(int i = 0 ; i < size ; i++)
			contactPool.push(new Contact());
		
		System.out.println("Contactpool created, size "+contactPool.size());
	}
	public static Contact obtain(){
		if(contactPool.size() < 5){
			for(int i = 0 ; i < 5 ; i++)
				contactPool.push(new Contact());
			System.err.println("Warning, running out of contacts");
		}
		if(contactPool.size() > 50 )
			contactPool.pop();
		
		return contactPool.pop();
	}
	public static void restore(Contact c){
		c.reset();
		contactPool.push(c);
	}
	
	

}
