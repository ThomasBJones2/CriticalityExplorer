package com.criticalityworkbench.randcomphandler;

import au.com.bytecode.opencsv.CSVReader;

import java.util.*;
import java.lang.Thread;

public class BoostedEconomyEpsilon extends EconomyEpsilon{


	BoostedEconomyEpsilon(String inputClassName, 
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
				new DataEnsemble<>(theExtractor.readInLocations, BoostedEnsTriple::new );
			nextEnsemble.clearData();
			criticalityEnsemble.put(inputSize, nextEnsemble);
			
			System.out.println("the median is " + criticalityEnsemble.get(inputSize
							).getMedian("Absolute_Logarithm_Value", "InputObjects.NaiveMultiply.check"));
			i++;
			System.gc();		
		}
		this.ratioShift = ratioShift;

		this.NAME = "BoostedEconomyEpsilon";
	}

}
