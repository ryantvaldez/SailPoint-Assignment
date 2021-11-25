package com.sailpoint.assignment;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.PatternSyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.Collections;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;

/*
 * **
 * 
 * @Class:
 * Public collating listener class which can listen for, buffer, and package messages from a file
 * or console input
 * 
 * **
 */
public class Listener {

	private AtomicInteger currentMessageId;
	private SortedMap<Integer, Message> buffer;
	private final String fileName;
	
	/*
	 * **
	 * 
	 * @Class
	 * Private subclass - each instance needs access to the buffer of the
	 * instantiating Listener object
	 * 
	 * **
	 */
	private class Collator implements Runnable {
		
		private Message message;

		/* @Method
		 * Construct a Collator Thread object to handle the Message, m
		 * 
		 * Returns: an instance of the Collator subclass
		 */
		private Collator(Message m) {
			this.message = m;
		}

		/*	@Method
		 *  Override the run() method of the Runnable interface. This function is run when
		 *  a new Collator Thread is started
		 *  
		 *  Returns: void
		 */
		@Override
		public void run() {
			
			// Stores the index of the next message id that would trigger the processor
			int index;
			
			if (this.message.getId() == (index = Listener.this.currentMessageId.get()) || Listener.this.buffer.containsKey(index)) {
				// We know we have at least one message in proper order which is ready for processing
				List<Message> batch = new ArrayList<>();
				Message iterator;
				try {
		
					Listener.this.buffer.put(this.message.getId(), this.message);
					// iterate through the buffer to find out how many subsequent messages it
					// contains without gaps
					while (!Listener.this.buffer.isEmpty()) {
						iterator = Listener.this.buffer.remove(Listener.this.buffer.firstKey());
						if (iterator.getId() == index) {
							// As long as this iterator holds the next message, add it to the batch
							boolean addToBatch = batch.add(iterator);
							assert addToBatch;
						} else {
							// Add back the last message removed from the buffer
							// At most, we will have to re-add one message
							Listener.this.buffer.put(iterator.getId(), iterator);
							break;
						}
						index++;
					}
					// Atomically update the currentMessageId of the parent Listener object
					boolean updateCurrentId = Listener.this.currentMessageId.compareAndSet(this.message.getId(),
							index);
					assert updateCurrentId;
					Processor.process(batch);

				} catch (NoSuchElementException | AssertionError e) {
					e.printStackTrace();

				}
			} else if(this.message.getId() < index || Listener.this.buffer.containsKey(this.message.getId()) ){
				// Do not allow for any duplicate entries
				System.out.printf("A message with id %d has already been stored or processed.%nNext valid id: %d%n", this.message.getId(), index);
			} else {
				// Just add the message to the buffer to be processed later
				Listener.this.buffer.put(this.message.getId(), this.message);
			}
		}
	}
	/* @Method
	 * Construct a Listener object to read from console input
	 * 
	 * Returns: an instance of the Listener class
	 */
	public Listener() {
		// Will begin by polling for message 0
		this.currentMessageId = new AtomicInteger();
		// Create a synchronized sorted map (must be synchronized externally)
		this.buffer = Collections.synchronizedSortedMap(new TreeMap<Integer, Message>());
		this.fileName = null;
	}
	
	/* @Method
	 * Construct a Listener object to read from the file, "fileName"
	 * 
	 * Returns: an instance of the Listener class
	 */
	public Listener(String fileName) {
		this.currentMessageId = new AtomicInteger();
		this.buffer = Collections.synchronizedSortedMap(new TreeMap<Integer, Message>());
		// Use the given file as input
		this.fileName = fileName;
	}
	
	/* @Method
	 * A simple getter for obtaining the current key set of the Listener object's buffer
	 * 
	 * Returns: this Listener object's key set
	 */
	public Set<Integer> getKeySet() {
		return this.buffer.keySet();
	}

	/* @Method
	 * Use a Scanner to continuously poll for file contents until empty
	 * or console input until "exit" is written by the user
	 * 
	 * Returns: void
	 */
	public void listen() {
		Scanner scanner = null;
		if(this.fileName == null) {
			// If there is no specified file, use console input
			scanner = new Scanner(System.in);
			System.out.println("Listening to console input...");
		} else {
			File inputFile = new File(this.fileName);
			try {
				scanner = new Scanner(inputFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			System.out.printf("Listening to file %s...%n", this.fileName);
		}
		while (scanner.hasNext()) {
			try {
				String input = scanner.nextLine();
				if (input.equals("exit")) {
					// Allow the user to exit the listener program with "exit"
					break;
				} else if(input.equals("")) {
					// Disregard any empty input lines
					continue;
				}
				// Valid input should split into <id> and <text> at the first whitespace character
				String[] splitInput = input.split(" ", 2);
				int id = Integer.parseInt(splitInput[0]);
				Message m = new Message(id, splitInput[1]);
				// Create a new collator thread to concurrently add to the buffer, organize, and process messages
				Thread collatorThread = new Thread(new Collator(m));
				collatorThread.run();
			} catch (NumberFormatException | PatternSyntaxException | ArrayIndexOutOfBoundsException e) {
				// These exceptions are thrown by Integer.parseInt, String.split, and referencing 
				// a non-existent index of splitInput[], respectively
				System.out.printf("**Invalid Input**%nEach line must be of the form "
						+ "'<id><whitespace><text>', where <id> is an integer value and "
						+ "<text> is a string.%n");				
			}
		}
		System.out.println("Shutting down listener...");
		scanner.close();
	}

}
