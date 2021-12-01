package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;

import java.util.List;
import java.util.HashMap;

public class ParticipatingInCourse extends Action<Boolean>{

	private String studentID;
	private String courseName;
	private Integer grade;

	public ParticipatingInCourse(String studentID, String courseName, Integer grade){

		this.studentID = studentID;
		this.courseName = courseName;
		this.grade = grade;

		setActionName("Participate In Course");

	}

	@Override
	protected void start(){

		CoursePrivateState coursePrivateState = (CoursePrivateState) ((actorThreadPool.getActors()).get(courseName));
		StudentPrivateState studentPrivateState = (StudentPrivateState) ((actorThreadPool.getActors()).get(studentID));

		if(VerifyAvailableSpots(coursePrivateState) && VerifyPrerequisites(coursePrivateState.getPrequisites(), studentPrivateState.getGrades())){
			(coursePrivateState.getRegStudents()).add(studentID);
			coursePrivateState.SetRegisteredStudents((coursePrivateState.getRegistered()).intValue() + 1);
			(studentPrivateState.getGrades()).put(courseName, grade);
			complete(true);
		}
		else
			complete(false);

	}

	private boolean VerifyAvailableSpots(CoursePrivateState coursePrivateState){///Might want to revise

		boolean output = false;

		if((coursePrivateState.getRegistered()).intValue() < (coursePrivateState.getAvailableSpots()).intValue())
			output = true;

		return output;

	}

	private boolean VerifyPrerequisites(List<String> prerequisites, HashMap<String, Integer> grades){

		boolean output = true;

		for(String prerequisite : prerequisites){
			if(output == false)
				break;
			if((grades.get(prerequisite)).intValue() < 56)
				output = false;
		}

		return output;
			
	}
	
}