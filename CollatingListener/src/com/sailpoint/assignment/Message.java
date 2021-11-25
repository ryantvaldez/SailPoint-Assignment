package com.sailpoint.assignment;

/*
 * **
 * 
 * @Class:
 * Simple class for storing and accessing the two fields of a message
 * 
 * **
 */
public class Message {

	private final int id;
	private final String body;

	/* @Method
	 * Construct a Message object with id, 'id', text field, 'body'
	 * 
	 * Returns: an instance of the Message class
	 */
	public Message(int id, String body) {
		this.id = id;
		this.body = body;
	}

	/* @Method
	 * A simple getter for the id field
	 * 
	 * Returns: this message object's id
	 */
	public int getId() {
		return this.id;
	}

	/* @Method
	 * A simple getter for the body field (text)
	 * 
	 * Returns: this message object's body
	 */
	public String getBody() {
		return this.body;
	}

}
