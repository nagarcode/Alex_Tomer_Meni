package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.List;

public class OpenANewCourse extends Action<Void>{

    private String departmentName;
    private String courseName;
    private int space;
    private List<String> prerequisites;

    public OpenANewCourse(String departmentName, String courseName, int space, List<String> prerequisites){
        super();
        this.departmentName = departmentName;
        this.courseName = courseName;
        this.space = space;
        this.prerequisites = prerequisites;

        setActionName("Open Course");

    }

    @Override
    protected void start(){

        (((DepartmentPrivateState) (actorThreadPool.getActors()).get(departmentName)).getCourseList()).add(courseName);

        CoursePrivateState coursePrivateState = new CoursePrivateState();
        coursePrivateState.SetAvailableSpots(space);
        (coursePrivateState.getPrequisites()).addAll(prerequisites);
        
        (actorThreadPool.getActors()).put(courseName, coursePrivateState);

        complete(null);

    }

}