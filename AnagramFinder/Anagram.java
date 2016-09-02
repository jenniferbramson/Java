// CS 1501 Summer 2016
// Test program for DictInterface.  Use this program to see how the DictInterface
// works and also to test your DLB implementation of the DictInterface (for Part B
// of Assignment 1).  See the posted output file for the correct output.
// As written the program uses MyDictionary, so it will work and show you the
// output using the MyDictionary class.  To test your DBL class substitute a
// DLB object as indicated below.  Note that since MyDictionary and DLB should
// both implement DictInterface no other changes are necessary for the program
// to work.

import java.io.*;
import java.util.*;
public class Anagram
{

	// uses either DLB or MyDictionary depending on command line input
	private static DictInterface D;

	public static void main(String [] args) throws IOException
	{
		Scanner fileScan = new Scanner(new FileInputStream("dictionary.txt"));
		String st;
		StringBuilder sb;
		Scanner fileScan2;
		PrintWriter out;
		// Store Strings of mixed letters in an ArrayList
		ArrayList<String> letters = new ArrayList<String>();


		// Set up scrambled words to search through
		try  {
			fileScan2 = new Scanner(new FileInputStream(args[0])); // test file
			String str;
			while (fileScan2.hasNext()) {
				str = fileScan2.nextLine();
				letters.add(str);
			}
			out = new PrintWriter(args[1]); // output file

			if (args[2] == "orig") {
				D = new MyDictionary();
			} else {
				// DLB
				D = new DLB();
			}

			// Create the Dictionary
			while (fileScan.hasNext())
			{
				st = fileScan.nextLine();
				D.add(st);
			}

			// Get next word, sort word and search through dictionary for matches
			for (int i = 0; i < letters.size(); i ++) {
				String current = letters.get(i); // because letters is an
																				// array, .get() uses direct access
				System.out.println("Here are the results for " + current + ":");
				out.println("Here are the results for " + current + ":");
				// Sort String
				char[] currentArr = current.toCharArray();
				Arrays.sort(currentArr); // This sort is a dual pivot Quicksort, so
																// O(N*log2(N)) on average
				current = new String(currentArr);
				current = current.trim(); // With the way it was sorted, the whitespace
																	// is leading, trim removes it

				// ArrayList to store solutions:
				ArrayList<ArrayList<String>> solutions = new ArrayList<ArrayList<String>>(2);
				solutions.add(new ArrayList<String>());
				StringBuilder word = new StringBuilder(current);
				StringBuilder prefix = new StringBuilder();

				perm(prefix, word, solutions, new ArrayList<StringBuilder>());

				// Print out solutions
				int j = 0;
				for (ArrayList<String> arr : solutions) {
					if (arr.size() > 0) {
						System.out.println(j + " word solutions:");
						out.println(j + " word solutions:");
						for (String s : arr) {
							System.out.println(s);
							out.println(s);
						}
					}
					j++;
				}

				// Print out empty line after each scrambled word's anagrams
				System.out.println();
				out.println();
			} // end for loop

			out.close(); // close file

		} catch (Exception e) {
			// I just used a general Exception to catch array out of bounds exceptions
			// and IO exceptions from inproper command line input
			System.out.println("File with anagrams, output file name, and data structure type required");
		}
	}


	// Each solution is saved as a String to the 'solutions' ArrayList at an
	// index corresponding to the number of words in the solution
	private static void perm(StringBuilder prefix, StringBuilder word,
													ArrayList<ArrayList<String>> solutions,
													ArrayList<StringBuilder> multi) {
		if (word.length() > 0) {
			int ans;
			if (prefix.length() > 0) {
				ans = D.searchPrefix(prefix);
			} else {
				// First iteration, prefix empty
				ans = 4;
			}
			switch(ans) {
				case (0): // Not found
						// Base case, backtrack
						break;
				case (2): // A word
						// Add a space and try to find if you can make a word with the
						// remaining characters

						// MULTI WORD PORTION
						// Recurse with a subset of the original word, eliminating the
						// character for the word found already
						multi.add(prefix);
						perm(new StringBuilder(), word, solutions, multi);
						multi.remove(multi.size() - 1); // remove last word just added
						//MULTI WORD PORTION
						break;
				case (1): // A prefix
				case (4): // Prefix is empty
						// Append new character to prefix, then return to the top of the
						// method with a recursive call to evaluate

						// skip the next character if it is an empty space
						if (word.charAt(0) == ' ') {
							word.deleteCharAt(0);
						}

						prefix.append(word.charAt(0)); // add next char
						word.deleteCharAt(0); // delete that char so it isn't repeated
						perm(prefix, word, solutions, multi);

						// Once all promising prefix combos are tried with that character
						// appended, try swapping in all other possible characters in that
						// spot using the swap method
						for (int j = 0; j < word.length(); j++) {
							// Skips a j value if the swap is between the same letterss
							j = swap(prefix, word, j);
							if (j < word.length()) { // check to see if need to skip perm
								perm(prefix, word, solutions, multi);
							}
						}

						// Once all swaps are done, the last character originally in 'word'
						// is the last character of 'prefix', so append in back to 'word'
						// to keep order
						word.append(prefix.charAt(prefix.length() - 1));
						prefix.deleteCharAt(prefix.length() - 1); // delete that char so it isn't repeated
						break;
				case (3): // A word and prefix
						// Append new character to prefix, then return to the top of the
						// method with a recursive call to evaluate

						// MULTI WORD PORTION
						// Recurse with a subset of the original word, eliminating the
						// character for the word found already
						multi.add(prefix);
						perm((new StringBuilder()), word, solutions, multi);
						multi.remove(multi.size() - 1); // remove last word just added
						// MULTI WORD PORTION

						// skip the next character if it is an empty space
						if (word.charAt(0) == ' ') {
							word.deleteCharAt(0);
						}

						prefix.append(word.charAt(0)); // add next char
						word.deleteCharAt(0); // delete that char so it isn't repeated
						perm(prefix, word, solutions, multi);
						// Once all promising prefix combos are tried with that character
						// appended, try swapping in all other possible characters in that
						// spot using the swap method
						for (int j = 0; j < word.length(); j++) {
							// Skips a j value if the swap is between the same letterss
							j = swap(prefix, word, j);
							if (j < word.length()) { // check to see if need to skip recurse
								perm(prefix, word, solutions, multi);
							}
						}

						// Once all swaps are done, the last character originally in 'word'
						// is the last character of 'prefix', so append in back to 'word'
						// to keep order
						word.append(prefix.charAt(prefix.length() - 1));
						prefix.deleteCharAt(prefix.length() - 1); // delete that char so it isn't repeated

						break;
			} // end switch statement
		} else {
			// Out of characters to append to prefix, evaluate then backtrack
			// base case
			int ans = D.searchPrefix(prefix);
			switch (ans) {
				case 0: // Not found
					break;
				case 1: // A prefix
					break;
				case 2: // A word
				case 3: // A word and prefix
					// Creates a word and all characters used, so is a solution
					if (multi.size() == 0) {
						// one word solution
						if (solutions.size() < 2) {
							solutions.add(new ArrayList<String>());
						}
						solutions.get(1).add(new String(prefix));
					} else {
						// multi word solution
						// Make sure the proper ArrayList in solutions is initialized
						while (solutions.size() <= (multi.size() + 1)) {
							solutions.add(new ArrayList<String>());
						}
						// Add the last word to the multiword solution ArrayList, then
						// add it as a String to the ArrayList at an index corresponding
						// to the number of words in the solution. Then, remove
						// the word from 'multi' before backtracking, so it return as it
						// arrived
						multi.add(prefix);
						String temp = "";
						for (StringBuilder s: multi) {
							temp += s;
							temp += " ";
						}
						solutions.get(multi.size()).add(new String(temp));
						multi.remove(multi.size() - 1);
					}
					break;
			} // end switch statement
		} // end else statement
	} // end perm method

	// swaps the last character in 'prefix' with the character at index in 'word'
	private static int swap(StringBuilder prefix, StringBuilder word, int index) {
		if (index < word.length() && prefix.length() > 0) {
			char temp = word.charAt(index);

			// Only swap character that are different to avoid repitition
			// Like characters next to each other because the data is sorted
			if (temp != prefix.charAt(prefix.length() - 1)) {
				word.setCharAt(index, prefix.charAt(prefix.length() - 1));
				prefix.setCharAt(prefix.length() - 1, temp);
			} else {
				index = swap(prefix, word, index+1);
			}
		}

		return index;
	} // end swap method

}
