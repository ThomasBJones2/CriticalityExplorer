package RandComp;

import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.*;
import java.io.*;
import java.lang.annotation.Annotation;
import org.reflections.Reflections;
import org.reflections.scanners.*;
public class EconomyExperimenter extends Experimenter{

	EconomyExperimenter(
		int runName, 
		int errorPoint,
		int experimentSize,
		boolean experimentRunning,
		String fallibleMethodName){
		super(runName, 
			errorPoint, 
			experimentSize, 
			experimentRunning, 
			fallibleMethodName);
	}

	EconomyExperimenter(){
		super();
	}

	public static Experimenter emptyObject(){
		System.out.println("Grabbing Economy Experiment...");
		return new EconomyExperimenter();
	}


	@Override
	public void runMain() throws InterruptedException{
		ArrayList<EpsilonProbability> probabilityShapes = getProbabilityShapes();

		for(EpsilonProbability probabilityShape : probabilityShapes){
			setProbabilityShape(probabilityShape);	
			runExperiments(new EconomyExperiment(probabilityShape));
		}
	}

	public void setProbabilityShape(EpsilonProbability probabilityShape){
		RandomMethod.eProbability = probabilityShape;
		RandomMethod.epsilonTest = true;

		System.out.print("Getting Average Error on Epsilon Probability Distribution");
		probabilityShape.printName();	
	}


	public class EconomyExperiment implements ExperimentFunction{
		EpsilonProbability probabilityShape;

		EconomyExperiment(EpsilonProbability probabilityShape){
			this.probabilityShape = probabilityShape;
		}


		public void runExperiment(ArrayBlockingQueue<Runnable> threadQueue, 
				ThreadPoolExecutor thePool, 
				ArrayBlockingQueue<Runnable> checkThreadQueue, 
				ThreadPoolExecutor checkThread,
				int inputSize,
				int loopCount){

			for(double probability = 0; probability <= 0.1; probability += 0.01){
				probabilityShape.setProbability(probability);


				for(int j = 0; j < 1000; j ++){ 
					
					long runName = j + (long)(probability*10000.0*1000.0);
					if(runName % 100 == 0){
						System.out.println("Now on runtime: " + j);
					}
					while(threadQueue.size() >= 8){}
					while(checkThreadQueue.size() >= 8){}
					Experimenter exp = new EconomyExperimenter(
						(int) runName, j, inputSize, true, "All");
					Future theFuture = thePool.submit(exp);
					CheckFuture cf = new CheckFuture(theFuture);
					checkThread.submit(cf);
				}
			}
		}
	}

	static ArrayList<EpsilonProbability> getProbabilityShapes(){
		ArrayList<EpsilonProbability> out = new ArrayList<EpsilonProbability>();
		out.add(new NullEpsilon());
		return out;
	}

}
