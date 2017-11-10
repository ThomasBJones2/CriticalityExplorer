package com.criticalityworkbench.randcomphandler;

import au.com.bytecode.opencsv.CSVReader;

import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.*;
import java.io.*;
import java.lang.annotation.Annotation;
import org.reflections.Reflections;
import org.reflections.scanners.*;

public class EpsilonExperimenter extends Experimenter{

	EpsilonExperimenter(
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

	EpsilonExperimenter(){
		super();
	}

	public static Experimenter emptyObject(){
		System.out.println("Grabbing Epsilon Experiment...");
		return new EpsilonExperimenter();
	}

	@Override
	public void dropZeros(){
			Location.dropZeros = false;
	}

	@Override
	public void runMain() throws InterruptedException, IOException{
		getRunTimes();

		ArrayList<EpsilonProbability> probabilityShapes = getProbabilityShapes();

		for(EpsilonProbability probabilityShape : probabilityShapes){
			setProbabilityShape(probabilityShape);	
			runExperiments(new EpsilonExperiment(probabilityShape));
		}
		System.out.println("Now Done with Epsilon Experimenter Experiments");
	}

	public void setProbabilityShape(EpsilonProbability probabilityShape){
		RandomMethod.eProbability = probabilityShape;
		RandomMethod.epsilonTest = true;

		System.out.print("Getting Average Error on Epsilon Probability Distribution ");
		probabilityShape.printName();	
	}


	EpsilonExperimenter grabThisClass(int runName, 
																			int errorPoint,
																			int experimentSize,
																			boolean experimentRunning,
																			String fallibleMethodName,
																			String scoreName){

		return new EpsilonExperimenter(runName,
																	errorPoint,
																	experimentSize,
																	experimentRunning,
																	fallibleMethodName,
																	scoreName);
	}

	public class EpsilonExperiment implements ExperimentFunction{
		EpsilonProbability probabilityShape;

		double startProbability = 0.0;

		EpsilonExperiment(EpsilonProbability probabilityShape){
			this.probabilityShape = probabilityShape;
		}

		public void translateLastState(String[] result){
			for(String rs: result){
				String[] r = rs.split(" ");
				if(r[0].equals("probability:"))
					this.startProbability = Double.parseDouble(r[1]);
			}
		}

		public void resetExperiment(){
			this.startProbability = 0;
		}

		ArrayList<String> extractScoreNames(Experiment experimentClass){
			Score[] scores = experimentClass.scores(experimentClass);
			ArrayList<String> scoreNames = new ArrayList<>();
			for(Score score : scores){
				scoreNames.add(score.getName());
			}
			return scoreNames;
		}

		public void runExperiment(
				int inputSize,
				int loopCount) throws InterruptedException{
			probabilityShape.setInputSize(inputSize);


			Experiment experimentClass = (Experiment)getNewObject(experimentClassName);			
			ArrayList<String> scoreNames = extractScoreNames(experimentClass);

			System.out.println("Found Score Names");
			for(String scoreName : scoreNames){
				System.out.println(scoreName);
			}

			for(String scoreName : scoreNames) {
				for(double probability = startProbability; probability <= 0.1; probability += 0.01){
				  System.out.println("On scoreName: " + scoreName + " and probability " + probability);
					resetThreading();


					probabilityShape.setProbability(probability);
					String[] state = {"#", "probability: " + probability};
					saveState(state);

					for(int runTime = 0; runTime < 1000; runTime ++){ 
					
						long runName = runTime + (long)(probability*10000.0*1000.0);
						if(runName % 100 == 0){
							System.out.println("Now on runtime: " + runTime);
							probabilityShape.printCounts();
						}


						while(threadQueue.size() >= numThreads*3){}
						Experimenter exp = grabThisClass(
							(int) runName, runTime, inputSize, true, "All", scoreName);
						theFutures.add(thePool.submit(exp));
					}
				}
			}
		}
	}

	ArrayList<EpsilonProbability> getProbabilityShapes(){
		ArrayList<EpsilonProbability> out = new ArrayList<EpsilonProbability>();
		out.add(new NullEpsilon());
		return out;
	}

}
