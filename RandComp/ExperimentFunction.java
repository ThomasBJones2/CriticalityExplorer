package RandComp;

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

public interface ExperimentFunction{

	public void runExperiment(ArrayBlockingQueue<Runnable> threadQueue, 
			ThreadPoolExecutor thePool, 
			ArrayBlockingQueue<Runnable> checkThreadQueue, 
			ThreadPoolExecutor checkThread,
			int inputSize,
			int loopCount);

		public void translateLastState(String[] result);

		default public String[] getLastState(List<String[]> results){
			String[] out = null;

			for(String[] result : results){
				if (result[0].equals("#"))
					out = result;
			}
			return out;
		}

		default public void readResultsAndResetExperiment(CSVReader inputReader){
			try{
				List<String[]> results = inputReader.readAll();
				String[] lastState = getLastState(results);
				if (lastState != null) {
					translateLastState(lastState);
				}

			} catch (IOException e) {
				System.out.println("No results stored, starting from the beginning");
			}
		}
}
