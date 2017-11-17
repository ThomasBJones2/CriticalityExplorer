package com.criticalityworkbench.randcomphandler;

import java.util.*;
import java.io.*;


public class EconomyExperimenter extends EpsilonExperimenter {
	
	@Override
	ArrayList<EpsilonProbability> getProbabilityShapes(){
		ArrayList<EpsilonProbability> out = new ArrayList<EpsilonProbability>();
		out.add(new EconomyEpsilon(inputClassName, 
																experimentClassName, 
																experimentTypeName,
																processedDataInputDirectory,
                                imageRootDirectory, 
																fallibleMethods,
																0.0));
		return out;
	}

	EconomyExperimenter(
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

	EconomyExperimenter(){
		super();
	}

	public static Experimenter emptyObject(){
		System.out.println("Grabbing Economy Experiment...");
		return new EconomyExperimenter();
	}

	public String[] getScoreStrings(Score[] scores, Score errorScore){

		String[] theScores = new String[1 + (errorScore==null?0:1)];
		for(int i = 0; i < scores.length; i ++){
			if(scores[i].getName().equals(scoreName)){
				theScores[0] = "score: " + scores[i].toString();
			}
		}
		if(errorScore != null)
			theScores[theScores.length - 1] = "score: " + errorScore.toString();
		
		return theScores;
	}


	@Override
	EconomyExperimenter grabThisClass(int runName, 
																			int errorPoint,
																			int experimentSize,
																			boolean experimentRunning,
																			String fallibleMethodName,
																			String scoreName){
		return new EconomyExperimenter(runName,
															errorPoint,
															experimentSize,
															experimentRunning,
															fallibleMethodName,
															scoreName);
	}




}
