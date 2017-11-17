package com.criticalityworkbench.randcomphandler;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.*;
import java.io.*;
import java.lang.annotation.Annotation;
import org.reflections.Reflections;
import org.reflections.scanners.*;

public interface ExperimentFunction{

	public void runExperiment(
			int inputSize,
			int loopCount) throws InterruptedException;

		public void translateLastState(String[] result);
		public void resetExperiment();
    
		default public void setOutputWriter(CSVWriter outputWriter){}


		default public String[] getLastState(List<String[]> results){
			String[] out = null;

			for(String[] result : results){
				if (result[0].equals("#"))
					out = result;
			}
			return out;
		}

		default public void readResultsAndResetExperiment(CSVReader inputReader){		
			resetExperiment();
			try{
				
				List<String[]> results = inputReader.readAll();
				
				String[] lastState = getLastState(results);
				if (lastState != null) {
					for(String state : lastState)
							System.out.println("state in final state: " + state);

					translateLastState(lastState);
				}

			} catch (IOException e) {
				System.out.println("No results stored, starting from the beginning");
			}
		}
}
