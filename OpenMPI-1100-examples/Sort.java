/*
 * Copyright (c) 2011      Cisco Systems, Inc.  All rights reserved.
 *
 * Simple ring test program
 */

import mpi.* ;
 
class Ring {
    static public void main(String[] args) throws MPIException {
	
	
	MPI.Init(args) ;
      
	int source;  // Rank of sender
	int dest;    // Rank of receiver 
	int tag=50;  // Tag for messages	
	int next;
	int prev;
	int message[]	 = {2, 3, 4, 12, 11, 1, 99, 999,65,43,23, 989, 23, 233, 0, 4, 5 ,332,66, 776} ;

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
	    message[0] = 10;

	    System.out.println("Process 0 sending " + message + " to rank " + next + " (" + size + " processes in ring) -"+ tag); 
	    MPI.COMM_WORLD.send(message[0], 10,20 ,  MPI.INT, next, tag); 
	}

	/* Pass the message around the ring.  The exit mechanism works as
	   follows: the message (a positive integer) is passed around the
	   ring.  Each time it passes rank 0, it is decremented.  When
	   each processes receives a message containing a 0 value, it
	   passes the message on to the next process and then quits.  By
	   passing the 0 message first, every process gets the 0 message
	   and can quit normally. */

	while (true) {
	    MPI.COMM_WORLD.recv(message[0],1,  20, MPI.INT, prev, tag);

	    if (0 == myrank) {
		--message[0];
		System.out.println("Process 0 decremented value: " + message[0] + " -"+ tag);
	    }else {
//		++message[0];
		System.out.println("Process "+myrank+ " decremented value: " + message[0] + " -"+ tag);

	    }

	    MPI.COMM_WORLD.send(message[0], 1,20, MPI.INT, next, tag);
	    if (0 == message[0]) {
		System.out.println("Process " + myrank + " exiting");
		break;
	    }
	}

	/* The last process does one extra send to process 0, which needs
	   to be received before the program can exit */

	if (0 == myrank) {
	    MPI.COMM_WORLD.recv(message[0], 1, MPI.INT, prev, tag);
	}
    
	MPI.Finalize();
    }
}
