package bgu.atd.a1.sim;

import bgu.atd.a1.sim.Computer;

import java.util.Map;
import java.util.HashMap;

/**
 * represents a warehouse that holds a finite amount of computers
 * and their suspended mutexes.
 * releasing and acquiring should be blocking free.
 */
public class Warehouse{

	private static Warehouse instance = null;
	private Map<String, Computer> computers;

	private Warehouse(){

		computers = new HashMap<>();

	}

	public static Warehouse GetInstance(){

		if(instance == null)
			instance = new Warehouse();

		return instance;
		
	}

	public Map<String, Computer> GetComputers(){

		return computers;

	}
	
}