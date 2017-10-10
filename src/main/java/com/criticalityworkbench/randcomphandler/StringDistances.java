package com.criticalityworkbench.randcomphandler;

import java.lang.Math;

public class StringDistances{

	static double symbolDistance(String error, String correct){
		double out = 0;
		if (error == null){
			error = "";
		}
		if (correct == null){
			correct = "";
		}
		for(int i = 0; i < correct.length(); i++){
			if(error.length() <= i || error.charAt(i) != correct.charAt(i)){
				out ++;
			}
		}
		out += Math.max(error.length() - correct.length(), 0);
		return (out/(correct.length()));
	}

	static double manhattanDistance(String error, String correct){
		double out = 0;
		if(error == null){
			error = "";
		}
		if(correct == null){
			correct = "";
		}
		for(int i = 0; i < correct.length(); i++){
			if(error.length() <= i)
				out += 8;
			else {
				for(int j = 0; j < 8; j ++){
					if (((error.charAt(i) >> j) & 1) != ((correct.charAt(i)) & 1))
						out += 1;
				}
			}	
		}
		out += Math.max((error.length() - correct.length())*8, 0);
		return (out/(correct.length()*8));
	}


}
