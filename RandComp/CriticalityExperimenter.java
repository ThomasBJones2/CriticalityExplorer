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

public class CriticalityExperimenter extends Experimenter{

	CriticalityExperimenter(
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


	CriticalityExperimenter(){
		super();
	}

	public static Experimenter emptyObject(){
		System.out.println("Grabbing Criticality Experiment...");
		return new CriticalityExperimenter();
	}
	
	@Override
	public void runMain() throws InterruptedException, IOException{
			RunTimeTriple<Long>[][] rTime = getRunTimes();
			printRunTimes(rTime);
			changeToExperimentTime(rTime);
			printRunTimes(rTime);
			printFallibleMethods();	
			runExperiments(new CriticalityExperiment(rTime));
	}


	public class CriticalityExperiment implements ExperimentFunction{
		RunTimeTriple<Long>[][] rTimes;
	
		CriticalityExperiment(RunTimeTriple<Long>[][] rTimes){
			this.rTimes = rTimes;
		}


		public void runExperiment(ArrayBlockingQueue<Runnable> threadQueue, 
				ThreadPoolExecutor thePool, 
				ArrayBlockingQueue<Runnable> checkThreadQueue, 
				ThreadPoolExecutor checkThread,
				int inputSize,
				int loopCount){

			//System.out.println("there are " + rTimes[loopCount].errorPoints + " errorpoints");
			for(int fallmeth = 0; fallmeth < FallibleMethods.size(); fallmeth ++){
				for(int i = 0; i < rTimes[fallmeth][loopCount].runTime; i ++){
					for(int j = 0; 
							j < Math.min(ERROR_POINTS, rTimes[fallmeth][loopCount].errorPoints); 
							j ++){ 
						//rTimes[loopCount].errorPoints; j ++){
						long runName = 
							fallmeth*rTimes[fallmeth][loopCount].runTime
							*Math.min(ERROR_POINTS, rTimes[fallmeth][loopCount].errorPoints)
							+ i*Math.min(ERROR_POINTS, rTimes[fallmeth][loopCount].errorPoints) 
							+ j;
						if(runName % 
								((rTimes[fallmeth][loopCount].runTime*
									Math.min(ERROR_POINTS, rTimes[fallmeth][loopCount].errorPoints))/100) == 0){
							System.out.println("Now on fallible method: " + 
									FallibleMethods.get(fallmeth) + 
									"runtime: " + i + " and errorPoint " + j);
						}
						while(threadQueue.size() >= 8){}
						while(checkThreadQueue.size() >= 8){}
						if(FallibleMethods.get(fallmeth) == null){
							System.out.println("well the fallmeth: " + fallmeth + "gives us a null method");

						}
						Experimenter exp = new CriticalityExperimenter(
							(int) runName, j, inputSize, true, FallibleMethods.get(fallmeth));
						Future theFuture = thePool.submit(exp);
						CheckFuture cf = new CheckFuture(theFuture);
						checkThread.submit(cf);
						//Thread thread = new Thread(exp);
						//threads.add(thread);
						//thread.start();
					}
				}
			}		
		}

	}



	static void changeToExperimentTime(RunTimeTriple<Long>[][] in){
		for(int i = 0; i < in.length; i ++){
			for(int j = 0; j < in[i].length; j ++){
				System.out.println((in[i][j].runTime*in[i][j].errorPoints));
				if (in[i][j].runTime > 0 && in[i][j].errorPoints>0) {
					in[i][j].runTime = 
						Math.min(24*60*60*ERROR_POINTS/(in[i][j].runTime*ERROR_POINTS), (long)ERROR_POINTS);
						
				} else {
					in[i][j].runTime = 0L;
				}
			}
		}
	}

	static RunTimeTriple<Long>[][] getRunTimes() throws InterruptedException{
		RunTimeTriple<Long>[] nearOut = new RunTimeTriple[3];

		for(int q = 10, c = 0; q <= 1000; q *= 10, c ++){		
			long avgRunTime = System.currentTimeMillis();
			ArrayList<Thread> threads = new ArrayList<>();
			
				for(int i = 0; i < 10; i ++){
					Experimenter exp = new CriticalityExperimenter(i, i, q, false, "All");
					Thread thread = new Thread(exp);
					threads.add(thread);
					thread.start();
				}
				for(Thread t : threads){
					t.join(MAX_RUN_TIME);
					if(t.isAlive()) System.out.println("interupting " + t.toString()); t.interrupt();
				}
				avgRunTime = (System.currentTimeMillis() - avgRunTime)/10;
				nearOut[c] = new RunTimeTriple<Long>(avgRunTime,
						(long)RandomMethod.getAverageTimeCount());
				System.out.println("The average run time is: " + avgRunTime);
				System.out.println("The average error point is: " + RandomMethod.getAverageTimeCount());
				RandomMethod.clearAspect();
			//Thread.sleep(10000);
		}
		RunTimeTriple<Long>[][] out = runTimeBoost(nearOut);

		return out;
	}

	static RunTimeTriple<Long>[][] runTimeBoost(RunTimeTriple<Long>[] rtt){
		RunTimeTriple<Long>[][] out = new RunTimeTriple[FallibleMethods.size()][rtt.length];
		for(int j = 0; j < FallibleMethods.size(); j ++){
			for(int i = 0; i < rtt.length; i ++){
				out[j][i] = new RunTimeTriple(rtt[i]);
				out[j][i].name = FallibleMethods.get(j);
			}
		}
		return out;
	}

	static void printRunTimes(RunTimeTriple<Long>[][] rTime){
				for(int i = 0; i < rTime.length; i++){
					for(int j = 0; j < rTime[i].length; j ++){
						rTime[i][j].print();
					}
				}
	}


}
