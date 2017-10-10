package com.criticalityworkbench.huffmancoding;
/* 
 * Reference Huffman coding
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/reference-huffman-coding
 * https://github.com/nayuki/Reference-Huffman-coding
 */

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.ArrayList;
import com.criticalityworkbench.randcomphandler.*;

public class StringInputStream implements AbstractLocation{
	String theString;
	int charCount;
	
	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}
	
	public StringInputStream(String inString){
		theString = new String(inString);
		charCount = 0;
	}

	public void print(){
		System.out.println("Printing a StringInputStream");
		System.out.println(theString);
	}


	int subRead(){
		int out = 0;
		if(charCount < theString.length()){
			out = (int)theString.charAt(charCount);
			charCount ++;
		} else {
			out = -1;
		}
		return out;	
	}

	@Randomize
	public int read(){
		return subRead();	
	}

	public int readRand(Random rand){
		if (subRead() == -1)
			return -1;
		return (subRead() ^ 1) & 1;
	}

	public void reset(){
		charCount = 0;
	}

	public void close(){}



}
