package HuffManCoding;
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
import RandComp.*;


public class StringOutputStream{
	String theString;
	int charCount;
	StringBuilder sb;
	
	public StringOutputStream(){
		theString = new String();
		sb = new StringBuilder();		
		charCount = 0;
	}

	
	void subWrite(int symbol){
		sb.append((char) symbol);
	}

	public void writeRand(Random rand, int symbol){
		int bit = rand.nextInt(8);
		int mask = 1 << bit;
		symbol &= mask;
		subWrite(symbol);
	}

	@Randomize 
	public void write(int symbol){
		subWrite(symbol);
	}

	public void close(){
		theString = sb.toString();
	}

	public String getOutputString(){
		return theString;
	}

}
