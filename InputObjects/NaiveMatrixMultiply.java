package InputObjects;

import RandComp.*;
import java.util.ArrayList;
import java.lang.reflect.*;
import java.math.BigInteger;

public class NaiveMatrixMultiply
	implements Experiment<InputMatrices, NaiveMatrixMultiply> {
	Matrix output;
	InputMatrices inputs;

	NaiveMatrixMultiply(){}

	public static NaiveMatrixMultiply emptyObject(){return new NaiveMatrixMultiply();}

	public void experiment(InputMatrices in){
		inputs = in;
		naiveMultiply(in);
	}

	public void naiveMultiply(InputMatrices input){
		output = new Matrix(input.size);
		for(int i = 0; i < input.size; i ++){
			for(int j = 0; j < input.size; j ++){
				output.values[i][j] = BigInteger.ZERO;
				for(int k = 0; k < input.size; k ++){
					NaiveMultiply mult = NaiveMultiply.emptyObject();
					mult.multiply(new InputIntegers(inputs.A.values[i][k], inputs.B.values[k][j]));
					output.values[i][j] = output.values[i][j].add(mult.theProduct);
				}
			}
		}
	}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return output.getCurrentLocations();
	}

	public Score[] scores(NaiveMatrixMultiply correctObject){
		Score[] out = new Score [2];
		out[0] = ScorePool.frobeniusNorm(this.output, correctObject.output);
		out[1] = ScorePool.infinityNorm(this.output, correctObject.output);
		return out; 

	}
}
