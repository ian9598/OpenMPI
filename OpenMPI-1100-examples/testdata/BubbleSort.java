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
            for( j=0;  j < num.length -1;  j++ )
            {
                   if ( num[ j ] > num[j+1] )   // change to > for ascending sort
                   {
                           temp = num[ j ];                //swap elements
                           num[ j ] = num[ j+1 ];
                           num[ j+1 ] = temp;
                          flag = true;              //shows a swap occurred  
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


	int myrank = MPI.COMM_WORLD.getRank() ;
	int size = MPI.COMM_WORLD.getSize() ;

	/* Calculate the rank of the next process in the ring.  Use the
	   modulus operator so that the last process "wraps around" to
	   rank zero. */

	next = (myrank + 1) % size;
	prev = (myrank + size - 1) % size;



	
	//long totaltime = 0 ; 
    	String[] filenames = {"med.3.killer.1000.txt","med.3.killer.10000.txt","rand.dups.1000.txt","rand.dups.10000.txt","rand.dups.100000.txt",
    			"rand.no.dups.1000.txt","rand.no.dups.10000.txt","rand.no.dups.100000.txt", "rand.steps.1000.txt","rand.steps.10000.txt",
    			"rand.steps.100000.txt", "rev.partial.1000.txt","rev.partial.10000.txt","rev.partial.100000.txt", "rev.saw.1000.txt","rev.saw.10000.txt",
    			"rev.saw.100000.txt", "seq.partial.1000.txt","seq.partial.10000.txt","seq.partial.100000.txt","seq.saw.1000.txt",
    			"seq.saw.10000.txt", "seq.saw.100000.txt"};
    	
    	int[] sizeOfArray = {1000,10000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000};  
    	int[][] array = new int[23][100000] ; 
    	ArrayList <int[]> list = new ArrayList<int[]>() ; 
    	int[] a = new int[100000]  ; 
    	int go = 0 ; 
    	try {
    	    for(int i = 0 ; i< 2 ; i++ ){ // 23 file 
    		System.out.println(go);
    			
    		File file = new File(filenames[i]);
             	BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             	StringBuffer tmp = new StringBuffer();
             	int [] eachfile = new int[sizeOfArray[i]];
             	int count = 0 ; 
             	while (input.ready()) {   
             		String line = input.readLine() ;
                    eachfile[count] = Integer.parseInt(line);
                    count++; 
             	}
             	list.add(eachfile); 
             	BubbleSort.BubbleSort(list.get(i));
             	go++ ;
            }
           

            
     	} catch (Exception e) {
        }
        /* If we are the "master" process (i.e., MPI_COMM_WORLD rank 0),
	put the number of times to go around the ring in the
	message. */
        if (0 == myrank) {
	    message[40] = 10 ; 
	    System.out.println("Process 0 sending " + message + " to rank " + next + " (" + size + " processes in ring) -"+ tag); 
	    MPI.COMM_WORLD.send(message, 41,  MPI.INT, next, tag);		 
	    MPI.COMM_WORLD.send(list.get(0), 1000,  MPI.INT, next, tag);
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
	    MPI.COMM_WORLD.recv(list.get(0),1000, MPI.INT, prev, tag);
	    	
	 if (0 == myrank) {
		--message[40];
		 int[] array1 = new int[10] ;
     		for ( int i = 0 ; i < 10 ; i++ ){
            		array1[i]=  message[i+(10* myrank)];
      		}
		BubbleSort.BubbleSort(array1) ;
      		for ( int i=  0 ; i < 10 ; i++ ){
            		message[i+(10*myrank)]=  array1[i];
      		}            
		System.out.println("Process 0 decremented value: " + message[40] + " -"+ tag);
	 }
	 else {
	   	System.out.println ( "Here" + myrank);
	   	int[] array1 = new int[10] ;
      		for ( int i = 0 ; i < 10 ; i++ ){
          	array1[i]=  message[i+(10* myrank)];
      	 	}
        	BubbleSort.BubbleSort(array1) ;
      	 	for ( int i=  0 ; i < 10 ; i++ ){
          		message[i+(10*myrank)]=  array1[i];
      		 }
                 
	  }
	   	
	   
	
	  MPI.COMM_WORLD.send(message, 41, MPI.INT, next, tag);
	  MPI.COMM_WORLD.send(list.get(0),1000, MPI.INT, next, tag);
	  if (0 == message[40]) {
		System.out.println("Process " + myrank + " exiting");
        	break;
	   }
	}

	/* The last process does one extra send to process 0, which needs
	   to be received before the program can exit */

	if (0 == myrank) {
	    	
	    MPI.COMM_WORLD.recv(message,41, MPI.INT, prev, tag);
	    MPI.COMM_WORLD.recv(list.get(0),1000, MPI.INT, prev, tag);
	    for ( int i = 0 ; i< 40 ; i++ ){
	    	System.out.println(message[i]+",");
	    }	
		
	}
    
	MPI.Finalize();
    }
}
