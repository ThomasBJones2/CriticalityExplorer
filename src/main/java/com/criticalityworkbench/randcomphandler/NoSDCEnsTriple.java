package com.criticalityworkbench.randcomphandler;

import java.util.*;
import java.lang.*;

public class NoSDCEnsTriple extends EnsTriple{
	@Override
	void addScore(Score score){
		//System.out.println("The location is " + location);
		count ++;
	}

	@Override
	void resolve(){}
}
