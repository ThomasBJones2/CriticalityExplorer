package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;

public class InputString implements Input<InputString>{
	
	String theString;
	int length;

	public static Dictionary dict;

	public static void initialize(){
		System.out.println("Initializing InputString");
		dict = new Dictionary();
	}

	public InputString(){
		length = 0;
	}

	public void print(){
		System.out.println(theString);
	}

	public InputString(int n){
		length = n;
	}

	public void randomize(Random rand){
		theString = "";
		for(int i = 0; i < length; i ++){
			theString += dict.getRandomWord(rand) + " ";
		}
	}

	public void copy(InputString in){
		theString = new String(in.theString);
	}

	public static InputString emptyObject(){return new InputString();} 
	public static InputString newInputOfSize(int n){return new InputString(n);}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}

	public static void main(String[] args){
		InputString.dict = new Dictionary();
		InputString iString = new InputString(10);
		iString.randomize(new Random());
		System.out.println(iString.theString);
	}
}
