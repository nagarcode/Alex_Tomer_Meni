package bgu.atd.a1.sim.privateStates;

import bgu.atd.a1.PrivateState;

import java.util.HashMap;

/**
 * this class describe student private state
 */
public class StudentPrivateState extends PrivateState{

	private HashMap<String, Integer> grades;
	private long signature;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public StudentPrivateState(){

		super();
		grades = new HashMap<>();
		signature = 0;

	}

	public HashMap<String, Integer> getGrades(){

		return grades;

	}

	public long getSignature(){

		return signature;

	}

	public void SetSignature(long signature){

		this.signature = signature;

	}

}