package InputObjects;

import RandComp.*;
import java.util.Random;
import java.util.ArrayList;
import java.math.BigInteger;


public class InputIntegers implements Input<InputIntegers>{
	BigInteger x;
	BigInteger y;
	int size;

	public InputIntegers(){}

	public InputIntegers(String inX, String inY){
		x = new BigInteger(inX, 10);
		y = new BigInteger(inY, 10);

	}

	public InputIntegers(BigInteger a, BigInteger b){
		x = new BigInteger(a.toString(10), 10);
		y = new BigInteger(b.toString(10), 10);
	}

	public InputIntegers(int n){
		size = n;
	}

	public InputIntegers(InputIntegers inIntegers){copy(inIntegers);}

	public static InputIntegers emptyObject(){return new InputIntegers();}

	public static InputIntegers newInputOfSize(int n){return new InputIntegers(n);}	

	static public void initialize(){}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}


	public void randomize(Random rand){
		x = new BigInteger(size, rand);
		y = new BigInteger(size, rand);
	}

	public void copy(InputIntegers inIntegers){
		x = new BigInteger(inIntegers.x.toString(10), 10);
		y = new BigInteger(inIntegers.y.toString(10), 10);
	}

	public BigInteger getX(){
		return new BigInteger(x.toString(10), 10);
	}

	public BigInteger getY(){
		return new BigInteger(y.toString(10), 10);
	}

}
