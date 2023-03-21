import java.util.Random;
import java.util.HashMap;

/**
 * This is the main class for your Markov Model.
 *
 * Assume that the text will contain ASCII characters in the range [1,255].
 * ASCII character 0 (the NULL character) will be treated as a non-character.
 *
 * Any such NULL characters in the original text should be ignored.
 */
public class MarkovModel {
	public int order;
	//hash map to keep track of how often a particular substring appears
	HashMap<String, Integer> freqHashMap;
	//hash map to keep track of how often next element appears
	// after a substring
	HashMap<String, int[]> charHashMap;


	// Use this to generate random numbers as needed
	private Random generator = new Random();

	// This is a special symbol to indicate no character
	public static final char NOCHARACTER = (char) 0;

	/**
	 * Constructor for MarkovModel class.
	 *
	 * @param order the number of characters to identify for the Markov Model sequence
	 * @param seed the seed used by the random number generator
	 */
	public MarkovModel(int order, long seed) {
		// Initialize your class here
		this.order = order;
		this.freqHashMap = new HashMap<>();
		this.charHashMap = new HashMap<>();
		// Initialize the random number generator
		generator.setSeed(seed);
	}

	/**
	 * Builds the Markov Model based on the specified text string.
	 */
	public void initializeText(String text) {
		// Build the Markov model here
		int len = text.length();
		for (int i = 0; i < len - order; i++) {
			String subStr = text.substring(i, i + order);
			//if the substring isn't already present
			//add it to both hashmaps and set frequency to 1
			if (!freqHashMap.containsKey(subStr)) {
				//freq hashmap
				freqHashMap.put(subStr, 1);
				//add to character hashmap

				char nextChar = text.charAt(i + order);
				int iChar = (int) nextChar; //returns ascii character
				int[] arrayOfChars = new int[256];
				arrayOfChars[iChar] = 1;
				this.charHashMap.put(subStr, arrayOfChars);
			} else {
				//add to frequency hashmap
				int currentCount = freqHashMap.get(subStr);
				freqHashMap.put(subStr, currentCount + 1);

				//add to character hashmap
				char nextChar = text.charAt(i + order);
				int iChar = (int) nextChar; //ascii character
				int[] arrayOfChars = this.charHashMap.get(subStr);
				arrayOfChars[iChar] += 1;
				this.charHashMap.put(subStr, arrayOfChars);
			}
		}
	}

	/**
	 * Returns the number of times the specified kgram appeared in the text.
	 */
	public int getFrequency(String kgram) {
		if (kgram.length() == this.order) {
			if (this.freqHashMap.containsKey(kgram)) {
				return this.freqHashMap.get(kgram);
			}
		}
		//return 0 if key doesnt exist in the hashmap or
		//if the length of the key is wrong
		return 0;
	}

	/**
	 * Returns the number of times the character c appears immediately after the specified kgram.
	 */
	public int getFrequency(String kgram, char c) {
		if (kgram.length() == this.order) {
			if (this.charHashMap.containsKey(kgram)) {
				int index = (int) c;
				return this.charHashMap.get(kgram)[index];
			}
		}
		return 0;
	}

	/**
	 * Generates the next character from the Markov Model.
	 * Return NOCHARACTER if the kgram is not in the table, or if there is no
	 * valid character following the kgram.
	 */
	public char nextCharacter(String kgram) {
		// See the problem set description for details
		// on how to make the random selection.
		if (kgram.length() != this.order) {
			return NOCHARACTER;
		}
		int totalFrequency = this.freqHashMap.get(kgram);

		if (totalFrequency == 0) {
			return NOCHARACTER;
		}


		//choose random number from 0 to totalFrequency
		int randomNumber = generator.nextInt(totalFrequency);
		int[] arr = this.charHashMap.get(kgram);
		int i = 0;
		int count = 0;
		//checks to see if kargs was at the end of the string
		//in other words there is no character that comes after it.
		boolean isEnd = true;
		while (randomNumber >= 0) {
			if (arr[i] == 0) {
				i++;
			} else if (count >= arr[i]) {
				i++;
				count = 0;
			} else {
				isEnd = false;
				count++;
				randomNumber--;
			}
		}
		if (isEnd) {
			return NOCHARACTER;
		}

		return (char) i;


	}
}
