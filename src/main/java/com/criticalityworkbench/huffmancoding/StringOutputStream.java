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
import java.lang.StringBuilder;
import java.util.Random;
import java.util.ArrayList;
import com.criticalityworkbench.randcomphandler.*;


public class StringOutputStream implements AbstractLocation{
	String theString;
	int charCount;
	StringBuilder sb;
	
	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}
	
	public StringOutputStream(){
		theString = new String();
		sb = new StringBuilder();		
		charCount = 0;
	}

	
	void subWrite(int symbol){
		sb.append((char) symbol);
	}

	public void writeRand(Random rand, Integer symbol){
		int bit = rand.nextInt(8);
		int mask = 1 << bit;
		symbol ^= mask;
		subWrite(symbol);
	}

	@Randomize 
	public void write(Integer symbol){
		subWrite(symbol);
	}

	public void close(){
		theString = sb.toString();
	}

	public String getOutputString(){
		return theString;
	}

}
