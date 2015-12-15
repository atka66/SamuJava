/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package samujava;

public class SamuJava {

    /**
     * @param args the command line arguments
     */
	public static void main(String[] args) {

		// Do not remove this copyright notice!
		System.out.print( "This program will try to provide a disembodied DevRob agent called Samu Bátfai."
		  + "\n"
		  + "Copyright (C) 2015 Norbert Bátfai"
		  + "\n"
		  + "License GPLv3+: GNU GPL version 3 or later <http://gnu.org/licenses/gpl.html>"
		  + "\n"
		  + "This is free software: you are free to change and redistribute it."
		  + "\n"
		  + "There is NO WARRANTY, to the extent permitted by law."
		  + "\n"
		  + "\n");


		System.out.print("The prenatal development phase has started." + "\n");
	
		System.out.print("The prenatal development phase has finished." + "\n");


		String[] test = {
	  	"A rare black squirrel has become a regular visitor to a suburban garden",
	  	"This is a car",
	  	"This car is mine",
	  	"I have a little car",
	  	"The sky is blue",
	  	"The little brown bear has eaten all of the honey",
	  	"I love Samu"
		};

		int k = 0;

		for (Samu samu = new Samu(); samu.run();) {
			double sum = 0.0;
	    	for (int i = 0; i < 7; ++i) {
	    		samu.putIn(test[i]);
				sum += samu.reward();
	    	}
	    	System.out.print( "###### " + (++k) + "-th iter " + sum + "\n");
		}
	}
    
}
