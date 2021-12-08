package bgu.atd.a1.sim;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class Computer{

	private String computerType;
	private long failSig;
	private long successSig;
	private Semaphore mutex;
	
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
			if(coursesGrades.get(course) == null || coursesGrades.get(course) < 56){
				output = failSig;
				break;
			}
		}

		return output;

	}

	public Semaphore GetMutex(){

		return mutex;

	}

	public void SetFailSignature(long failSignature){

		failSig = failSignature;

	}

	public void SetSuccessSignature(long successSignature){

		successSig = successSignature;

	}

	public void PrintComputer(){/////Remove this once done testing.

		System.out.format("The value of computerType is: %s\n", computerType);
		System.out.format("The value of failSig is: %ld\n", failSig);
		System.out.format("The value of successSig is: %ld\n", successSig);

	}

}