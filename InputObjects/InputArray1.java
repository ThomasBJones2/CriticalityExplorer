package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;

public class InputArray1 extends InputArray{

	public InputArray1(){}

	public InputArray1(int n){
		super(n);	
	}

	public InputArray1(double[] inArray){copy(inArray);}

	public static InputArray emptyObject(){return new InputArray1();}

	public static InputArray newInputOfSize(int n){return new InputArray1(n);}	

	@Override
	public double sumRand(Random rand, Double val1, Double val2){
		double out = (1.0 + (0.2)*rand.nextDouble())*(val1 + val2);
		return out;
	}

	@Override
	public InputArray copyTenth(int in){
		int dist = theArray.length/10;
		double[] newArray = new double[dist];
		for(int i = dist*in; i < dist*(in + 1); i ++)
			newArray[i - dist*in] = theArray[i];
		return newLocInputArray(newArray);
	}

	@Override
	public InputArray newLocInputArray(double[] inArr){return new InputArray1(inArr);}


}
