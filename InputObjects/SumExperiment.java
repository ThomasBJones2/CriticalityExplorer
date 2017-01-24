package InputObjects;

import RandComp.*;
import java.util.ArrayList;
import java.lang.reflect.*;

public class SumExperiment 
	implements Experiment<InputArray, SumExperiment> {
	double theSum;
	InputArray sumArray;

	SumExperiment(){}

	public static SumExperiment emptyObject(){return new SumExperiment();}

	void findSum(InputArray in){
		theSum = 0;
		for(int i = 0; i < in.theArray.length; i ++){
			theSum = in.sum(theSum, in.theArray[i]);
		}
	}

	public void experiment(InputArray in){
		sumArray = in;
		findSum(in);
	}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return sumArray.getCurrentLocations();
	}

	public Score[] scores(SumExperiment correctObject){
		Score[] out = new Score [2];
		out[0] = ScorePool.absolutePercentValue(this.theSum, correctObject.theSum);
		out[1] = ScorePool.absoluteValue(this.theSum, correctObject.theSum);
		return out; 

	}
}
