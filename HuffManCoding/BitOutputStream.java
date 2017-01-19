package HuffManCoding;
/* 
 * Reference Huffman coding
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/page/reference-huffman-coding
 * https://github.com/nayuki/Reference-Huffman-coding
 */

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * A stream where bits can be written to. Because they are written to an underlying
 * byte stream, the end of the stream is padded with 0's up to a multiple of 8 bits.
 * The bits are written in big endian. Mutable and not thread-safe.
 * @see BitInputStream
 */
public final class BitOutputStream {
	
	/* Fields */
	
	// The accumulated bits for the current byte, always in the range [0x00, 0xFF].
	private int currentByte;
	
	// Number of accumulated bits in the current byte, always between 0 and 7 (inclusive).
	private int numBitsFilled;
	
	//This is where the outputStore of bit are located. 
	public ArrayList<Character> outputStore;
	
	/* Constructor */
	
	/**
	 * Constructs a bit output stream based on the specified byte output stream.
	 * @param in the byte output stream
	 * @throws NullPointerException if the output stream is {@code null}
	 */
	public BitOutputStream() {
		outputStore = new ArrayList<>();
		currentByte = 0;
		numBitsFilled = 0;
	}
	
	
	public void print(){
		System.out.println("The size is: " + outputStore.size());
		for(int i = 0; i < outputStore.size(); i ++){
			System.out.print(outputStore.get(i) + " ");
		}
	
	}

	/* Methods */
	
	/**
	 * Writes a bit to the stream. The specified bit must be 0 or 1.
	 * @param b the bit to write, which must be 0 or 1
	 * @throws IOException if an I/O exception occurred
	 */
	public void write(int b) throws IOException {
		if (b != 0 && b != 1)
			throw new IllegalArgumentException("Argument must be 0 or 1");
		currentByte = (currentByte << 1) | b;
		numBitsFilled++;
		if (numBitsFilled == 8) {
			outputStore.add(new Character((char)currentByte));
			//output.write(currentByte);
			currentByte = 0;
			numBitsFilled = 0;
		}
	}
	
	
	/**
	 * Closes this stream and the underlying output stream. If called when this
	 * bit stream is not at a byte boundary, then the minimum number of "0" bits
	 * (between 0 and 7 of them) are written as padding to reach the next byte boundary.
	 * @throws IOException if an I/O exception occurred
	 */
	public void close() throws IOException {
		while (numBitsFilled != 0)
			write(0);
	}
	
}
