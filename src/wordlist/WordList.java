/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordlist;
import java.util.Vector;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;

/**
 *
 * @author Jessi
 */
public class WordList 
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        // TODO code application logic here
        
        
        Path directory =  Paths.get("20_newsgroups");        
        FileReadParser FRP = new FileReadParser();
        
//        if (FRP.isDirectory(directory));
//        System.out.println("DIRECTORY");
        
        if (FRP.isExistingDirectory(directory.getFileName().toString()))
        {
            
            //System.out.println("\nDIRECTORY: "+directory.getFileName().toString());            
            File file = new File(directory.getFileName().toString());            
            String[] files = file.list();
            for (String string : files)
            {
                
                //System.out.println("FOUND: [" + string + "]");
                
                Path folder =  Paths.get(string); 
                if (FRP.isExistingDirectory(directory.getFileName().toString() + "/"+folder.getFileName().toString()))
                {
                    //System.out.println("\tDIRECTORY: " + string);
                } 
                else 
                if (FRP.isExistingFile(directory.getFileName().toString() + "/"+folder.getFileName().toString()))
                {   
                    //System.out.println("\tFILE: " + string);        
                    try 
                    {
                        ListWords MyList = new ListWords();
                        MyList.populateStopWords();                      
                        
                        
                        String [] ps = FRP.parserDoc(directory.getFileName().toString() + "/"+string);
                        for (String strings: ps)
                        {
                            //strings.toLowerCase();
                            MyList.parseDoc(strings, directory.getFileName().toString()); 
                            
                            wordNode TempNode = MyList.Words.get(strings);
                            
                        
                        }
                        
                        


                    }catch (IOException ex) 
                    {

                    }
                  
                }
                else
                {
                   // System.out.println("ERROR\n ");
                
                }
            }
        }
    }
}
    

