package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.privateStates.DepartmentPrivateState;
import bgu.atd.a1.sim.Warehouse;

import java.util.concurrent.CountDownLatch;

public class NotifyWhenComputerIsReady extends Action<Void>{
	
	private String computerType;
	private String departmentName;

	public NotifyWhenComputerIsReady(String computerType, String departmentName, CountDownLatch countDownLatch){

		super(countDownLatch);
		this.computerType = computerType;
		this.departmentName = departmentName;

		setActionName("Notify When Ready");
		
	}

	@Override
	protected void start(){

		if(((((Warehouse.GetInstance()).GetComputers()).get(computerType)).GetMutex()).tryAcquire() == true)
			complete(null);
		else
			sendMessage(this, departmentName, new DepartmentPrivateState());
		
	}

}