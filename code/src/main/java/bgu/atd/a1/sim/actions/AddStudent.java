package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.Actor;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.concurrent.CountDownLatch;

public class AddStudent extends Action<Void>{

	private String departmentName;
	private String studentID;

	public AddStudent(String departmentName, String studentID, CountDownLatch countDownLatch){

		super(countDownLatch);
		this.departmentName = departmentName;
		this.studentID = studentID;

		setActionName("Add Student");
		
	}
	
	@Override
	protected void start(){

		((((DepartmentPrivateState) (actorThreadPool.getActors()).get(departmentName))).getStudentList()).add(studentID);

		(actorThreadPool.getActors()).put(studentID, new StudentPrivateState());

		complete(null);

	}

}