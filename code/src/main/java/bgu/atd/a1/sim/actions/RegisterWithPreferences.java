package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.ParticipatingInCourse;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;

import java.lang.InterruptedException;

public class RegisterWithPreferences extends Action<Void>{

	private String studentID;
	private List<String> preferences;
	private List<Integer> grades; 

	public RegisterWithPreferences(String studentID, List<String> preferences, List<Integer> grades){

		this.studentID = studentID;
		this.preferences = preferences;
		this.grades = grades;

		setActionName("Register With Preferences");

	}

	@Override
	protected void start(){

		int i = -1;

		for(String preference : preferences){

			if((getResult()).isResolved())
				break;

			i += 1;
			ParticipatingInCourse participatingInCourse = new ParticipatingInCourse(studentID, preference, grades.get(i));
			List<ParticipatingInCourse> dependencies = new LinkedList<>();
			dependencies.add(participatingInCourse);

			CountDownLatch countDownLatch = new CountDownLatch(1);

			then(dependencies, () -> {
				if((participatingInCourse.getResult()).get())
					complete(null);
				countDownLatch.countDown();
			});

			sendMessage(participatingInCourse, preference, new CoursePrivateState());

			try{
				countDownLatch.await();
			}
			catch(InterruptedException exception){
				System.out.println("Thread was interrupted.");
			}	

		}

		if(!(getResult()).isResolved())
			complete(null);

	}
	
}