/*
 * Copyright (c) 2011      Cisco Systems, Inc.  All rights reserved.
 *
 * Simple ring test program
 */

import mpi.* ;
 
class SortNew {

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
	int message[]	 = {2, 3, 4, 12, 11, 1, 99,87,98,99, 999,165,433,423, 989, 423, 533, 660, 604, 776,10};
//	int message[] = new int [21];	
	int count[] = new int[1];
	int myrank = MPI.COMM_WORLD.getRank() ;
	int size = MPI.COMM_WORLD.getSize() ;

	/* Calculate the rank of the next process in the ring.  Use the
	   modulus operator so that the last process "wraps around" to
	   rank zero. */

	next = (myrank + 1) % size;
	prev = (myrank + size - 1) % size;

	/* If we are the "master" process (i.e., MPI_COMM_WORLD rank 0),
	   put the number of times to go around the ring in the
	   message. */

	if (0 == myrank) {
	    message[20] = 10 ; 
	    int message1[]    = {2, 3, 4, 12, 11, 1, 99,87,98,99, 999,165,433,423, 989, 423, 533, 660, 604, 776} ;	
		
		     
	    System.out.println("Process 0 sending " + message + " to rank " + next + " (" + size + " processes in ring) -"+ tag); 
	    
	    MPI.COMM_WORLD.send(message, 21,  MPI.INT, next, tag);		 
	}

	/* Pass the message around the ring.  The exit mechanism works as
	   follows: the message (a positive integer) is passed around the
	   ring.  Each time it passes rank 0, it is decremented.  When
	   each processes receives a message containing a 0 value, it
	   passes the message on to the next process and then quits.  By
	   passing the 0 message first, every process gets the 0 message
	   and can quit normally. */
	
	while (true) {
	    MPI.COMM_WORLD.recv(message,21, MPI.INT, prev, tag);
	    	
	    if (0 == myrank) {
		--message[20];
		System.out.println("Process 0 decremented value: " + message[20] + " -"+ tag);
	    }
	    else if ( 1 == myrank){
		int[] array1 = new int[10 ] ; 
		for ( int i = 0 ; i < 10 ; i++ ){
			array1[i]=  message[i];
		}
		SortNew.BubbleSort(array1) ;
		for ( int i=  0 ; i < 10 ; i++ ){ 
                        message[i]=  array1[i];
                }
		//MPI.COMM_WORLD.send(message,20, MPI.INT, next, tag);	
	
	   }
	   else if ( 2 == myrank){
                int[] array1 = new int[10 ] ;
                for ( int i = 0 ; i < 10 ; i++ ){
                        array1[i]=  message[i+10];
                }
                SortNew.BubbleSort(array1) ;
                for ( int i=  0 ; i < 10 ; i++ ){
                        message[i+10]=  array1[i];
                }
               // MPI.COMM_WORLD.send(message,20, MPI.INT, next, tag);
            
           }	
	   else {
	   	System.out.println(myrank); 
		 for ( int i=  0 ; i < 20 ; i++ ){
                       // message[i]=  array1[i];
                 	System.out.println ( "finished : " + message[i]) ; 
		}  	

	   }	
	
	    MPI.COMM_WORLD.send(message, 21, MPI.INT, next, tag);
	    if (0 == message[20]) {
		System.out.println("Process " + myrank + " exiting");
		break;
	    }
	}

	/* The last process does one extra send to process 0, which needs
	   to be received before the program can exit */

	if (0 == myrank) {
	    MPI.COMM_WORLD.recv(message,21, MPI.INT, prev, tag);
	}
    
	MPI.Finalize();
    }
}
