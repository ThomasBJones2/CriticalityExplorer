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

public interface ExperimentFunction{
	public void runExperiment(ArrayBlockingQueue<Runnable> threadQueue, 
			ThreadPoolExecutor thePool, 
			ArrayBlockingQueue<Runnable> checkThreadQueue, 
			ThreadPoolExecutor checkThread,
			int inputSize,
			int loopCount);
}
