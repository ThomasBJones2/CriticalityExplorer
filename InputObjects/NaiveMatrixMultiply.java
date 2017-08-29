package InputObjects;

import RandComp.*;
import java.util.ArrayList;
import java.lang.reflect.*;

public class NaiveMatrixMultiply
	implements Experiment<InputArray, NaiveMatrixMultiply> {
	Matrix output;
	InputMatrices inputs;

	SumExperiment(){}

	public static NaiveMatrixMultiply emptyObject(){return new NaiveMatrixMultiply();}

	public void experiment(InputMatrix in){
		inputs = in;
		naiveMultiply(in);
	}

	public void navieMultiply(InputMatrices input){
		output = new Matrix(input.size);
		for(int i = 0; i < input.size; i ++){
			for(int j = 0; j < input.size; j ++){
				output[i][j] = BigInteger.Zero();
				for(int k = 0; k < input.size; k ++){
					NaiveMultiply mult = NaiveMultiply.emptyObject()
					mult.multiply(InputIntegers(inputs.A.values[i][k], inputs.B.values[k][j]))
					output[i][j]  = ouput[i][j].add(mult.theProduct);
				}
			}
		}
	}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return Matrix.getCurrentLocations();
	}

	public Score[] scores(SumExperiment correctObject){
		Score[] out = new Score [2];
		out[0] = ScorePool.MatrixScore1(this.output, correctObject.output);
		out[1] = ScorePool.absoluteValue(this.output, correctObject.output);
		return out; 

	}
}
