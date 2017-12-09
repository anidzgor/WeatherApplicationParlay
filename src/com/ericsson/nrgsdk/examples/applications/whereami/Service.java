package com.ericsson.nrgsdk.examples.applications.whereami;

import java.util.ArrayList;

import com.ericsson.nrgsdk.examples.applications.whereami.Configuration;
import com.ericsson.nrgsdk.examples.applications.whereami.Feature;

public class Service 
{
	private Feature parent;
	private ArrayList<User> usersOfService;
	
	public Service(Feature parent)
	{
		this.parent = parent;
		
		usersOfService = new ArrayList<User>();
	}
	
	public boolean addUser(User user)
	{
		if (!usersOfService.contains(user))
		{
			usersOfService.add(user);
			System.out.println("Dodano u¿ytkownika o numerze " + user.getNumer());
			return true;
		}
		
		return false;
	}
	
	public ArrayList<User> getUserOfService() {
		return usersOfService;
	}

}
