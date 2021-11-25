# SailPoint-Assignment
My implementation of a collating Listener in Java.

Java 17.0.1

Author: Ryan Valdez

Date: Nov 25, 2021

For the purpose of my implementation, I designed the Listener class to accept input from a file or from the console. If using a file, please save said file to the 'SailPoint-Assignment/CollatingListener/src' directory and pass the file name as a command line argument when running the program. Note that, in either case, input must be formatted such that it looks like:

\<id\> \<message body\>

where \<id\> is an integer value and \<message body\> is anything after the first whitespace and before the line terminator. However, the Listener is designed to catch invalid input.

I also assumed that the implementation should include a native Processor class, as mentioned in the assignment outline. For simplicity, my Processor contains a static 'void process(List\<Message\> batch)' method, which accepts a list of Message objects and prints its contents to System.out.

To build from the command line, you may use the 'build.sh', 'clean.sh', and/or 'build_clean.sh' script files from inside the 'SailPoint-Assignment/CollatingListener/src' directory, which cleans and builds the class files.

To run from the command line, use the 'run.sh' script in the same directory. Include a file name as an argument when calling './run.sh' if you would like to use a file as input. Otherwise, the Listener will wait for console input. When finished entering console input, type 'exit' to stop the Listener.

I have included a few test file examples. 'sailpoint_test.txt' contains part of 'The SailPoint Story' from https://www.sailpoint.com/company/, 'count_to_fifty_test.txt' contains the numbers 1 to 50 as messages, and 'error_overload_test' contains repeated invalid input with valid input.
