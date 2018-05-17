/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordlist;
import java.util.*;
import java.util.ArrayList;

/**
 *
 * @author heromachine
 */
public class DocTable {
    int FileCount; 
    public HashMap <String, document> docTable = new HashMap<>();
    
    public void addDoc(String summ, String DocID){
        document newDoc = new document();
        newDoc.docID = DocID;
        newDoc.summary = summ;
        docTable.put(DocID, newDoc);
    }
    
    public void calDocLength(ListWords wList)
    {
        FileCount = docTable.size();
        //System.out.print("\n\nFILECOUNT: -->"+docT.FileCount+ "<--");
         for(Map.Entry<String, wordNode> e : wList.Words.entrySet())
        { 
            float TFF = 0;
            float FileDfi = 0;
            
            wordNode temp = e.getValue();           
            
            for(Map.Entry<String, DocLink> w : temp.x.entrySet())
            {        
                TFF = w.getValue().wordFreq/wList.Tfi.get(w.getKey());
                FileDfi = FileCount/temp.Dfi; 
                FileDfi = (float)(Math.log(FileDfi)/Math.log(2));                                          
                
                
                document tempDoc =  docTable.get((String)w.getKey());
                tempDoc.doclength += Math.pow((TFF * FileDfi), 2);
                docTable.replace((String)w.getKey(), tempDoc);
                /*System.out.println("Document key#: " + w.getKey() + "\nDocument \'docnum\'#: " + tempDoc.docID + 
                        " ...DocLength = " + docT.docTable.get(w.getKey()).doclength);*/
                               
            }
            
            
            
        }
         
        
        for(Map.Entry<String, document> d : docTable.entrySet())
        {
            
           //System.out.println("Document Number "+ d.getKey());
            if (d.getValue().doclength > 0 && docTable.containsKey(d.getKey()))
            {
                document tempDoc = docTable.get(d.getKey());
                tempDoc.doclength = (float)Math.sqrt(d.getValue().doclength);
                docTable.replace(d.getKey(), tempDoc);
               
            }
        }
    }
        
}

