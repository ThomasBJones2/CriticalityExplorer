package InputObjects;

import java.util.Random;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//this class is used to grab random words
//
public class Dictionary{
	ArrayList<String> words;
	
	public String getRandomWord(Random rand){
		return words.get(rand.nextInt(words.size()));
	}


	public Dictionary(){
		words = new ArrayList<>();
		try {
			BufferedReader in = new BufferedReader(new FileReader(
						"/usr/share/dict/american-english"));
			String str;
			while((str = in.readLine()) != null) {
				words.add(str);
			}
			
			in.close();
		} catch (IOException e){
			System.out.println("You must not have an american-english dictionary in " + 
					" /usr/share/dict/american-english");
			System.out.println(e);
		}
	}
}
