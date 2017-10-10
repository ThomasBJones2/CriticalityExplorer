package com.criticalityworkbench.inputobjects;

import com.criticalityworkbench.randcomphandler.*;
import java.util.Random;
import java.util.ArrayList;

public class InputArray implements Input<InputArray>{
	double[] theArray;
	double theSum;
	
	public InputArray(){}

	public InputArray(int n){
		theArray = new double[n];
	}

	public InputArray(double[] inArray){copy(inArray);}

	public static InputArray emptyObject(){return new InputArray();}

	public static InputArray newInputOfSize(int n){return new InputArray(n);}	

	public InputArray newLocInputArray(double[] inArr){return new InputArray(inArr);}

	static public void initialize(){}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}


	public double singleRandomVal(Random rand){
		return Math.abs(-1000000.0 + (2000000.0)*rand.nextDouble());
	}	

	public void randomize(Random rand){
		for(int i = 0; i < theArray.length; i ++){
			theArray[i] = singleRandomVal(rand)/theArray.length;
		}
	}

	public int size(){
		return theArray.length;
	}

	@Randomize
	public double sum(Double val1, Double val2){
		return val1 + val2;
	}

	public double sumRand(Random rand, Double val1, Double val2){
		return val1 + val2;
	}

	public void copy (double[] inArray){
		theArray = new double[inArray.length];
		for(int i = 0; i < inArray.length; i ++){
			theArray[i] = inArray[i];
		}
	}

	public void copy(InputArray inArray){
		theArray = new double[inArray.theArray.length];
		for(int i = 0; i < inArray.theArray.length; i ++){
			theArray[i] = inArray.theArray[i];
		}
	}

	public InputArray copyTenth(int in){
		int dist = theArray.length/10;
		double[] newArray = new double[dist];
		for(int i = dist*in; i < dist*(in + 1); i ++)
			newArray[i - dist*in] = theArray[i];
		return newLocInputArray(newArray);
	}

}
