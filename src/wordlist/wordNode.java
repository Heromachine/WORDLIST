/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordlist;
import java.util.*;
/**
 *
 * @author heromachine
 */
public class wordNode {
    String name;
    boolean keep; 
 
    
    
    HashMap<String, DocLink> x = new HashMap();    
    int Dfi = x.size();
    
    
    public void addDocLink(String dnum, int pos, int Freq){
        DocLink newDoc = new DocLink();
        newDoc.docnum = dnum;
        newDoc.wordFreq = Freq;
        
        x.put(dnum, newDoc);
    }
}