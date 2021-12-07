package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.NotifyWhenComputerIsReady;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.privateStates.StudentPrivateState;
import bgu.atd.a1.sim.Warehouse;

import java.util.List;
import java.util.LinkedList;

public class CheckAdministrativeObligations extends Action<Void>{

	private String departmentName;
	private List<String> studentsIDs;
	private String computerType;
	private List<String> prerequisites;

	public CheckAdministrativeObligations(String departmentName, List<String> studentsIDs, String computerType, List<String> prerequisites){
		super();
		this.departmentName = departmentName;
		this.studentsIDs = studentsIDs;
		this.computerType = computerType;
		this.prerequisites = prerequisites;

		setActionName("Administrative Check");

	}

	@Override
	protected void start(){

		DepartmentPrivateState departmentPrivateState = (DepartmentPrivateState) (actorThreadPool.getActors()).get(departmentName);

		if(((((Warehouse.GetInstance()).GetComputers()).get(computerType)).GetMutex()).tryAcquire() == true)
			PerformAdministrativeWork(studentsIDs, computerType);
		else{
			List<Action<Void>> dependencies = new LinkedList<>();
			NotifyWhenComputerIsReady notifyWhenComputerIsReady = new NotifyWhenComputerIsReady(computerType);
			dependencies.add(notifyWhenComputerIsReady);
			then(dependencies, () -> {
				PerformAdministrativeWork(studentsIDs, computerType);
			});
			sendMessage(notifyWhenComputerIsReady, departmentName, new DepartmentPrivateState());
		}

		complete(null);

	}

	private void PerformAdministrativeWork(List<String> studentsIDs, String computerType){

		for(String studentID : studentsIDs){
			StudentPrivateState studentPrivateState = (StudentPrivateState) (actorThreadPool.getActors()).get(studentID);
			studentPrivateState.SetSignature((((Warehouse.GetInstance()).GetComputers()).get(computerType)).checkAndSign(prerequisites, studentPrivateState.getGrades()));
		}

		((((Warehouse.GetInstance()).GetComputers()).get(computerType)).GetMutex()).release();

	}
	
}