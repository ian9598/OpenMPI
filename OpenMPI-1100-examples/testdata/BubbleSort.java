/*
 * Copyright (c) 2011      Cisco Systems, Inc.  All rights reserved.
 *
 * Simple ring test program
 */

import mpi.* ;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList; 
class BubbleSort {

    public static void BubbleSort( int [ ] num )
{
     int j;
     boolean flag = true;   // set flag to true to begin first pass
     int temp;   //holding variable

     while ( flag )
     {
            flag= false;    //set flag to false awaiting a possible swap
            for( j=0;  j < num.length -1;  j++ ){
                   if ( num[ j ] > num[j+1] ){  
                           temp = num[ j ];                
                           num[ j ] = num[ j+1 ];
                           num[ j+1 ] = temp;
                           flag = true;              
                  } 
            } 
      } 
} 

    public static void BubbleSort( int [ ] num  , int length)
{
     int j;
     boolean flag = true;   // set flag to true to begin first pass
     int temp;   //holding variable

     while ( flag )
     {
            flag= false;    //set flag to false awaiting a possible swap
            for( j=0;  j < length -1;  j++ ){
                   if ( num[ j ] > num[j+1] ){  
                           temp = num[ j ];                
                           num[ j ] = num[ j+1 ];
                           num[ j+1 ] = temp;
                           flag = true;              
                  } 
            } 
      } 
} 	


    static public void main(String[] args) throws MPIException {
	
	
	MPI.Init(args) ;
      
	int source;  // Rank of sender
	int dest;    // Rank of receiver 
	int tag=50;  // Tag for messages	
	int next;
	int prev;
	int message[]	 = {2, 3, 4, 12, 11, 1, 99,87,98,99, 999,165,433,423, 989, 423, 533, 660, 604, 776,999,165,433,423, 989, 423, 533, 660, 604, 776,1912, 1413, 1104, 1212, 1311, 1231, 1199,1487,1098,1099,3999,2165,1433,1423, 3989, 7423, 3533, 1660, 1604, 2776,10};
	int[] sizeOfArray = {1000,10000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000};  
	int ia = 0 ; 	int count = 0 ; 
	int filesize = sizeOfArray[ia] ; 
	int [] eachfile = new int[filesize];
	int myrank = MPI.COMM_WORLD.getRank() ;
	int size = MPI.COMM_WORLD.getSize() ;
	int c = 0 , c2 = 0 , c3 =0 , c4= 0 ; // count 
	int [] array = new int[1000] ;
	int [] array2 = new int[1000] ;
	int [] array3= new int[1000] ;
	int [] array4 = new int[1000] ;
	
	/* Calculate the rank of the next process in the ring.  Use the
	   modulus operator so that the last process "wraps around" to
	   rank zero. */

	next = (myrank + 1) % size;
	prev = (myrank + size - 1) % size;
	
    	String[] filenames = {"med.3.killer.1000.txt","med.3.killer.10000.txt","rand.dups.1000.txt","rand.dups.10000.txt","rand.dups.100000.txt",
    			"rand.no.dups.1000.txt","rand.no.dups.10000.txt","rand.no.dups.100000.txt", "rand.steps.1000.txt","rand.steps.10000.txt",
    			"rand.steps.100000.txt", "rev.partial.1000.txt","rev.partial.10000.txt","rev.partial.100000.txt", "rev.saw.1000.txt","rev.saw.10000.txt",
    			"rev.saw.100000.txt", "seq.partial.1000.txt","seq.partial.10000.txt","seq.partial.100000.txt","seq.saw.1000.txt",
    			"seq.saw.10000.txt", "seq.saw.100000.txt"};
    	
    	
    	
    	
        /* If we are the "master" process (i.e., MPI_COMM_WORLD rank 0),
	put the number of times to go around the ring in the
	message. */
        if (0 == myrank) {
            try {
    			
    		File file = new File(filenames[ia]);
             	BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             	StringBuffer tmp = new StringBuffer();
             
             	while (input.ready()) {   
             	    String line = input.readLine() ;
                    eachfile[count] = Integer.parseInt(line);
                    count++; 
             	}
     	    } catch (Exception e) {        }	
	    message[40] = 1 ; 
	    System.out.println( myrank +" COUNT : "+ count );
	    System.out.println("Process 0 sending " + message + " to rank " + next + " (" + size + " processes in ring) -"+ tag); 
	    MPI.COMM_WORLD.send(message, 41,  MPI.INT, next, tag);		 
	    MPI.COMM_WORLD.send(eachfile, filesize,  MPI.INT, next, tag);
	   
	}
	/* Pass the message around the ring.  The exit mechanism works as
	   follows: the message (a positive integer) is passed around the
	   ring.  Each time it passes rank 0, it is decremented.  When
	   each processes receives a message containing a 0 value, it
	   passes the message on to the next process and then quits.  By
	   passing the 0 message first, every process gets the 0 message
	   and can quit normally. */
	
	while (true) {
	    	
	    MPI.COMM_WORLD.recv(message,41, MPI.INT, prev, tag);
	    MPI.COMM_WORLD.recv(eachfile, filesize,  MPI.INT, prev, tag);
	    System.out.println( myrank +" COUNT : "+ count );	
	 if (0 == myrank) {
		--message[40];
		System.out.println ( "Here" + myrank);
	   	int boundSize  = (filesize/size ) ; 
		int lowerBound = boundSize * myrank  ; // ( 0.25 * 0)
		int upperBound = (boundSize * (myrank+1)) -1  ;  // ( 0.25*1)
	//	int lowerBound = 0 ; int upperBound = 249 ; 
		System.out.println("Process 0 decremented value: " + message[40] + " -"+ tag);
		int[] gather = new int[filesize] ; 
		c = 0 ;
		for ( int i = 0 ; i < filesize ;i++ ){
			if(eachfile[i] >= lowerBound && eachfile[i] <=  upperBound ){
				gather[c] = eachfile[i] ; 
				c++ ; 
			}
		}
		array = new int [c] ; 
		for ( int i = 0 ; i< c ; i++ ){
			array[i] = gather[i] ; 
			//System.out.println(gather[i]) ; 
		}
		BubbleSort.BubbleSort(array); 
		System.out.print ( "length here ( "+ c+" )");
		MPI.COMM_WORLD.send(array, c,  MPI.INT, 0, tag);
		
 	 }
	 else if ( myrank ==1 ) {
	   	int boundSize  = (filesize/size ) ; 
		int lowerBound = boundSize * myrank  ; // ( 0.25 * 0)
		int upperBound = (boundSize * (myrank+1)) -1  ;  // ( 0.25*1)	
		//int[] gather = new int[filesize] ; 
		c = 0 ; 
		for ( int i = 0 ; i < filesize ;i++ ){
			if(eachfile[i] >= lowerBound && eachfile[i] <=  upperBound ){
				array2[c] = eachfile[i] ; 
				c++ ; 
			}
		}
		System.out.println ("++++++++");
		
		BubbleSort.BubbleSort(array2 , c ); 
		for( int i = 0 ; i< c ; i++){
			System.out.println ("- "+ array2[i] +" "+ i+ " "+c +" "+array2.length);
		}
		MPI.COMM_WORLD.send(array2, c,  MPI.INT, 0, tag);
                 
	  }
	   	
	  
	  MPI.COMM_WORLD.send(message, 41, MPI.INT, next, tag);
	  MPI.COMM_WORLD.send(eachfile, filesize,  MPI.INT, next, tag);
	  if (0 == message[40]) {
		System.out.println("Process " + myrank + " exiting");
        	break;
	   }
	}

	/* The last process does one extra send to process 0, which needs
	   to be received before the program can exit */

	if (0 == myrank) {
	    	
	    MPI.COMM_WORLD.recv(message,41, MPI.INT, prev, tag);
	    MPI.COMM_WORLD.recv(array, array.length,  MPI.INT, 0 , tag);
	    System.out.print ( "length  ( "+ array.length +" )");
	    MPI.COMM_WORLD.recv(array2, array2.length,  MPI.INT, 1, tag);
	   // MPI.COMM_WORLD.recv(array2, array2.length,  MPI.INT, 2, tag);
	    //MPI.COMM_WORLD.recv(array2, array2.length,  MPI.INT, 3, tag);
	    //for ( int i = 0 ; i< array2.length ; i++ ){
	    //	System.out.println(array2[i]+",");
	    //}	
	    MPI.COMM_WORLD.recv(eachfile, filesize,  MPI.INT, prev, tag);

		
	}
    
	MPI.Finalize();
    }
}
