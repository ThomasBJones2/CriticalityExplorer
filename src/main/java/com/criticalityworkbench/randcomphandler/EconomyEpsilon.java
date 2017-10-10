package com.criticalityworkbench.randcomphandler;


import au.com.bytecode.opencsv.CSVReader;

import java.util.*;
import java.lang.Thread;

public class EconomyEpsilon implements EpsilonProbability{
	HashMap<Integer, DataEnsemble<EnsTriple>> criticalityEnsemble;
	
	double avgProbability;
	double ratioShift;

	int inputSize;

	String NAME = "EconomyEpsilon";

	EconomyEpsilon(){}

	EconomyEpsilon(String inputClassName, 
		String experimentClassName, 
		String experimentTypeName,
		double ratioShift){

		criticalityEnsemble = new HashMap<>();

		int i = 0;
		for(int inputSize : Experimenter.inputSizes){
			System.gc();		
			DataExtractor theExtractor = new DataExtractor(inputClassName, 
																			experimentClassName,
																			Experimenter.criticalityExperimentName);
			System.out.println("Reading in data");
			theExtractor.readDataIn(inputSize);
			System.out.println("Assembling Data Ensemble");
			DataEnsemble<EnsTriple> nextEnsemble = 
				new DataEnsemble<>(theExtractor.readInLocations, EnsTriple::new );
			nextEnsemble.clearData();
			criticalityEnsemble.put(inputSize, nextEnsemble);
			
			System.out.println("the median is " + criticalityEnsemble.get(inputSize
							).getMedian("Absolute_Logarithm_Value", "InputObjects.NaiveMultiply.check"));
			i++;
			System.gc();		
		}
		this.ratioShift = ratioShift;
	}



	public int getUpCount(){
		return criticalityEnsemble.get(inputSize).getUpCountSum();
	}

	public int getDownCount(){
		return criticalityEnsemble.get(inputSize).getDownCountSum();
	}

	public void printCounts(){
		criticalityEnsemble.get(inputSize).printCounts();
	}

	
	public double getProbability(String scoreName, String methodName, Location location){
		double criticality = criticalityEnsemble.get(inputSize).getCriticality(scoreName, 
																																					methodName, 
																																					location);
		double median = criticalityEnsemble.get(inputSize).getMedian(scoreName, methodName);


		int upCount = criticalityEnsemble.get(inputSize).getUpCount(scoreName, methodName);
		int downCount = criticalityEnsemble.get(inputSize).getDownCount(scoreName, methodName);
		double modifier = downCount==0?1.0:((double)upCount/(double)downCount);
		median = median*modifier;


		if(criticalityEnsemble.get(inputSize).isValidLocation(scoreName,
																													methodName,
																													location)){

			if (criticality > median) {
				upCount ++;
				criticalityEnsemble.get(inputSize).setUpCount(scoreName,methodName,upCount);
				//if(methodName.equals("InputObjects.NaiveMultiply.add"))
			  //System.out.println(methodName + " " + scoreName + " " + 
						//DataEnsemble.getCountFromLocation(location, methodName) + " " + 
						//criticality + " " + median + " " + modifier);
				return avgProbability*ratioShift;
			}
			else{
				downCount ++;
				criticalityEnsemble.get(inputSize).setDownCount(scoreName,methodName,downCount);
				return avgProbability*(2.0 - ratioShift);
			}
		}

		return avgProbability;
	}

	public void setProbability(double probability){
		this.avgProbability = probability;
	}

	public int indexOf(int[] array, int val){
		for(int i = 0; i < array.length; i ++){
			if(array[i] == val)
				return i;
		}
		return -1;
	}
	
	public void setInputSize(int inputSize){
		this.inputSize = inputSize;
	}

	public double getLocation(){
		return this.avgProbability;
	}

	public String getName(){
		return NAME;
	}

	public void printName(){
		System.out.println(NAME);
	}

}
