package com.sailpoint.assignment;
/*
 * **
 * 
 * @Class:
 * General class for implementing the main method of this project; this ensures 
 * all other classes stay encapsulated
 * 
 * **
 */
public class Solution {

	public static void main(String[] args) {
		
		Listener listener;
		// Check if the user has specified a file to be used 
		if(args.length == 0) {
			listener = new Listener();
		} else if(args.length == 1) {
			listener = new Listener(args[0]);
		} else {
			System.out.println("Invalid arguments. Please include a single valid input file name or nothing (for user input). Defaulting to user input mode.");
			listener = new Listener();
		}
		listener.listen();
		System.exit(0);

	}

}
