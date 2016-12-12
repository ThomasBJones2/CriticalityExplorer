package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;

public class InputArray implements Input<InputArray>{
	double[] theArray;
	double theSum;
	
	public InputArray(){}

	public static InputArray emptyObject(){return new InputArray();}

	public InputArray(int n){
		theArray = new double[n];
	}

	public ArrayList<DefinedDistance> getCurrentDistances(){
		return new ArrayList<DefinedDistance>();
	}


	public static InputArray newInputOfSize(int n){
		return new InputArray(n);
	}	

	double singleRandomVal(Random rand){
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

	public double findSum(){
		theSum = 0;
		for(int i = 0; i < theArray.length; i ++){
			theSum = this.sum(theSum, theArray[i]);
		}
		return theSum;
	}

	@Randomize
	public double sum(Double val1, Double val2){
		return val1 + val2;
	}

	public double sumRand(Random rand, Double val1, Double val2){
		double out = (1.0 + (0.2)*rand.nextDouble())*(val1 + val2);
//		System.out.println(val1 + " " + val2 + 
//				" " + out + " " + ScorePool.AbsolutePercentValue(out, val1 + val2).score); 
		return out;
	}

//	public double sumRand(Random rand, Double val1, Double val2){
//		return singleRandomVal(rand);
//	}

	public void copy(InputArray inArray){
		theArray = new double[inArray.theArray.length];
		for(int i = 0; i < inArray.theArray.length; i ++){
			theArray[i] = inArray.theArray[i];
		}
	}

}
