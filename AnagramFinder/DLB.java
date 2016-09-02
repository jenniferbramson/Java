// CS 1501 Summer 2016
// Use this class with Assignment 1 Part A.  You may use either or both
// versions of the searchPrefix method, depending upon how you design
// your algorithm.  Read over the code and make sure you understand how
// it works and why it is not very efficient.

import java.util.*;
public class DLB implements DictInterface
{

  private Node root;

	public DLB() {
    // root data doesn't matter
		root = new Node(' ');
	}

  /*
	// Add new String by adding each character to the trie
	public boolean add(String s) {

    if (s.length() > 0) {
      // change s to char array
      char[] c = s.toCharArray();
      Node curr = root;
      char currentChar;

      for (int i = 0; i < c.length; i++) { // children not there, add children
        currentChar = c[i];
        curr = curr.child;

        while (curr != null && curr.data != currentChar) {
          System.out.println("Looking for char");
          curr = curr.sib; // get the next sibling
        }

        // now curr should either be at the end of the linked list of siblings
        // or it should at the node that contains that character
        if (curr == null) {
          // Create a new node with that character
          curr = new Node(currentChar);
        } else {
          // curr.data == currentChar
          // It is at the correct character, just do nothing
        }

      } // end for loop going through String

      // add 'end of word character' - since you are adding this at the beginning
      // of the linked list, and all characters are added at the end of the
      // linked list, after searching through the list, the end word character
      // will always be the first item in the linked list if a word ends there
      curr = curr.child;
      curr = new Node('$'); // dollar sign is used as the end of word character

      return true;
    } // end adding valid string
    else {
      // string was an empty string, which is not valid
      return false;
    }
	} // end add method
  */


  // Add new String by adding each character to the trie
  public boolean add(String s) {

    if (s.length() > 0) {
      // change s to char array
      char[] c = s.toCharArray();
      Node curr = root;
      char currentChar;

      for (int i = 0; i < c.length; i++) { // children not there, add children
        currentChar = c[i];

        if (curr.child != null) {
          curr = curr.child;

          while (curr.sib != null && curr.data != currentChar) {
            curr = curr.sib; // get the next sibling
          }

          // now curr should either be at the end of the linked list of siblings
          // or it should at the node that contains that character
          if (curr.data == currentChar) {
            // It is at the correct character, just do nothing
          } else {
            // Create a new node with that character
            curr.sib = new Node(currentChar);
            curr = curr.sib;
          }
        } else {
          curr.child = new Node(currentChar);
          curr = curr.child;
        }

      } // end for loop going through String

      // add 'end of word character' - since you are adding this at the beginning
      // of the linked list, and all characters are added at the end of the
      // linked list, after searching through the list, the end word character
      // will always be the first item in the linked list if a word ends there
      curr.child = new Node('$'); // dollar sign is used as the end of word character

      return true;
    } // end adding valid string
    else {
      // string was an empty string, which is not valid
      return false;
    }
  } // end add method



	// Implement the searchPrefix method as described in the
	// DictInterface class.
	public int searchPrefix(StringBuilder s) {

    boolean word, prefix;
		Node curr = root;
    char currentChar;
		word = false;
    prefix = false;


		for (int i = 0; i < s.length(); i++) {
      curr = curr.child;
      currentChar = s.charAt(i);

      // check through sibling linked list for the character
      while (curr != null && curr.data != currentChar) {
        curr = curr.sib;
      }

      if (curr == null) {
        // character missing, so we are done checking the prefix
        // return 0, meaning 'not a prefix'
        return 0;
      } else {
        // character found, continue on
      }

    } // end for loop

    // If it has progressed to this point, either the prefix is a prefix, a
    // word, or both a prefix and a word. We need to find out which.

    // Check to see if the first child is an end word char ('$')
    curr = curr.child;
    if (curr.data == '$') {
      // it is a word
      curr = curr.sib;
      if (curr != null) {
        // if the end word character had a sibling, then it is a prefix too
        return 3; // prefix and word
      } else {
        // if the end word character had no sibling, then it is just a word
        return 2; // word
      }
    } else {
      // a prefix
      return 1; // prefix
    }

  } // end searchPrefix method



  // not used, so throw an exception (implement later)
	public int searchPrefix(StringBuilder s, int start, int end)	{
    return 0;
	}



  private class Node {

    private char data;
    private Node child;
    private Node sib;

    private Node(char data, Node child) {
      this.data = data;
      this.child = child;
    }

    private Node(char data) {
      this.data = data;
    }

  } // end Node class

}
