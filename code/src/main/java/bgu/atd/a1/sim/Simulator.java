/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.atd.a1.sim;

import bgu.atd.a1.Action;
import bgu.atd.a1.sim.actions.*;
import bgu.atd.a1.sim.Computer;
import bgu.atd.a1.sim.Warehouse;
import bgu.atd.a1.ActorThreadPool;
import bgu.atd.a1.PrivateState;
import bgu.atd.a1.sim.privateStates.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.lang.InterruptedException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator{

	private static String inputFileName;
	public static ActorThreadPool actorThreadPool;
	private static ParseJSONInput parsedObject;
	
	/**
	* Begin the simulation Should not be called before attachActorThreadPool()
	*/
    public static void start(){
			
			attachActorThreadPool(actorThreadPool);

			for(int i = 0; i < (parsedObject.Computers).length; i++){
				Computer computer = new Computer((parsedObject.Computers[i]).Type);
				computer.SetFailSignature((parsedObject.Computers[i]).SigFail);
				computer.SetSuccessSignature((parsedObject.Computers[i]).SigSuccess);
				((Warehouse.GetInstance()).GetComputers()).put((parsedObject.Computers[i]).Type, computer);
			}

			CountDownLatch countDownLatchPhase1 = new CountDownLatch((parsedObject.Phase1).length);

			for(int i = 0; i < (parsedObject.Phase1).length; i++)
				ConvertParsedActionIntoAProperActionObjectAndSubmit(parsedObject.Phase1[i], countDownLatchPhase1);

			actorThreadPool.start();

			try{
				countDownLatchPhase1.await();
			}
			catch(InterruptedException exception){
				System.out.println("Thread was interrupted.");
			}	

			CountDownLatch countDownLatchPhase2 = new CountDownLatch((parsedObject.Phase2).length);

			for(int i = 0; i < (parsedObject.Phase2).length; i++)
				ConvertParsedActionIntoAProperActionObjectAndSubmit(parsedObject.Phase2[i], countDownLatchPhase2);

			try{
				countDownLatchPhase2.await();
			}
			catch(InterruptedException exception){
				System.out.println("Thread was interrupted.");
			}

			CountDownLatch countDownLatchPhase3 = new CountDownLatch((parsedObject.Phase3).length);

			for(int i = 0; i < (parsedObject.Phase3).length; i++)
				ConvertParsedActionIntoAProperActionObjectAndSubmit(parsedObject.Phase3[i], countDownLatchPhase3);

			try{
				countDownLatchPhase3.await();
			}
			catch(InterruptedException exception){
				System.out.println("Thread was interrupted.");
			}

    }

	/**
	* attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	* 
	* @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	*/
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		
		actorThreadPool = myActorThreadPool;

	}
	
	/**
	* shut down the simulation
	* returns list of private states
	*/
	public static HashMap<String,PrivateState> end(){

		try{
			actorThreadPool.shutdown();
		}	
		catch(InterruptedException exception){
			System.out.println("Thread was interrupted.");
		}

		HashMap<String, PrivateState> output = new HashMap<>(actorThreadPool.getActors());

		try{
			FileOutputStream fileOutputStream = new FileOutputStream("result.ser");
			try{
				ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

				objectOutputStream.writeObject(output);
			}
			catch(IOException exception){
			}	
		}
		catch(FileNotFoundException exception){
			System.out.println("Couldn't find the supplied file.");
		}

		return output;

	}
	
	public static void main(String [] args){

		try{
			inputFileName = args[0];
			Gson gson = new Gson();
			JsonReader jsonReader = new JsonReader(new FileReader(inputFileName));
			parsedObject = gson.fromJson(jsonReader, ParseJSONInput.class);
			attachActorThreadPool(new ActorThreadPool(parsedObject.threads));
		}
		catch(FileNotFoundException exception){
			System.out.println("Couldn't find the supplied file.");
		}		

		start();
		
		end();

	}

	private static void ConvertParsedActionIntoAProperActionObjectAndSubmit(ParseAction parsedAction, CountDownLatch countDownLatch){

		Action<?> actionToBeSubmitted;

		switch(parsedAction.Action){
			case "Open Course":
				actionToBeSubmitted = new OpenANewCourse(parsedAction.Department, parsedAction.Course, parsedAction.Space, ConvertStringArrayIntoAList(parsedAction.Prerequisites), countDownLatch);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
				break;
			case "Add Student":
				actionToBeSubmitted = new AddStudent(parsedAction.Department, parsedAction.Student, countDownLatch);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
				break;
			case "Participate In Course":
				actionToBeSubmitted = new ParticipatingInCourse(parsedAction.Student, parsedAction.Course, parsedAction.Grade[0], countDownLatch);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Course, new CoursePrivateState());
				break;
			case "Unregister":
				actionToBeSubmitted = new Unregister(parsedAction.Student, parsedAction.Course, countDownLatch);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Course, new CoursePrivateState());
				break;
			case "Administrative Check":
				actionToBeSubmitted = new CheckAdministrativeObligations(parsedAction.Department, ConvertStringArrayIntoAList(parsedAction.Students), parsedAction.Computer, ConvertStringArrayIntoAList(parsedAction.Conditions), countDownLatch);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
				break;
			case "Close Course":
				actionToBeSubmitted = new CloseACourse(parsedAction.Department, parsedAction.Course, countDownLatch);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
				break;
			case "Add Spaces":
				actionToBeSubmitted = new OpenNewPlacesInACourse(parsedAction.Course, parsedAction.Number, countDownLatch);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Course, new CoursePrivateState());
				break;
			default:
				actionToBeSubmitted = new RegisterWithPreferences(parsedAction.Student, ConvertStringArrayIntoAList(parsedAction.Preferences), ConvertIntegerArrayIntoAList(parsedAction.Grade), countDownLatch);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Student, new StudentPrivateState());
		}

	}

	private static List<String> ConvertStringArrayIntoAList(String[] stringArray){

		List<String> output = new LinkedList<>();

		for(int i = 0; i < stringArray.length; i++)
			output.add(stringArray[i]);

		return output;

	}

	private static List<Integer> ConvertIntegerArrayIntoAList(Integer[] integerArray){

		List<Integer> output = new LinkedList<>();

		for(int i = 0; i < integerArray.length; i++)
			output.add(integerArray[i]);

		return output;

	} 

	private class ParseJSONInput{

		private int threads;

		private ParseComputer[] Computers;

		@SerializedName("Phase 1")
		private ParseAction[] Phase1;

		@SerializedName("Phase 2")
		private ParseAction[] Phase2;

		@SerializedName("Phase 3")
		private ParseAction[] Phase3;

	}

	private class ParseComputer{

		private String Type;

		@SerializedName("Sig Success")
		private long SigSuccess;

		@SerializedName("Sig Fail")
		private long SigFail;

	}

	private class ParseAction{

		private String Action;
		private String Department;
		private String Course;
		private int Space;
		private String[] Prerequisites;
		private String Student;
		private Integer[] Grade;
		private int Number;
		private String[] Preferences;
		private String[] Students;
		private String Computer;
		private String[] Conditions;

	}

}