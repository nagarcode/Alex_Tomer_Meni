package bgu.atd.a1.sim;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Computer{

	String computerType;
	long failSig;
	long successSig;
	Semaphore mutex;
	
	public Computer(String computerType){

		this.computerType = computerType;
		mutex = new Semaphore(1);

	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		
		long output = successSig;

		for(String course : courses){
			if(coursesGrades.get(course) < 56){
				output = failSig;
				break;
			}
		}

		return output;

	}

	public Semaphore GetMutex(){

		return mutex;
		
	}

}