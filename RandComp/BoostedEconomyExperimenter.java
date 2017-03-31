package RandComp;

import java.util.*;
import java.io.*;


public class BoostedEconomyExperimenter extends EconomyExperimenter {
	
	@Override
	ArrayList<EpsilonProbability> getProbabilityShapes(){
		ArrayList<EpsilonProbability> out = new ArrayList<EpsilonProbability>();
		out.add(new BoostedEconomyEpsilon(inputClassName, 
																experimentClassName, 
																experimentTypeName,
																0.0));
		return out;
	}

	BoostedEconomyExperimenter(
		int runName, 
		int errorPoint,
		int experimentSize,
		boolean experimentRunning,
		String fallibleMethodName,
		String scoreName){

			super(runName, 
				errorPoint, 
				experimentSize, 
				experimentRunning, 
				fallibleMethodName,
				scoreName);
	}

	BoostedEconomyExperimenter(){
		super();
	}

	public static Experimenter emptyObject(){
		System.out.println("Grabbing Boosted Economy Experiment...");
		return new BoostedEconomyExperimenter();
	}

	@Override
	BoostedEconomyExperimenter grabThisClass(int runName, 
																			int errorPoint,
																			int experimentSize,
																			boolean experimentRunning,
																			String fallibleMethodName,
																			String scoreName){
		return new BoostedEconomyExperimenter(runName,
															errorPoint,
															experimentSize,
															experimentRunning,
															fallibleMethodName,
															scoreName);
	}




}
