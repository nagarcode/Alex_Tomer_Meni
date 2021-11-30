package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.CoursePrivateState;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;

import java.util.List;

public class NewCourseAction extends Action<Boolean> {

    private String departmentName;
    private String courseName;
    private int space;
    private List<String> prerequisites;

    public NewCourseAction(String departmentName, String courseName, int space, List<String> prerequisites) {
        this.departmentName = departmentName;
        this.courseName = courseName;
        this.space = space;
        this.prerequisites = prerequisites;
    }


    @Override
    protected void start() {
        sendMessage(this, departmentName, new DepartmentPrivateState());
        CoursePrivateState courseState = new CoursePrivateState();
        courseState.getPrequisites().addAll(prerequisites);
        //TODO  send AddSpaces message
    }
}
