package bgu.atd.a1.sim.privateStates;

import bgu.atd.a1.PrivateState;

import java.util.LinkedList;
import java.util.List;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState(){

		availableSpots = 0;
		registered = 0;
		regStudents = new LinkedList<>();
		prequisites = new LinkedList<>();

	}

	public Integer getAvailableSpots(){

		return availableSpots;

	}

	public Integer getRegistered(){

		return registered;

	}

	public List<String> getRegStudents(){

		return regStudents;

	}

	public List<String> getPrequisites(){

		return prequisites;

	}

	public void SetAvailableSpots(int availableSpots){/////

		this.availableSpots = availableSpots;

	}

	public void SetRegisteredStudents(int registeredStudents){/////

		this.registered = registeredStudents;

	}

}