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

public class CriticalityExperimenter extends Experimenter{

	static double EPSILON_PROBABILITY = 0.0;
	static double RUNTIME_PROBABILITY = 0.01;
	
	CriticalityExperimenter(
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


	CriticalityExperimenter(){
		super();
	}

	public static Experimenter emptyObject(){
		System.out.println("Grabbing Criticality Experiment...");
		return new CriticalityExperimenter();
	}

	@Override
	public void dropZeros(){
			Location.dropZeros = true;
	}

  public String getRunTimeString(){
		RunTimeTriple<Long>[][] rTime = null; 
		try{
			RandomMethod.eProbability.setProbability(RUNTIME_PROBABILITY);
      rTime = getRunTimes();
		} catch(Exception E){
      System.out.println("Exception in getRunTimeString " + E);
		}
		return runTimesToString(rTime);
	}

	@Override
	public void runMain() throws InterruptedException, IOException{
			RandomMethod.eProbability.setProbability(EPSILON_PROBABILITY);
			RunTimeTriple<Long>[][] rTime = rTimeTriple; //getRunTimes();
			printRunTimes(rTime);
			changeToExperimentTime(rTime);
			printRunTimes(rTime);
			printFallibleMethods();	
			runExperiments(new CriticalityExperiment(rTime));
	}


	public class CriticalityExperiment implements ExperimentFunction{
		RunTimeTriple<Long>[][] rTimes;
		int fallmethStart = 0;
		int errorPointStart = 0;	


		CriticalityExperiment(RunTimeTriple<Long>[][] rTimes){
			this.rTimes = rTimes;
		}

		public void translateLastState(String[] result){
			for(String rs: result){
				String[] r = rs.split(" ");
				if(r[0].equals("fallmeth:"))
					this.fallmethStart = fallibleMethods.indexOf(r[1]);
				if(r[0].equals("errorPoint:"))
					this.errorPointStart = Integer.parseInt(r[1]);
			}
			System.out.println("errorPointStart " + 
					this.errorPointStart + 
					" fallmeth " + 
					this.fallmethStart);
		}

		public void resetExperiment(){
			fallmethStart = 0;
			errorPointStart = 0;	
		}

		public void runExperiment(
				int inputSize,
				int inputSizeloopCount) throws InterruptedException{

			int fallmeth = fallmethStart;
			int errorPoint = errorPointStart; 


			String[] state = {"#", "fallmeth: " + fallmeth, "errorPoint: " + errorPoint};
			saveState(state);
			for(int runTime = 0; 
				runTime < rTimes[fallmeth][inputSizeloopCount].runTime; 
				runTime ++){
				long runName = 
					fallmeth*rTimes[fallmeth][inputSizeloopCount].runTime
					*Math.min(NUM_RUNS, rTimes[fallmeth][inputSizeloopCount].errorPoints)
					+ errorPoint*
						Math.min(NUM_RUNS, rTimes[fallmeth][inputSizeloopCount].runTime) 
					+ runTime;
				if(runTime % 100 == 0){
					System.out.println("Now on fallible method: " + 
							fallibleMethods.get(fallmeth) + 
							"runtime: " + runTime + " and errorPoint " + errorPoint);
				}
				if(fallibleMethods.get(fallmeth) == null){
					System.out.println("well the fallmeth: " + fallmeth + "gives us a null method");

				}
				Experimenter exp = new CriticalityExperimenter(
					(int) runName, errorPoint, inputSize, true, 
					fallibleMethods.get(fallmeth),
					"All");
				while(threadQueue.size() >= numThreads*3){}
				theFutures.add(thePool.submit(exp));
			}
		}

	}

	static void changeToExperimentTime(RunTimeTriple<Long>[][] in){
		for(int i = 0; i < in.length; i ++){
			for(int j = 0; j < in[i].length; j ++){
				System.out.println((in[i][j].runTime*in[i][j].errorPoints));
				if (in[i][j].runTime > 0 && in[i][j].errorPoints>0) {
					in[i][j].runTime = 
						Math.min(MAX_RUN_TIME/(in[i][j].runTime*5), (long)NUM_RUNS);
						
				} else {
					in[i][j].runTime = 0L;
				}
			}
		}
	}

	static String runTimesToString(RunTimeTriple<Long>[][] rTime){
		String outString = "";
				for(int i = 0; i < rTime.length; i++){
					for(int j = 0; j < rTime[i].length; j ++){
						outString += rTime[i][j].toString() + "\n";
					}
				}
		return outString;
	}



	static void printRunTimes(RunTimeTriple<Long>[][] rTime){
	    System.out.print(runTimesToString(rTime));
	}


}
