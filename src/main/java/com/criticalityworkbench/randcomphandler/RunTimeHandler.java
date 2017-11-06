package com.criticalityworkbench.randcomphandler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.*;

public class RunTimeHandler implements RequestHandler<String, String> 



{
  public String handleRequest(String input, Context context){
    String testInput = "runTime com.criticalityworkbench.inputobjects.InputMatrices com.criticalityworkbench.inputobjects.NaiveMatrixMultiply com.criticalityworkbench.randcomphandler.CriticalityExperimenter 1";
		try{
			String out = "";
			if(input.equals("test"))
		    out = Experimenter.runTimeHandler(testInput);
			else
        out = Experimenter.runTimeHandler(input);
		  return out;
		} catch (Exception e){
      throw new RuntimeException(e);
		}
	}

}
