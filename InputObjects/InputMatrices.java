package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;

public class InputMatrices implements Input<InputMatrices>{

	Matrix A, B;
	int size;

	public Matrix(){}

	public InputMatrices(int n){
		A = new Matrix(n);
		B = new Matrix(n);
	}

	public InputMatrices(InputMatrices inMatrices){copy(inMatrices);}

	public static InputMatrices emptyObject(){return new InputMatrices();}

	public static InputMatrices newInputOfSize(int n){return new InputMatrices(n);}	

	static public void initialize(){}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}


	public void randomize(Random rand){
		A = A.Randomize(rand);
		B = B.Randomize(rand);
	}

	public int size(){
		return this.size;
	}


	public void copy(InputMatrices inMatrices){
		A = inMatrices.A.copy();
		B = inMatrices.B.copy();
		size = inMatrices.size;
	}

}
