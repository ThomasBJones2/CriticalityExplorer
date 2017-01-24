package HuffManCoding;
/* 
 * Reference Huffman coding
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/reference-huffman-coding
 * https://github.com/nayuki/Reference-Huffman-coding
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import RandComp.*;

/**
 * A stream of bits that can be read. Because they come from an underlying byte stream,
 * the total number of bits is always a multiple of 8. The bits are read in big endian.
 * Mutable and not thread-safe.
 * @see BitOutputStream
 */
public final class BitInputStream {
	
	/* Fields */

	
	// Either in the range [0x00, 0xFF] if bits are available, or -1 if end of stream is reached.
	private int currentByte;
	
	// Number of remaining bits in the current byte, always between 0 and 7 (inclusive).
	private int numBitsRemaining;
	
	ArrayList<Character> inputStore;

	private int currentLocation;

	private int getCurByte(){
		int out;		
		if(currentLocation < inputStore.size()) {
			out = inputStore.get(currentLocation);
			currentLocation ++;		
		} else {
			out = 256;
		}	
		return out;
	}		

	/* Constructor */
	
	/**
	 * Constructs a bit input stream based on the specified byte input stream.
	 * @param in the byte input stream
	 * @throws NullPointerException if the input stream is {@code null}
	 */
	public BitInputStream(BitOutputStream in) {
		inputStore = in.outputStore;
		currentByte = 0;
		numBitsRemaining = 0;
		currentLocation = 0;
	}
	
	
	
	/* Methods */
	
	/**
	 * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or -1 if
	 * the end of stream is reached. The end of stream always occurs on a byte boundary.
	 * @return the next bit of 0 or 1, or -1 for the end of stream
	 * @throws IOException if an I/O exception occurred
	 */

	public void print(){
		System.out.println("The size is: " + inputStore.size());
		for(int i = 0; i < inputStore.size(); i ++){
			System.out.print(inputStore.get(i));
		}
		System.out.println();
	}


	public int readRand(Random rand) throws IOException{
		return (getVal() ^ 1) & 1;
	}

	@Randomize
	public int read() throws IOException {
		return getVal();
	}
	

	private int getVal() throws IOException{
		if (currentByte == -1)
			return -1;
		if (numBitsRemaining == 0) {
			currentByte = getCurByte();
			if (currentByte == -1)
				return -1;
			numBitsRemaining = 8;
		}
		if (numBitsRemaining <= 0)
			throw new AssertionError();
		numBitsRemaining--;
		return (currentByte >>> numBitsRemaining) & 1;
	}



	
	/**
	 * Reads a bit from this stream. Returns 0 or 1 if a bit is available, or throws an {@code EOFException}
	 * if the end of stream is reached. The end of stream always occurs on a byte boundary.
	 * @return the next bit of 0 or 1
	 * @throws IOException if an I/O exception occurred
	 * @throws EOFException if the end of stream is reached
	 */
	public int readNoEof() throws IOException {
		int result = read();
		if (result != -1)
			return result;
		else
			throw new EOFException();
	}
	
	
	/**
	 * Closes this stream and the underlying input stream.
	 * @throws IOException if an I/O exception occurred
	 */
	public void close(){
		currentByte = -1;
		numBitsRemaining = 0;
	}

	void reset(){
		numBitsRemaining = 0;
		currentLocation = 0;
		currentByte = 0;
	}
	
}
