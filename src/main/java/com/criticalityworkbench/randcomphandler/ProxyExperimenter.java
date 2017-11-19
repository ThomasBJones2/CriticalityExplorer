package com.criticalityworkbench.randcomphandler;

import java.util.*;
import java.io.*;


public class ProxyExperimenter extends EconomyExperimenter {
	
	@Override
	ArrayList<EpsilonProbability> getProbabilityShapes(){
		ArrayList<EpsilonProbability> out = new ArrayList<EpsilonProbability>();
		out.add(new ProxyEpsilon(inputClassName, 
																experimentClassName, 
																experimentTypeName,
																proxyMethodName, 
																processedDataInputDirectory,
                                imageRootDirectory, 
																0.0));
		return out;
	}


	@Override
	public void runMain() throws InterruptedException, IOException{
		getRunTimes();
		in_proxy_experiment = true;

		RandomMethod.useProxyMethod = true;
		RandomMethod.proxyMethodName = proxyMethodName;

		ArrayList<EpsilonProbability> probabilityShapes = getProbabilityShapes();

		for(EpsilonProbability probabilityShape : probabilityShapes){
			setProbabilityShape(probabilityShape);	
			runExperiments(new EpsilonExperiment(probabilityShape));
		}
		System.out.println("Now Done with Proxy Experimenter Experiments");
	}



	ProxyExperimenter(
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

	ProxyExperimenter(){
		super();
	}

	public static Experimenter emptyObject(){
		System.out.println("Grabbing Proxy Experiment...");
		return new ProxyExperimenter();
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
	ProxyExperimenter grabThisClass(int runName, 
																			int errorPoint,
																			int experimentSize,
																			boolean experimentRunning,
																			String fallibleMethodName,
																			String scoreName){
		return new ProxyExperimenter(runName,
															errorPoint,
															experimentSize,
															experimentRunning,
															fallibleMethodName,
															scoreName);
	}




}
