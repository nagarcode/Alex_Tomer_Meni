package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

public class Unregister extends Action<Boolean>{

	private String studentID;
	private String courseName;

	public Unregister(String studentID, String courseName){

		this.studentID = studentID;
		this.courseName = courseName;

		setActionName("Unregister");

	}

	@Override
	protected void start(){

		CoursePrivateState coursePrivateState = (CoursePrivateState) (actorThreadPool.getActors()).get(courseName);
		StudentPrivateState studentPrivateState = (StudentPrivateState) (actorThreadPool.getActors()).get(studentID);

		if((coursePrivateState.getRegStudents()).contains(studentID)){
			(coursePrivateState.getRegStudents()).remove(studentID);
			coursePrivateState.SetRegisteredStudents((coursePrivateState.getRegistered()).intValue() - 1);
			(studentPrivateState.getGrades()).remove(courseName);
			complete(true);
		}
		else 
			complete(false);

	}
	
}