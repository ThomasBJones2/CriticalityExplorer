package com.criticalityworkbench.inputobjects;

import com.criticalityworkbench.randcomphandler.*;
import java.util.ArrayList;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.util.*;
import java.lang.Math;

public class HalfNaiveMultiply 
	implements Experiment<InputIntegers, HalfNaiveMultiply> {
	BigInteger theProduct;

	HalfNaiveMultiply(){}

	public static HalfNaiveMultiply emptyObject(){return new HalfNaiveMultiply();}

	@Randomize
  public void multiply(InputIntegers xy){
    multiply_helper(xy);
	}

	public void multiplyRand(Random rand, InputIntegers xy){
		  multiply_helper(xy);
		  theProduct = addRand(rand, BigInteger.ZERO, theProduct);
	}

	public void multiply_helper(InputIntegers xy){
		BigInteger x = xy.getX();
		BigInteger y = xy.getY();
		
		BigInteger out = BigInteger.ZERO;
		boolean negative = x.compareTo(BigInteger.ZERO)<0;
		x = x.abs();

		for(int i = 0; i < x.bitLength(); i ++){
			
			if(check(x.testBit(i))){
				//System.out.println(i +  " " + x.bitCount());
				BigInteger curStep = new BigInteger(y.toString());
				for(int j = 0; j < i; j ++){
					curStep = curStep.multiply(new BigInteger("2", 10));
				}
				out = add(out, curStep);
			}
		}
		theProduct = out;		
		if (negative)
			theProduct = BigInteger.ZERO.subtract(theProduct);
	}

	//@Randomize
	public BigInteger add(BigInteger a, BigInteger b){
		BigInteger out = a.add(b);
		return out;
	}

	public BigInteger addRand(Random rand, BigInteger a, BigInteger b){
		BigInteger out = a.add(b);
		if(out.bitLength() > 0){
			out = out.flipBit(Math.abs(rand.nextInt(out.bitLength()/2)));
		}
		return out;
	}

	//@Randomize
	public Boolean check(Boolean a){
		return a;
	}

	public Boolean checkRand(Random rand, Boolean a){
		return !a;
	}


	public void experiment(InputIntegers in){
		multiply(in);
	}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}

	public Score[] scores(HalfNaiveMultiply correctObject){
		Score[] out = new Score [3];
		out[0] = ScorePool.absolutePercentValueBigInteger(this.theProduct, correctObject.theProduct);
		out[1] = ScorePool.absoluteValueBigInteger(this.theProduct, correctObject.theProduct);
		out[2] = ScorePool.logarithmAbsoluteValueBigInteger(this.theProduct, 
				correctObject.theProduct);
		return out; 

	}

	public static void main(String[] args){
		InputIntegers a = new InputIntegers(args[0], args[1]);	
		HalfNaiveMultiply bob = new HalfNaiveMultiply();
		bob.experiment(a);
		System.out.println(a.getX().toString(10) + "X" + 
				a.getY().toString(10) + 
				" = " + bob.theProduct.doubleValue());
	}
}
