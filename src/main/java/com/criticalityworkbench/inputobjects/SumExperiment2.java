package com.criticalityworkbench.inputobjects;

import java.lang.Class;

public class SumExperiment2 extends SumExperiment{


	public static SumExperiment emptyObject(){return new SumExperiment2();}

	@Override
	void findSum(InputArray in){
		double[] sum10 = new double[10];
		for(int i = 0; i < sum10.length; i ++){
			InputArray in2 = in.copyTenth(i);	
			super.findSum(in2);
			sum10[i] = theSum;
		}
		super.findSum(in.newLocInputArray(sum10));
	}

	

}
