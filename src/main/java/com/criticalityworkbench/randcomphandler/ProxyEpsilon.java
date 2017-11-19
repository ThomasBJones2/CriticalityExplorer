package com.criticalityworkbench.randcomphandler;


import au.com.bytecode.opencsv.CSVReader;

import java.util.*;
import java.lang.Thread;

public class ProxyEpsilon implements EpsilonProbability{
	HashMap<Integer, DataEnsemble<EnsTriple>> criticalityEnsemble;
	
	double avgProbability;
	double ratioShift;

	int inputSize;
  String proxyMethodName;

	String NAME = "ProxyEpsilon";

	ProxyEpsilon(){}

	ProxyEpsilon(String inputClassName, 
		String experimentClassName, 
		String experimentTypeName,
		String proxyMethodName,
		String rawDataOutputDirectory,
		String imageRootDirectory,
		double ratioShift){

		criticalityEnsemble = new HashMap<>();
		System.out.println("ProxyMethodName in Proxy Epsilon " + proxyMethodName);
    this.proxyMethodName = proxyMethodName;
		System.out.println("this.ProxyMethodName in Proxy Epsilon " + this.proxyMethodName);
		
		int i = 0;
		for(int inputSize : Experimenter.inputSizes){
			System.gc();		
			DataExtractor theExtractor = new DataExtractor(inputClassName, 
																			experimentClassName,
																			Experimenter.criticalityExperimentName,
																			rawDataOutputDirectory,
                                      imageRootDirectory,
																			proxyMethodName);
			theExtractor.use_decompose = true;

			System.out.println("Reading in data");
			theExtractor.readDataIn(inputSize);
			System.out.println("Assembling Data Ensemble");
			DataEnsemble<EnsTriple> nextEnsemble = 
				new DataEnsemble<>(theExtractor.readInLocations, EnsTriple::new );
			nextEnsemble.clearData();
			criticalityEnsemble.put(inputSize, nextEnsemble);
			
			System.out.println("the median is " + criticalityEnsemble.get(inputSize
							).getMedian("Log_Frobenius_Norm", proxyMethodName));
			i++;
			System.gc();		
		}
		upCount = 0;
		downCount = 0;
		this.ratioShift = ratioShift;
	}


	public int getUpCount(){
	  return upCount;
	}

	public int getDownCount(){
	  return downCount;
	}

	public void printCounts(){
		System.out.println("upCount " + upCount + " downCount " + downCount);
	}


  public double getUpDownRatio(){
      return (double)upCount/(double)downCount;
	}

  int upCount;
	int downCount;



	public double getProbability(String scoreName, String methodName, Location location){
		double criticality = criticalityEnsemble.get(inputSize).getCriticality(scoreName, 
																																					this.proxyMethodName, 
																																					location);
		double median = criticalityEnsemble.get(inputSize).getMedian(scoreName, 
				this.proxyMethodName);

    //These lines were to correct for the wrong number of values, however the story
		//in the paper can probably allow for wrong number of values as long as we compare
		//values to each other correctly
		//
		//
		//
		//int upCount = criticalityEnsemble.get(inputSize).getUpCount(scoreName, methodName);
		//int downCount = criticalityEnsemble.get(inputSize).getDownCount(scoreName, methodName);
		//double modifier = downCount==0?1.0:((double)upCount/(double)downCount);
		//median = median*modifier;


		if(criticalityEnsemble.get(inputSize).isValidLocation(scoreName,
																													this.proxyMethodName,
																													location)){

			if (criticality > median) {
				upCount ++;
				//criticalityEnsemble.get(inputSize).setUpCount(scoreName,methodName,upCount);
				
				
				//if(methodName.equals("InputObjects.NaiveMultiply.add"))
			  
				//Random rand = new Random();
		    //if(rand.nextDouble() < 0.001 && avgProbability > 0)
				//	System.out.println(methodName + " " + scoreName + " " + 
				//		DataEnsemble.getCountFromLocation(location, methodName) + " " + 
				//		criticality + " " + median + " " + (avgProbability*ratioShift));
	
				return avgProbability*ratioShift;
			}
			else{
				downCount ++;
				//criticalityEnsemble.get(inputSize).setDownCount(scoreName,methodName,downCount);
			  
				//Random rand = new Random();
		    //if(rand.nextDouble() < 0.001 && avgProbability > 0)
				//	System.out.println(methodName + " " + scoreName + " " + 
				//		DataEnsemble.getCountFromLocation(location, methodName) + " " + 
				//		criticality + " " + median + " " + (avgProbability*(2.0 - ratioShift)));
			
				return avgProbability*(2.0 - ratioShift);
			}
		}


		return avgProbability;
	}

	public void setProbability(double probability){
		this.avgProbability = probability;
		upCount = 0;
		downCount = 0;
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
