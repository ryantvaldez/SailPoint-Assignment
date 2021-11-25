package com.sailpoint.assignment;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/*
 * **
 * 
 * @Class:
 * A generic Processor class to stand in for a more complex processor; could be extended and 
 * overridden for more complex data handling
 * 
 * **
 */
public class Processor {

	private static Lock lock = new ReentrantLock();
	/* @Method
	 * A static method to be called externally to process an ordered batch of messages
	 * This method utilizes a static lock to ensure data is not processed out of order
	 * 
	 * Returns: void
	 */
	public static void process(List<Message> batch) {
		// Utilize a lock to ensure batches are printed in order across multiple threads
		lock.lock();
		System.out.printf("_____PROCESSOR_____: %n");
		for (int i = 0; i < batch.size(); i++) {
			System.out.printf("%s ", batch.get(i).getBody());
		}
		System.out.println();
		lock.unlock();
	}

}
