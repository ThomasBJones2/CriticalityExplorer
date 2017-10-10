package com.criticalityworkbench.randcomphandler;

import java.util.*;
import java.lang.*;


public class BoostedEnsTriple extends EnsTriple{

	void resolve(){
		avg = average(dataPoints);
		avgFailCount = average(failCounts);
		avgTimeCount = average(timeCounts);
		stdErr = standardErr();
		avg = avg + stdErr;
		count = dataPoints.size();
		median = median();
		clearData();
	}
}

