import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

//http://java2novice.com/java-sorting-algorithms/selection-sort/
public class SelectionSortONEserial {

     public static void selectionSort( int [ ] a )
	{
	     for (int i = 0; i < a.length - 1; i++)
	         {
	             int index = i;
	             for (int j = i + 1; j < a.length; j++){
	                 if (a[j] < a[index]){
	                     index = j;
	                 }
	             }        
	       
	             int smallerNumber = a[index]; 
	             a[index] = a[i];
	             a[i] = smallerNumber;
	         }
	} 	
    
    private static void writeTextFile(File file, String text) {
		// physically write file
    	try {
    		FileOutputStream fo = new FileOutputStream(file);
    		fo.write(text.getBytes());
    		fo.close();
    	} catch (IOException e) {
    	}
    }


    public static void main(String[] args)  {
    	long time = 0 ; 
    	StopWatch st = new StopWatch(); 
    	String[] filenames = {"med.3.killer.1000.txt","med.3.killer.10000.txt","rand.dups.1000.txt","rand.dups.10000.txt","rand.dups.100000.txt",
    			"rand.no.dups.1000.txt","rand.no.dups.10000.txt","rand.no.dups.100000.txt", "rand.steps.1000.txt","rand.steps.10000.txt",
    			"rand.steps.100000.txt", "rev.partial.1000.txt","rev.partial.10000.txt","rev.partial.100000.txt", "rev.saw.1000.txt","rev.saw.10000.txt",
    			"rev.saw.100000.txt", "seq.partial.1000.txt","seq.partial.10000.txt","seq.partial.100000.txt","seq.saw.1000.txt",
    			"seq.saw.10000.txt", "seq.saw.100000.txt"};
    	int ia = Integer.parseInt(args[0]) ; 
    	File savedFile = new File ( filenames[ia]+"SELECTIONSERIAL.txt") ; 
    	int[] sizeOfArray = {1000,10000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000,1000,10000,100000};  
    int filesize = sizeOfArray[ia];
    	int[] array = new int[sizeOfArray[ia]] ; 
    	try {
 			
    		File file = new File(filenames[ia]);
             	BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             	StringBuffer tmp = new StringBuffer();
             	
             	int c = 0 ; 
             	while (input.ready()) {   
             		String line = input.readLine() ;
             		int in = Integer.parseInt(line);
             		array[c] = in ; 
             		c++ ; 

             	}
             	st.start() ; 
             	SelectionSortONEserial.selectionSort(array) ; 
             	String text = "" ; 
             	for ( int  i= 0 ; i< filesize ; i++ ){
          		//  System.out.println ( "Finally : " + array[i]+ " - index *" + i) ;
          		  text+= array[i] + "\n" ;
          	   }
          	st.stop() ;    
          	time = st.getElapsedTime();
          	writeTextFile((savedFile), text ) ;
            System.out.println ( "Sorted array save to " + savedFile.getName() );
		        System.out.println ("Time it take to sort "+ file.getName() +" : "+time + " ms"); 
            
     } catch (Exception e) {
            
     } 
    	
    
	
    }
}
