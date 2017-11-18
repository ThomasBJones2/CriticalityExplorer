package com.criticalityworkbench.inputobjects;

import com.criticalityworkbench.randcomphandler.*;
import java.util.ArrayList;
import java.lang.reflect.*;
import java.math.BigInteger;
import java.util.*;
import java.lang.Math;

public class KaratsubaMultiply extends NaiveMultiply {
//	implements Experiment<InputIntegers, KaratsubaMultiply> extends NaiveMultiply{

	KaratsubaMultiply(){}

	public static KaratsubaMultiply emptyObject(){return new KaratsubaMultiply();}

	public void experiment(InputIntegers in){
		multiply(in);
	}

	@Randomize_Decompose
  public void multiply(InputIntegers xy){
    multiply_helper_karatsuba(xy);
	}

	public void multiplyRand(Random rand, InputIntegers xy){
		  multiply_helper_karatsuba(xy);
		  theProduct = addRand(rand, BigInteger.ZERO, theProduct);
	}

	public void multiply_helper_karatsuba(InputIntegers xy){
		BigInteger x = xy.getX();
		BigInteger y = xy.getY();

		int neg1 = x.compareTo(BigInteger.ZERO);
		int neg2 = y.compareTo(BigInteger.ZERO);
		byte[] xbytes = x.abs().toByteArray();
		byte[] ybytes = y.abs().toByteArray();

		int smallerHalfSize = xbytes.length/2;

		//System.out.println("The size is: " + smallerHalfSize);
		//System.out.println("X is: " + x);
		//System.out.println("Y is: " + y);

		if (smallerHalfSize <= 1) {
			super.multiply(xy);

		} else if(x.compareTo(BigInteger.ZERO) == 0 || y.compareTo(BigInteger.ZERO) == 0){
			theProduct = BigInteger.ZERO;
		} else {
			BigInteger power = BigInteger.ONE;

			for(int i = 0; i < smallerHalfSize*8; i ++){
				power = power.multiply(new BigInteger("2", 10));
			}

			BigInteger x1 = new BigInteger(1, grabHalf(xbytes, false, smallerHalfSize));
			BigInteger x2 = new BigInteger(1, grabHalf(xbytes, true, smallerHalfSize));
			//System.out.println("xs: " + x1 + " " + x2);


			BigInteger y1 = new BigInteger(1, grabHalf(ybytes, false, smallerHalfSize));
			BigInteger y2 = new BigInteger(1, grabHalf(ybytes, true, smallerHalfSize));
			//System.out.println("ys: " + y1 + " " + y2);


			InputIntegers xy1 = new InputIntegers(x1,y1);
			InputIntegers xy2 = new InputIntegers(x2,y2);
			InputIntegers xpxypy = new InputIntegers(x1.add(x2), y1.add(y2));
		

			KaratsubaMultiply aMult = new KaratsubaMultiply();
			aMult.multiply(xy1);
			BigInteger a = aMult.theProduct.abs();
			

			KaratsubaMultiply bMult = new KaratsubaMultiply();
			bMult.multiply(xy2);
			BigInteger b = bMult.theProduct.abs();

			KaratsubaMultiply cMult = new KaratsubaMultiply();
			cMult.multiply(xpxypy);
			BigInteger c = cMult.theProduct.abs();
	
			//System.out.println(a + " " + b + " " + c);

			BigInteger k = c.subtract(a).subtract(b);

			//System.out.println(k);

			BigInteger noSigNum = add(add(a.multiply(power.multiply(power)),k.multiply(power)),b);
			
			//System.out.println(noSigNum + " " + power);

			theProduct = new BigInteger(neg1*neg2, noSigNum.toByteArray());

		}
	}

	byte[] grabHalf(byte[] in, boolean smallerHalf, int smallerHalfSize){
		byte[] out = new byte[0];
		if(smallerHalf){
			out = new byte[smallerHalfSize];
			for (int i = 0; i < smallerHalfSize; i ++){
				out[out.length - i - 1] = in[in.length - i - 1];
			}
		} else {
			out = new byte[zeroSafe(in.length - smallerHalfSize)];
			for (int i = 0; i < in.length - smallerHalfSize; i ++){
				out[i] = in[i];
			}
		}
		return out;
	}

	int zeroSafe(int in){
		return in>0 ? in : 0;
	}

	@Randomize
	public BigInteger add(BigInteger a, BigInteger b){
		return super.add(a,b);
	}

	public BigInteger addRand(Random rand, BigInteger a, BigInteger b){
		return super.addRand(rand, a, b);
	}


	@Randomize
	public Boolean check(Boolean a){
		return super.check(a);
	}

	public Boolean checkRand(Random rand, Boolean a){
		return super.checkRand(rand, a);
	}

	public static void main(String[] args){
		com.criticalityworkbench.randcomphandler.RandomMethod.randomize = false;
		InputIntegers a = new InputIntegers(args[0], args[1]);	
		NaiveMultiply bob = new KaratsubaMultiply();
		bob.multiply(a);
		System.out.println(a.getX().toString(10) + "X" + 
				a.getY().toString(10) + 
				" = " + bob.theProduct.doubleValue());
	}

}
