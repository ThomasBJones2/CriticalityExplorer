package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;

public class InputArray2 extends InputArray{


	public InputArray2(){}

	public InputArray2(int n){
		super(n);
	}

	public InputArray2(double[] inArray){
		copy(inArray);
	}

	public static InputArray emptyObject(){return new InputArray2();}

	public static InputArray newInputOfSize(int n){return new InputArray2(n);}	

	@Override	
	public double sumRand(Random rand, Double val1, Double val2){
		return singleRandomVal(rand);
	}

	@Override
	public InputArray newLocInputArray(double[] inArr){return new InputArray2(inArr);}

	@Override
	public InputArray copyTenth(int in){
		int dist = theArray.length/10;
		double[] newArray = new double[dist];
		for(int i = dist*in; i < dist*(in + 1); i ++)
			newArray[i - dist*in] = theArray[i];
		return newLocInputArray(newArray);
	}



}
