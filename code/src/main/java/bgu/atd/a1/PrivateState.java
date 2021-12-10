package bgu.atd.a1;

import java.util.LinkedList;
import java.util.List;

/**
 * an abstract class that represents private states of an actor
 * it holds actions that the actor has executed so far 
 * IMPORTANT: You can not add any field to this class.
 */
public abstract class PrivateState{
	
	// holds the actions' name what were executed
	private final List<String> history;

	public PrivateState(){

		history = new LinkedList<>();

	}

	public List<String> getLogger(){

		return history;

	}

	/**
	 * add an action to the records
	 *  
	 * @param actionName
	 */
	public void addRecord(String actionName){
		
		history.add(actionName);

	}

	public String toString(){

		StringBuilder out = new StringBuilder();
		for (String entry: history) {
		out.append(entry).append(", ");
		}
		return out.toString().substring(0,out.length()-2);
		
	}
	
}