package com.criticalityworkbench.randcomphandler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.*;

public class RandCompHandler implements RequestHandler<String, String> 



{
  public String handleRequest(String input, Context context){
    String testInput = "com.criticalityworkbench.inputobjects.InputMatrices com.criticalityworkbench.inputobjects.NaiveMatrixMultiply 0 0 com.criticalityworkbench.randcomphandler.CriticalityExperimenter 1";
		try{
			String out = "";
			if(input.equals("test"))
		    out = Experimenter.handler(testInput);
			else
        out = Experimenter.handler(input);
		  return out;
		} catch (Exception e){
      throw new RuntimeException(e);
		}
	}

}
