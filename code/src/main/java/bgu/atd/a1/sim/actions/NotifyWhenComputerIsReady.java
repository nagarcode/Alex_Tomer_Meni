package bgu.atd.a1.sim.actions;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.Warehouse;

public class NotifyWhenComputerIsReady extends Action<Void>{
	
	private String computerType;

	public NotifyWhenComputerIsReady(String computerType){

		this.computerType = computerType;

	}

	@Override
	protected void start(){

		while(true){
			if(((((Warehouse.GetInstance()).GetComputers()).get(computerType)).GetMutex()).tryAcquire() == true){
				complete(null);
				break;
			}
		}

	}

}