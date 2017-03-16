package RandComp;

import java.util.*;
import java.io.*;


public class EconomyExperimenter extends EpsilonExperimenter {
	
	static ArrayList<EpsilonProbability> getProbabilityShapes(){
		ArrayList<EpsilonProbability> out = new ArrayList<EpsilonProbability>();
		out.add(new EconomyEpsilon(inputClassName, 
																experimentClassName, 
																experimentTypeName,
																1.0));
		return out;
	}

	EconomyExperimenter(
		int runName, 
		int errorPoint,
		int experimentSize,
		boolean experimentRunning,
		String fallibleMethodName){

			super(runName, 
				errorPoint, 
				experimentSize, 
				experimentRunning, 
				fallibleMethodName);
	}

	EconomyExperimenter(){
		super();
	}


	@Override
	EconomyExperimenter grabThisClass(int runName, 
																			int errorPoint,
																			int experimentSize,
																			boolean experimentRunning,
																			String fallibleMethodName){
		return new EconomyExperimenter(runName,
															errorPoint,
															experimentSize,
															experimentRunning,
															fallibleMethodName);
	}




}
