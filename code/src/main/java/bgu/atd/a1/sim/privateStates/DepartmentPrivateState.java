package bgu.atd.a1.sim.privateStates;

import bgu.atd.a1.PrivateState;

import java.util.List;
import java.util.LinkedList;

/**
 * this class describe department's private state
 */
public class DepartmentPrivateState extends PrivateState{

	private List<String> courseList;
	private List<String> studentList;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public DepartmentPrivateState(){
		super();
		courseList = new LinkedList<>();
		studentList = new LinkedList<>();
		
	}

	public List<String> getCourseList(){

		return courseList;

	}

	public List<String> getStudentList(){

		return studentList;

	}
	
}