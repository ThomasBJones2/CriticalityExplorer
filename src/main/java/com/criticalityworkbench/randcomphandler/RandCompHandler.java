package com.criticalityworkbench.randcomphandler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.io.*;

public class RandCompHandler implements RequestHandler<String, String> 



{
  public String handleRequest(String input, Context context){
		try{
		  String out = Experimenter.handler(input);
		  return out;
		} catch (Exception e){
      throw new RuntimeException(e);
		}
	}

}
