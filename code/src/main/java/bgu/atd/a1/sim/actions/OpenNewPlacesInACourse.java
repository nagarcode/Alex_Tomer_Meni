package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;

public class OpenNewPlacesInACourse extends Action<Void>{

	private String courseName;
	private int places;

	public OpenNewPlacesInACourse(String courseName, int places){
		super();
		this.courseName = courseName;
		this.places = places;

		setActionName("Add Spaces");

	}
	
	@Override
	protected void start(){

		((CoursePrivateState) (actorThreadPool.getActors()).get(courseName)).SetAvailableSpots((((CoursePrivateState) (actorThreadPool.getActors()).get(courseName)).getAvailableSpots()).intValue() + places);

		complete(null);

	}
	
}