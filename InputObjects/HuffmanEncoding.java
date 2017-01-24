package InputObjects;

import RandComp.*;
import HuffManCoding.*;
import java.util.Random;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class HuffmanEncoding 
	implements Experiment<InputString, HuffmanEncoding> {

	String startString;
	String endString;

	public HuffmanEncoding(){}	

	public static HuffmanEncoding emptyObject(){return new HuffmanEncoding();}

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return new ArrayList<DefinedLocation>();
	}

	public void experiment(InputString iString){
		try{
			startString = new String(iString.theString);
			HuffmanCompress hc = new HuffmanCompress();
			HuffmanDecompress hdc = new HuffmanDecompress();

			//AdaptiveHuffmanCompress hc = new AdaptiveHuffmanCompress();
			//AdaptiveHuffmanDecompress hdc = new AdaptiveHuffmanDecompress();
			
			BitOutputStream compressedOut = hc.compress(new StringInputStream(startString));
			//compressedOut.print();
				
			StringOutputStream finalString= hdc.decompress(new BitInputStream(compressedOut));
			endString = finalString.getOutputString();
		} catch(IOException e) {
			System.out.println("To be honest, you should never see this exception");
			System.out.println(e);
		}
	}

	void printStrings(){
		if(startString != null) {
			System.out.println("Start string is: ");
			System.out.println(startString);
		}
		if(endString != null){
			System.out.println("End string is: ");
			System.out.println(endString);
		}
	}

	public Score[] scores(HuffmanEncoding correctObject){
		Score[] out = new Score [2];
		out[0] = ScorePool.symbolDistance(this.endString, correctObject.endString);
		out[0] = ScorePool.manhattanDistance(this.endString, correctObject.endString);
		return out; 

	}


	public static void main(String[] args){
		InputString.initialize();
		InputString inString = new InputString(10);
		inString.randomize(new Random());
		//inString.print();
		HuffmanEncoding he = new HuffmanEncoding();
		he.experiment(inString);
		he.printStrings();
	}
}
