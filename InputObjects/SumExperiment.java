package InputObjects;

import RandComp.*;
import java.util.ArrayList;

public class SumExperiment implements Experiment<InputArray,SumExperiment> {
	double theSum;
	InputArray sumArray;

	SumExperiment(){}

	public static SumExperiment emptyObject(){return new SumExperiment();}

	public void experiment(InputArray in){
		sumArray = in;
		theSum = sumArray.findSum();
	}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return sumArray.getCurrentLocations();
	}

	public Score[] scores(SumExperiment correctObject){
		Score[] out = new Score [2];
		out[0] = ScorePool.AbsolutePercentValue(this.theSum, correctObject.theSum);
		out[1] = ScorePool.AbsoluteValue(this.theSum, correctObject.theSum);
		return out; 

	}
}
