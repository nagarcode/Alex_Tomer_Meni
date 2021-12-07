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

			for(int i = 0; i < (parsedObject.Phase1).length; i++)
				ConvertParsedActionIntoAProperActionObjectAndSubmit(parsedObject.Phase1[i]);

			for(int i = 0; i < (parsedObject.Phase2).length; i++)
				ConvertParsedActionIntoAProperActionObjectAndSubmit(parsedObject.Phase2[i]);

			for(int i = 0; i < (parsedObject.Phase3).length; i++)
				ConvertParsedActionIntoAProperActionObjectAndSubmit(parsedObject.Phase3[i]);

			actorThreadPool.start();

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
				System.out.println("IO exception encountered.");
			}	
		}
		catch(FileNotFoundException exception){
			System.out.println("Couldn't find the supplied file.");
		}
		System.out.println(output);

		return output;
	}
	
	
	public static void main(String [] args){
		try {
			inputFileName = args[0];
			Gson gson = new Gson();
			JsonReader jsonReader = new JsonReader(new FileReader(inputFileName));
			parsedObject = gson.fromJson(jsonReader, ParseJSONInput.class);
			actorThreadPool = new ActorThreadPool(parsedObject.threads);
		}
		catch(FileNotFoundException exception){
			System.out.println("Couldn't find the supplied file.");
			System.out.println(exception);
		}

		start();
		try { //TODO remove whole block and import
			TimeUnit.SECONDS.sleep(5);
		}
		catch(Exception e ){
			System.out.println(e);
		}
			end();


	}

	private static void ConvertParsedActionIntoAProperActionObjectAndSubmit(ParseAction parsedAction){

		Action<?> actionToBeSubmitted;

		switch(parsedAction.Action){
			case "Open Course":
				actionToBeSubmitted = new OpenANewCourse(parsedAction.Department, parsedAction.Course, parsedAction.Space, ConvertStringArrayIntoAList(parsedAction.Prerequisites));
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
				break;
			case "Add Student":
				actionToBeSubmitted = new AddStudent(parsedAction.Department, parsedAction.Student);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
				break;
			case "Participate In Course":
				actionToBeSubmitted = new ParticipatingInCourse(parsedAction.Student, parsedAction.Course, parsedAction.Grade[0]);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Course, new CoursePrivateState());
				break;
			case "Unregister":
				actionToBeSubmitted = new Unregister(parsedAction.Student, parsedAction.Course);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Course, new CoursePrivateState());
				break;
			case "Administrative Check":
				actionToBeSubmitted = new CheckAdministrativeObligations(parsedAction.Department, ConvertStringArrayIntoAList(parsedAction.Students), parsedAction.Computer, ConvertStringArrayIntoAList(parsedAction.Conditions));
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
				break;
			case "Close Course":
				actionToBeSubmitted = new CloseACourse(parsedAction.Department, parsedAction.Course);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
				break;
			case "Add Spaces":
				actionToBeSubmitted = new OpenNewPlacesInACourse(parsedAction.Course, parsedAction.Number);
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Course, new CoursePrivateState());
				break;
			default:
				actionToBeSubmitted = new RegisterWithPreferences();
				actorThreadPool.submit(actionToBeSubmitted, parsedAction.Department, new DepartmentPrivateState());
		}

	}

	private static List<String> ConvertStringArrayIntoAList(String[] stringArray){

		List<String> output = new LinkedList<>();

		for(int i = 0; i < stringArray.length; i++)
			output.add(stringArray[i]);

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
		private int[] Grade;
		private int Number;
		private String[] Preferences;
		private String[] Students;
		private String Computer;
		private String[] Conditions;

		private void PrintParsedAction(){/////Soon to be deleted...

			System.out.format("Action field is: %s\n", Action);
			System.out.format("Department field is: %s\n", Department);
			System.out.format("Course field is: %s\n", Course);
			System.out.format("Space field is: %d\n", Space);
			if(Prerequisites != null){
				System.out.println("Prerequisites field is:");
			    for(int i = 0; i < Prerequisites.length; i++)
					System.out.format("\t\t%s\n", Prerequisites[i]);
			}	
			System.out.format("Student field is: %s\n", Student);
			if(Grade != null){
				System.out.println("Grade field is:");
			    for(int i = 0; i < Grade.length; i++)
					System.out.format("\t\t%d\n", Grade[i]);
			}	
			System.out.format("Number field is: %d\n", Number);
			if(Preferences != null){
				System.out.println("Preferences field is:");
				for(int i = 0; i < Preferences.length; i++)
					System.out.format("\t\t%s\n", Preferences[i]);
			}
			if(Students != null){	
				System.out.println("Students field is:");
				for(int i = 0; i < Students.length; i++)
					System.out.format("\t\t%s\n", Students[i]);
			}	
			System.out.format("Computer field is: %s", Computer);
			if(Conditions != null){
				System.out.println("Conditions field is:");
				for(int i = 0; i < Conditions.length; i++)
					System.out.format("\t\t%s\n", Conditions[i]);
			}	

		}

	}

}