package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

public class CloseACourse extends Action<Void>{

	private String departmentName;
	private String courseName;

	public CloseACourse(String departmentName, String courseName){
		
		super();
		this.departmentName = departmentName;
		this.courseName = courseName;

		setActionName("Close Course");

	}

	@Override
	protected void start(){

		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState) (actorThreadPool.getActors()).get(departmentName);
		CoursePrivateState coursePrivateState = (CoursePrivateState) (actorThreadPool.getActors()).get(courseName);

		for(String registeredStudent : coursePrivateState.getRegStudents())
			sendMessage(new Unregister(registeredStudent, courseName), courseName, coursePrivateState);

		(departmentPrivateState.getCourseList()).remove(courseName);

		coursePrivateState.SetAvailableSpots(-1);

		complete(null);

	}
	
}