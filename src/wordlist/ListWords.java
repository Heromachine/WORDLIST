/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordlist;
import java.util.*;
import java.lang.Object;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import org.apache.lucene.analysis.*;

import org.apache.lucene.util.Version;

import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import org.apache.lucene.util.AttributeSource;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.analysis.TokenFilter;

import java.io.Reader;
import java.io.StringReader;

import java.util.Map;
import org.apache.lucene.*;


public class ListWords {
    Set<String> stopWords = new HashSet<String>();
    //CharArraySet stopWords = EnglishAnalyzer.getDefaultStopSet();
    
    int StrLength = 0;
    int index = 0;
    boolean firstrun = true;
    HashSet Result = new HashSet();    
    ArrayList <String> retStr;    
    ArrayList <String> SearchPostFix = new ArrayList();    
    //HashMap Qwords;
    
    Map<String, HashMap<String, Integer>> WORDZ =  new HashMap();
    
    
    HashMap <String, wordNode> Words = new HashMap();    
    HashMap<String, Integer> Tfi = new HashMap<>(); 
    
    
    float QLength = 0; 
    HashMap<String, Integer> QFrequency = new HashMap();
    float Numerator= 0;
    public String ItoP = ""; 
    boolean V = true; 
    HashMap<String, wordNode> Copy = new HashMap<String, wordNode>(); 
    
    
    Map<String, Map<String, String>> Wordsz = 
    new HashMap<String, Map<String, String>>();
    
    public ListWords(){
        populateStopWords();
    }
    
    public void populateStopWords(){
        stopWords.add("a");stopWords.add("about");stopWords.add("above");stopWords.add("after");stopWords.add("again");stopWords.add("against");stopWords.add("all");
        stopWords.add("am");stopWords.add("an");stopWords.add("and");stopWords.add("any");stopWords.add("are");stopWords.add("aren't");stopWords.add("as");stopWords.add("at");stopWords.add("be");
        stopWords.add("because");stopWords.add("been");stopWords.add("before");stopWords.add("being");stopWords.add("below");stopWords.add("between");stopWords.add("both");
        stopWords.add("but");stopWords.add("by");stopWords.add("can't");stopWords.add("cannot");stopWords.add("could");stopWords.add("couldn't");stopWords.add("did");stopWords.add("didn't");
        stopWords.add("do");stopWords.add("does");stopWords.add("doesn't");stopWords.add("doing");stopWords.add("don't");stopWords.add("down");stopWords.add("during");stopWords.add("each");
        stopWords.add("few");stopWords.add("for");stopWords.add("from");stopWords.add("further");stopWords.add("had");stopWords.add("hadn't");stopWords.add("has");stopWords.add("hasn't");stopWords.add("have");
        stopWords.add("haven't");stopWords.add("having");stopWords.add("he");stopWords.add("he'd");stopWords.add("he'll");stopWords.add("he's");stopWords.add("her");stopWords.add("here");stopWords.add("here's");
        stopWords.add("her's");stopWords.add("herself");stopWords.add("him");stopWords.add("himself");stopWords.add("his");stopWords.add("how");stopWords.add("how's");
        stopWords.add("i");stopWords.add("i'd");stopWords.add("i'll");stopWords.add("i'm");stopWords.add("i've");stopWords.add("if");stopWords.add("in");stopWords.add("into");
        stopWords.add("is");stopWords.add("isn't");stopWords.add("it");stopWords.add("it's");stopWords.add("its");stopWords.add("itself");stopWords.add("let's");stopWords.add("me");stopWords.add("more");stopWords.add("most");stopWords.add("mustn't");
        stopWords.add("my");stopWords.add("myself");stopWords.add("no");stopWords.add("nor");stopWords.add("not");stopWords.add("of");stopWords.add("off");stopWords.add("on");stopWords.add("once");stopWords.add("only");
        stopWords.add("or");stopWords.add("other");stopWords.add("ought");stopWords.add("our");stopWords.add("ours");stopWords.add("ourselves");stopWords.add("out");stopWords.add("over");stopWords.add("own");
        stopWords.add("same");stopWords.add("shan't");stopWords.add("she");stopWords.add("she'd");stopWords.add("she'll");stopWords.add("she's");stopWords.add("should");stopWords.add("shouldn't");stopWords.add("so");stopWords.add("some");
        stopWords.add("such");stopWords.add("than");stopWords.add("that");stopWords.add("that's");stopWords.add("the");stopWords.add("their");stopWords.add("their's");stopWords.add("them");stopWords.add("themselves");
        stopWords.add("then");stopWords.add("there");stopWords.add("there's");stopWords.add("these");stopWords.add("they");stopWords.add("they'd");stopWords.add("they're");stopWords.add("they'll");stopWords.add("they've");
        stopWords.add("this");stopWords.add("those");stopWords.add("though");stopWords.add("to");stopWords.add("too");stopWords.add("under");stopWords.add("until");stopWords.add("up");stopWords.add("very");stopWords.add("was");
        stopWords.add("wasn't");stopWords.add("we");stopWords.add("we'd");stopWords.add("we'll");stopWords.add("we're");stopWords.add("we've");stopWords.add("were");stopWords.add("weren't");stopWords.add("what");stopWords.add("what's");
        stopWords.add("when");stopWords.add("when's");stopWords.add("where");stopWords.add("where's");stopWords.add("which");stopWords.add("while");stopWords.add("who");stopWords.add("who's");stopWords.add("whom");stopWords.add("why");
        stopWords.add("why's");stopWords.add("with");stopWords.add("won't");stopWords.add("would");stopWords.add("wouldn't");stopWords.add("you");stopWords.add("you'd");stopWords.add("you'll");stopWords.add("you're");stopWords.add("you've");
        stopWords.add("your");stopWords.add("yours");stopWords.add("yourself");stopWords.add("yourselves");
    }
    public void addWord02(String DocID, String tempWord, int preIn)
    {      
        if (!WORDZ.containsKey(tempWord))
        {
            HashMap<String, Integer> temp = new HashMap();
            temp.put(DocID, 1);
            WORDZ.put(tempWord, temp);         
        }        
        else 
        {
           if (WORDZ.get(tempWord).containsKey(DocID))
           {               
               HashMap<String, Integer> temp = new HashMap();
               temp.put(DocID, WORDZ.get(tempWord).get(DocID).intValue() + 1 );
               ((Map)WORDZ.get(tempWord)).replace(DocID, WORDZ.get(tempWord).get(DocID).intValue()+1);           
           }
           else 
           if(!WORDZ.get(tempWord).containsKey(DocID))
           {
               HashMap<String, Integer> temp = new HashMap();
               temp.put(DocID, WORDZ.get(tempWord).get(DocID).intValue() + 1 );
               ((Map)WORDZ.get(tempWord)).put(DocID, 1);           
           } 
        }       
    }    

    public void addWord(String DocID, String tempWord, int preIn){
        wordNode newWord = new wordNode();
        newWord.name = tempWord;
        newWord.Dfi = 1;            
        DocLink newDocLink = new DocLink();
        newDocLink.docnum = DocID;
        //System.out.println("Setting DocID to: " + newDocLink.docnum);
        newDocLink.wordFreq = 1;
        if(Tfi.get(DocID) != null){
            if(newDocLink.wordFreq > Tfi.get(DocID)){
                Tfi.replace(DocID, newDocLink.wordFreq);
            }
        }
        else {
            Tfi.put(DocID, newDocLink.wordFreq);
        }
                    


                    
        newWord.x.put(DocID, newDocLink);
        //addWord(tempWord, 0, DocID);
        Words.put(tempWord, newWord);
    }    
    public void appendWord(String DocID, String tempWord, int preIn){
        wordNode tempNode; //= new wordNode();
        DocLink tempDocLink; //= new DocLink();                    
        tempNode = (wordNode) Words.get(tempWord);
        if(!tempNode.x.containsKey(DocID)){
            tempNode.x.put(DocID, new DocLink());
            tempNode.Dfi++;
        }  
        
        tempDocLink = (DocLink) tempNode.x.get(DocID);
        tempDocLink.docnum = DocID;
                   
        tempDocLink.wordFreq++;
        
        if(Tfi.get(DocID) != null){
            if(tempDocLink.wordFreq > Tfi.get(DocID)){
                Tfi.replace(DocID, tempDocLink.wordFreq);
            }
        }
        else {
            Tfi.put(DocID, tempDocLink.wordFreq);
        }
        
                    
        tempNode.x.replace(DocID, tempDocLink);
                    
        Words.replace(tempWord, tempNode);
    }    
    public void parseDoc(String content, String DocID) throws IOException {
        
        String AppendCont = content + " Appended";
        AppendCont = AppendCont.toLowerCase();
        StringBuilder buildcont = new StringBuilder();
        buildcont.append(AppendCont);        
        
        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_30, new StringReader(buildcont.toString()));
        tokenStream = new StopFilter(true, tokenStream, stopWords);
        tokenStream = new PorterStemFilter(tokenStream);

        StringBuilder sb = new StringBuilder();
        TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
        try {
            while (tokenStream.incrementToken()){
                if (sb.length() > 0){
                    sb.append(" ");
                }
                sb.append(termAttr.term());
            }
        } catch(IOException e){
            //nothing
        }
        
        //StringBuilder sb = new StringBuilder();
        //sb.append(buildcont);
        
        int preIn = 0;
        String tempWord = "";
        
        //remove puntuation
        for(int i=0; i<sb.length(); i++){
            if(sb.charAt(i) == ' ' && sb.charAt(i+1) == ' '){
                sb.deleteCharAt(i);
                i--;
            }
            else if(sb.charAt(i) == '.' || sb.charAt(i) == ',' || sb.charAt(i) == ':' || 
                    sb.charAt(i) == ';' || sb.charAt(i) == '?' || sb.charAt(i) == '\'' || 
                    sb.charAt(i) == '\n' || sb.charAt(i) == '&' || sb.charAt(i) == '!' ||
                    sb.charAt(i) == '(' || sb.charAt(i) == ')' || sb.charAt(i) == '\"' ||
                    sb.charAt(i) == '_' || sb.charAt(i) == '~' || sb.charAt(i) == '`' ||
                    sb.charAt(i) == '<' || sb.charAt(i) == '>'){
                sb.deleteCharAt(i);
                sb.insert(i, ' ');
                //i--;
            }
        }
        
        
        for(int i=preIn; i<sb.length(); i++){
            if(sb.charAt(i) != ' '){
                tempWord += sb.charAt(i);
            }
            else {
                if(!Words.containsKey(tempWord)){
                    addWord(DocID, tempWord, preIn);
                    tempWord = "";
                    preIn = i+1;
                }
                else {
                    appendWord(DocID, tempWord, preIn);
                    
                    tempWord = "";
                    preIn = i+1;
                }
            }
   
        }
    }

     public void DocNumIntersections(Stack temp)
    {
        for(int s1 = 0; s1 < temp.size(); s1++)
        {
            wordNode tempNode = (wordNode) Words.get(temp.pop());
            ArrayList <String> KeyList = (ArrayList)tempNode.x.keySet();

            HashSet tempSet = new HashSet();

            for(int s2 = 0; s2 < tempNode.x.size(); s2++)
            {
                    DocLink tempDocLink = (DocLink)tempNode.x.get(KeyList.get(s2));
                    tempSet.add(tempDocLink.docnum);
            }

            Result.retainAll(tempSet);
        }
    }   
    public String Infix2PostFix(){
        //In2Post function: Transform string to logic-operable format...        
        Stack StackOperators = new Stack();
        
        for (int i = 0; i < retStr.size(); i++)
        {//If Operator Start Arrangment
            if (retStr.get(i) == " " 
                    || retStr.get(i) == "+"
                    || retStr.get(i) == "("
                    || retStr.get(i) == ")"
                    || retStr.get(i) == "!"
                )
            {//if the Operator Stack is empty add the element in the Infix Search Index                  
                if (StackOperators.isEmpty() == true)
                {
                    StackOperators.push(retStr.get(i));             
                }
                else 
                {//if the Infix Search Index is the same operator as the first slement in the Stack, add it to the Stack of operators.  
                    if (retStr.get(i) == StackOperators.lastElement())
                    {                   
                        StackOperators.push(retStr.get(i));
                    }
                    else
                    {//CHECK EACH OPERATOR IN QUEREY 
                       if (retStr.get(i) == "(")
                        {
                            if (StackOperators.lastElement() == "+" || 
                                    StackOperators.lastElement() == " "||
                                    StackOperators.lastElement() == "!")
                            {//CHECK EACH OPERATOR IN 
                               StackOperators.push("(");
                            }
                        }
                        else if (retStr.get(i) == ")")
                        {                            
                            while (StackOperators.lastElement()!= "(")
                            {
                                SearchPostFix.add((String) StackOperators.pop());
                                if (StackOperators.lastElement() == "(")
                                {
                                    StackOperators.pop();
                                    break; 
                                }                            
                            }                 
                        }    
                        else if (retStr.get(i) == "+")
                        {                            
                              StackOperators.push("+");
                        }
                        else if (retStr.get(i) == " ")
                        {   
                            if(StackOperators.lastElement() == "+")
                            {                                
                                SearchPostFix.add((String) StackOperators.pop());
                                StackOperators.push(" ");
                            }
                            else if (StackOperators.lastElement() == "!"  )
                            {   
                                SearchPostFix.add((String) StackOperators.pop());
                                StackOperators.push(" ");
                            }
                            else if (StackOperators.lastElement() == "(")
                            {
                                StackOperators.push(" ");
                            }
                        }
                        else if (retStr.get(i) == "!")
                        {
                            if (retStr.get(i+1) == "!" )
                            {
                                retStr.remove(i);
                                retStr.remove(i+1);
                            }                    
                            if(StackOperators.lastElement() == "+")
                            {
                                SearchPostFix.add((String)StackOperators.pop());
                                StackOperators.push("!");
                            }
                            else if (StackOperators.lastElement() == " " )
                            { 
                                SearchPostFix.add((String)StackOperators.pop());
                                StackOperators.push("!");
                            }
                            else if  (StackOperators.lastElement() == "(")
                            {                                
                                StackOperators.push("!");
                            }
                        }
                    }                
                }
            }  

            else 
            { 
                if (retStr.get(i) == "\"")
                {
                    SearchPostFix.add((String) retStr.get(i));
                    i +=1;                    
                    while (retStr.get(i)!= "\"")
                    {                       
                        SearchPostFix.add((String) retStr.get(i));
                        i +=1;                       
                    }            
                    SearchPostFix.add((String) retStr.get(i)); 
                }
                else
                {
                    SearchPostFix.add((String) retStr.get(i));
                }
            }
        }
        //PUT REMAINDING OPERATORS IN THE LIST
        while (StackOperators.empty() == false)
        {
            SearchPostFix.add((String) StackOperators.pop());
        }  
        /*for (int i = 0; i < SearchPostFix.size(); i++)
        {
            System.out.print(SearchPostFix.get(i));
        }
        System.out.print(" -> After inFix to postFix\n");*/
        
        return Logic();
    
    }   
    public String Logic(){
        
        HashSet Result = new HashSet();        
        
        
        if (SearchPostFix.size() > 1)
        {
            for (int i = 0; i < SearchPostFix.size(); i++)
            {
                if (Result.isEmpty())
                {                    
                    wordNode tempNode = new wordNode();
                    HashSet tempSet = new HashSet(); 
                    
                    if (SearchPostFix.get(i) == "\"")                         
                    {   
                        if(Words.containsKey(SearchPostFix.get(i+1)))
                        {
                            tempNode = Words.get(SearchPostFix.get(i+1));
                            for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                            {   
                                tempSet.add(entry.getKey());
                            }  
                            Result.addAll(tempSet); 
                        }
                        
                    }
                    else
                    {
                        if(Words.containsKey(SearchPostFix.get(i)))
                        {
                            tempNode = Words.get(SearchPostFix.get(i));
                            for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                            {   
                                tempSet.add(entry.getKey());
                            }
                            Result.addAll(tempSet); 
                        }
                    } 
                    
                    
                }
                if(SearchPostFix.get(i) == "+")//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                {
                    int j = i - 2;
                    int z = 0;
                    HashSet FirstWord = new HashSet(); 
                    
                    if (SearchPostFix.get(i-1) == "\"")
                    {  
                        HashSet TempSet = new HashSet();
                        Stack<String> tempStack = new Stack<String>();                        
                        wordNode tempNode = new wordNode();

                        //PUT WORDS IN THE FIRST QUOTE INSIDE A STACK
                        while (SearchPostFix.get(j) != "\"")
                        {                                           
                            if (Words.containsKey(SearchPostFix.get(j)))
                            {
                                tempStack.push(SearchPostFix.get(j));              
                            }
                            --j;               
                        }
                        while (tempStack.empty() == false)
                        {                                  
                            //Get each Word into a tempNode
                            String x = tempStack.pop();
                            if (Words.containsKey(x) == false)
                            {                                           
                                tempStack.pop();                                            
                                continue;
                            }
                            else 
                            {
                                tempNode = new wordNode();
                                tempNode = Words.get(x);                                         
                            }

                            for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                            {   
                                FirstWord.add(entry.getKey());
                            }                                                     
                        }
                        
                        if (SearchPostFix.get(j-1) == "\"")
                            {                                        
                                z = j;
                                tempStack = new Stack();
                                while (SearchPostFix.get(z) != "\"")
                                {   
                                    if (Words.containsKey(SearchPostFix.get(z)))
                                    {
                                        tempStack.push(SearchPostFix.get(z));              
                                    }
                                    --z;   
                                } 
                                
                                while (tempStack.empty() == false)
                                {                                  
                                    //Get each Word into a tempNode
                                    String x = tempStack.pop();
                                    if (Words.containsKey(x) == false)
                                    {                                           
                                        tempStack.pop();                                            
                                        continue;
                                    }
                                    else 
                                    {
                                        tempNode = new wordNode();
                                        tempNode = Words.get(x);                                         
                                    }

                                    for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                    {   
                                        TempSet.add(entry.getKey());
                                    }
                                                               
                                } 
                                FirstWord.retainAll(TempSet);
                                Result.addAll(FirstWord);
   
                            }
                            else if (SearchPostFix.get(j-1) != "\"" && Words.containsKey(SearchPostFix.get(j-1)))
                            {   
                                
                                tempNode = Words.get(SearchPostFix.get(i-1));                        
                                TempSet = new HashSet();
                                for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                {             
                                    TempSet.add(entry.getKey());
                                }
                                FirstWord.retainAll(TempSet);                      
                            }   Result.addAll(FirstWord);                         
                        }
                        else if (Words.containsKey(SearchPostFix.get(i-1)))
                        {   
                            HashSet tempSet = new HashSet();                                    
                            wordNode tempNode = new wordNode();
                            tempNode = Words.get(SearchPostFix.get(i-1));
                            tempSet = new HashSet();
                            for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                            {             
                                tempSet.add(entry.getKey());
                            }
                            Result.retainAll(tempSet);  

                            if (SearchPostFix.get(i- 2) == "\"")
                            {
                                Stack<String> tempStack = new Stack<String>(); 
                                
                                z = i-3;
                                tempStack = new Stack();
                                while (SearchPostFix.get(z) != "\"")
                                {
                                    if (Words.containsKey(SearchPostFix.get(z)))
                                    {
                                        tempStack.push(SearchPostFix.get(z));              
                                    }
                                    --z;   
                                } 
                                
                                while (tempStack.empty() == false)
                                {                                  
                                    //Get each Word into a tempNode
                                    String x = tempStack.pop();
                                    if (Words.containsKey(x) == false)
                                    {                                           
                                        tempStack.pop();                                            
                                        continue;
                                    }
                                    else 
                                    {
                                        tempNode = new wordNode();
                                        tempNode = Words.get(x);                                         
                                    }

                                    for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                    {   
                                        tempSet.add(entry.getKey());
                                    }
                                    Result.retainAll(tempSet);                            
                                }   
                            }
                        }
                    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                    }
                    else if (SearchPostFix.get(i) == "!")//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    {
                        int j = i - 2;
                        int z = 0;
                        HashSet RemovedPhrase = new HashSet();
                        HashSet KeptPhrase  = new HashSet();
                        
                        
                        if (SearchPostFix.get(i-1) == "\"")
                        {                                   
                                Stack<String> tempStack = new Stack<String>();                                                                   
                                wordNode tempNode = new wordNode();

                                //PUT WORDS IN THE FIRST QUOTE INSIDE A STACK
                                while (SearchPostFix.get(j) != "\"")
                                {     
                                    if (Words.containsKey(SearchPostFix.get(j)))
                                    {
                                        tempStack.push(SearchPostFix.get(j));              
                                    }
                                    --j;
                                }

                                while (tempStack.empty() == false)
                                {                                  
                                    //Get each Word into a tempNode
                                    String x = tempStack.pop();
                                    if (Words.containsKey(x) == false)
                                    {                                           
                                        tempStack.pop();                                            
                                        continue;
                                    }
                                    else 
                                    {
                                        tempNode = new wordNode();
                                        tempNode = Words.get(x);                                         
                                    }

                                    for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                    {   
                                        RemovedPhrase.add(entry.getKey());
                                    }
                                                             
                                }
                                
                                
                                
                                if (SearchPostFix.get(j-1) == "\"")
                                {                                        
                                    z = j;
                                    tempStack = new Stack();
                                    while (SearchPostFix.get(z) != "\"")
                                    {
                                        if (Words.containsKey(SearchPostFix.get(z)))
                                        {
                                            tempStack.push(SearchPostFix.get(z));              
                                        }
                                        --z;
                                    } 

                                    while (tempStack.empty() == false)
                                    {                                  
                                        //Get each Word into a tempNode
                                        String x = tempStack.pop();
                                        if (Words.containsKey(x) == false)
                                        {                                           
                                            tempStack.pop();                                            
                                            continue;
                                        }
                                        else 
                                        {
                                            tempNode = new wordNode();
                                            tempNode = Words.get(x);                                         
                                        }

                                        for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                        {   
                                            KeptPhrase.add(entry.getKey());
                                        }
                                        KeptPhrase.remove(RemovedPhrase);
                                    }   
                                    
                                }
                                else if (SearchPostFix.get(i-1) != "\"" && Words.containsKey(SearchPostFix.get(i-1)))
                                {     
                                    tempNode = Words.get(SearchPostFix.get(i-1));                               

                                    
                                    for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                    {             
                                        KeptPhrase.add(entry.getKey());
                                    }
                                    KeptPhrase.remove(RemovedPhrase);
                                    Result.addAll(KeptPhrase);
                                }                            
                        }
                        else if (Words.containsKey(SearchPostFix.get(i-1)))
                        {   
                                                             
                            wordNode tempNode = new wordNode();
                            tempNode = Words.get(SearchPostFix.get(i-1));
                            HashSet tempPhrase = new HashSet();
                            RemovedPhrase = new HashSet();
                            KeptPhrase = new HashSet();
                            for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                            {             
                                RemovedPhrase.add(entry.getKey());
                            }                             

                            if (SearchPostFix.get(i- 2) == "\"")
                            {
                                Stack<String> tempStack = new Stack<String>(); 
                                
                                z = i-3;
                                tempStack = new Stack();
                                while (SearchPostFix.get(z) != "\"")
                                {
                                    if (Words.containsKey(SearchPostFix.get(z)))
                                    {
                                        tempStack.push(SearchPostFix.get(z));              
                                    }
                                    --z;
                                } 
                                
                                while (tempStack.empty() == false)
                                {                                  
                                    //Get each Word into a tempNode
                                    String x = tempStack.pop();
                                    if (Words.containsKey(x) == false)
                                    {                                           
                                        tempStack.pop();                                            
                                        continue;
                                    }
                                    else 
                                    {
                                        tempNode = new wordNode();
                                        tempNode = Words.get(x);                                         
                                    }

                                    for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                    {   
                                        tempPhrase.add(entry.getKey());
                                    }
                                    KeptPhrase.retainAll(tempPhrase);                             
                                }
                                
                                KeptPhrase.remove(RemovedPhrase);
                                Result.addAll(KeptPhrase);
                            }
                            else if (Words.containsKey(SearchPostFix.get(i-2)))
                            {
                                wordNode tempNode2 = new wordNode();
                                tempNode2 = Words.get(SearchPostFix.get(i-2));
                                tempPhrase = new HashSet();
                                
                                
                                for(Map.Entry<String, DocLink> entry : tempNode2.x.entrySet())
                                {             
                                   tempPhrase.add(entry.getKey());
                                }                             

                                tempPhrase.removeAll(RemovedPhrase);                               
                            
                            }
                            
                            
                        }
                    ////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    }
                    else if (SearchPostFix.get(i) == " ")//|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
                    {
                        int j = i - 2;
                        int z = 0;

                        if (SearchPostFix.get(i-1) == "\"")
                        {                                   
                                Stack<String> tempStack = new Stack<String>(); 

                                HashSet tempSet = new HashSet();                                    
                                wordNode tempNode = new wordNode();

                                //PUT WORDS IN THE FIRST QUOTE INSIDE A STACK
                                while (SearchPostFix.get(j) != "\"")
                                {                                           
                                    if (Words.containsKey(SearchPostFix.get(j)))
                                    {
                                        tempStack.push(SearchPostFix.get(j));              
                                    }
                                    --j;             
                                }

                                while (tempStack.empty() == false)
                                {                                  
                                    //Get each Word into a tempNode
                                    String x = tempStack.pop();
                                    if (Words.containsKey(x) == false)
                                    {                                           
                                        tempStack.pop();                                            
                                        continue;
                                    }
                                    else 
                                    {
                                        tempNode = new wordNode();
                                        tempNode = Words.get(x);                                         
                                    }

                                    for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                    {   
                                        tempSet.add(entry.getKey());
                                    }
                                    Result.retainAll(tempSet);                            
                                }
                                if (SearchPostFix.get(j-1) == "\"")
                                {                                        
                                    z = j;
                                    tempStack = new Stack();
                                    while (SearchPostFix.get(z) != "\"")
                                    {
                                        if (Words.containsKey(SearchPostFix.get(z)))
                                        {
                                            tempStack.push(SearchPostFix.get(z));              
                                        }
                                        --z; 
                                    } 

                                    while (tempStack.empty() == false)
                                    {                                  
                                        //Get each Word into a tempNode
                                        String x = tempStack.pop();
                                        if (Words.containsKey(x) == false)
                                        {                                           
                                            tempStack.pop();                                            
                                            continue;
                                        }
                                        else 
                                        {
                                            tempNode = new wordNode();
                                            tempNode = Words.get(x);                                         
                                        }

                                        for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                        {   
                                            tempSet.add(entry.getKey());
                                        }
                                        Result.retainAll(tempSet);                            
                                    }   

                                }
                                else if (SearchPostFix.get(i-1) != "\"" && Words.containsKey(SearchPostFix.get(i-1)))
                                {     
                                    tempNode = Words.get(SearchPostFix.get(i-1));                               

                                    tempSet = new HashSet();
                                    for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                    {             
                                        tempSet.add(entry.getKey());
                                    }
                                    Result.addAll(tempSet);
                                }                            
                        }
                        else if (Words.containsKey(SearchPostFix.get(i-1)))
                        {   
                            HashSet tempSet = new HashSet();                                    
                            wordNode tempNode = new wordNode();
                            tempNode = Words.get(SearchPostFix.get(i-1));
                            tempSet = new HashSet();
                            for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                            {             
                                tempSet.add(entry.getKey());
                            }
                            Result.addAll(tempSet);  

                            if (SearchPostFix.get(i- 2) == "\"")
                            {
                                Stack<String> tempStack = new Stack<String>(); 
                                
                                z = i-3;
                                tempStack = new Stack();
                                while (SearchPostFix.get(z) != "\"")
                                {
                                    if (Words.containsKey(SearchPostFix.get(z)))
                                    {
                                        tempStack.push(SearchPostFix.get(z));              
                                    }
                                    --z; 
                                } 
                                
                                while (tempStack.empty() == false)
                                {                                  
                                    //Get each Word into a tempNode
                                    String x = tempStack.pop();
                                    if (Words.containsKey(x) == false)
                                    {                                           
                                        tempStack.pop();                                            
                                        continue;
                                    }
                                    else 
                                    {
                                        tempNode = new wordNode();
                                        tempNode = Words.get(x);                                         
                                    }

                                    for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                    {   
                                        tempSet.add(entry.getKey());
                                    }
                                    Result.addAll(tempSet);                             
                                }   
                            }
                            else 
                            {
                                tempNode = Words.get(SearchPostFix.get(0));
                                tempSet = new HashSet();
                                for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
                                {             
                                    tempSet.add(entry.getKey());
                                }
                                Result.addAll(tempSet); 
                            
                            
                            }
                        }
                    //|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
                    } else 
                    {continue;}
            }

        }else if (Words.containsKey(SearchPostFix.get(0)) && SearchPostFix.size() > 0)
        {
            
            wordNode tempNode = Words.get(SearchPostFix.get(0));
            HashSet tempSet = new HashSet();
            for(Map.Entry<String, DocLink> entry : tempNode.x.entrySet())
            {             
                tempSet.add(entry.getKey());
            }
            Result.addAll(tempSet);  

        }

        
        //System.out.println(Result.toString());
        String retItem = Result.toString();
                
        Result = new HashSet();
        retStr = new ArrayList<>();
    
        SearchPostFix = new ArrayList();
        
        return retItem;
        
}
    public void parseQuery(String srchParam, DocTable docT){
         
        String Q = "";
        int preIn = 0;
        
        StringBuilder AppendSrch = new StringBuilder(srchParam.toLowerCase() + "$");
        
        retStr = new ArrayList();
        
        //Qwords = new HashMap();
        
        //remove redundant spaces
        for(int i=0; i<AppendSrch.length(); i++){
            switch(AppendSrch.charAt(i)){
                case ' ':
                    if(AppendSrch.charAt(i+1) == ' '){
                        AppendSrch.deleteCharAt(i+1);
                        i--;
                    }
                    else if(AppendSrch.charAt(i+1) == '+'){
                        AppendSrch.deleteCharAt(i);
                        V = false;
                        i--;
                    }
                    else if(AppendSrch.charAt(i+1) == '!'){
                        AppendSrch.deleteCharAt(i);
                        V = false;
                        i--;
                    }
                    break;
                case '+':
                    V = false;
                    if(AppendSrch.charAt(i+1) == ' '){
                        AppendSrch.deleteCharAt(i+1);
                        i--;
                    }
                    else if(AppendSrch.charAt(i+1) == '!'){
                        AppendSrch.deleteCharAt(i);
                        i--;
                    }
                    break;
                case '!':
                    V = false;
                    if(AppendSrch.charAt(i+1) == ' '){
                        AppendSrch.deleteCharAt(i+1);
                        i--;
                    }
                    break;
                case '\"':
                    V = false;
                    if(AppendSrch.charAt(i+1) == ' '){
                        AppendSrch.deleteCharAt(i+1);
                        i--;
                    }
                    break;
                case '(':
                    V = false;
                    if(AppendSrch.charAt(i+1) == ' '){
                        AppendSrch.deleteCharAt(i+1);
                        i--;
                    }
                    break;
                case ')':
                    V = false;
                    if(AppendSrch.charAt(i+1) == ' '){
                        AppendSrch.deleteCharAt(i+1);
                        i--;
                    }
                    break;
                default:
                    //idk
                    break;
            }
        }
        /*System.out.print("\n");
        System.out.print(AppendSrch.toString());
        System.out.print(" -> After spaces removed\n");*/
        
        //remove puntuation
        for(int i=0; i<AppendSrch.length(); i++){
            if(AppendSrch.charAt(i) == '\"'){
                for(int z=i; z<AppendSrch.length(); z++){
                    if(AppendSrch.charAt(z) == '\"'){
                        i = z+1;
                        break;
                    }
                }
            }
            else if(AppendSrch.charAt(i) == '.' || AppendSrch.charAt(i) == ',' || AppendSrch.charAt(i) == ':' || 
                    AppendSrch.charAt(i) == ';' || AppendSrch.charAt(i) == '?' || AppendSrch.charAt(i) == '\''){
                AppendSrch.deleteCharAt(i);
                i--;
            }
            else if(i == AppendSrch.length()-2 && AppendSrch.charAt(i) == '!'){
                AppendSrch.deleteCharAt(i);
                i--;
            }
        }
        /*System.out.print(AppendSrch.toString());
        System.out.print(" -> After punctuation removal\n");*/
        
        //Populate Query hashmap
        /*for(int i=0; i<AppendSrch.length(); i++){
            if(AppendSrch.charAt(i) == ' ' || AppendSrch.charAt(i) == '+' || AppendSrch.charAt(i) == '!' ||
                    AppendSrch.charAt(i) == '\"' || AppendSrch.charAt(i) == '(' || AppendSrch.charAt(i) == '$' ||
                    AppendSrch.charAt(i) == ')'){
                
                if(!Qwords.containsKey(Q)){
                    if(!Words.containsKey(Q)){
                        //Query word is not in word list; omit from search
                        Q = "";
                    }
                    else {
                        wordNode tempNode;
                        tempNode = (wordNode) Words.get(Q);
                        
                        Qwords.put(Q, tempNode);
                        Q = "";
                    }
                }
                else {
                    //Query list already contains word; do nothing
                    Q = "";
                }
            }
            else {
                Q += AppendSrch.charAt(i);
            }
        }*/
        
        //Fill retStr list
        Q = "";
        String spc = " ",
               pls = "+",
               exl = "!",
               qot = "\"",
               opr = "(",
               cpr = ")"; 
        for(int i=0; i<AppendSrch.length(); i++){
            if(AppendSrch.charAt(i) == ' ' || AppendSrch.charAt(i) == '+' || AppendSrch.charAt(i) == '!' ||
                    AppendSrch.charAt(i) == '\"' || AppendSrch.charAt(i) == '(' || AppendSrch.charAt(i) == '$' ||
                    AppendSrch.charAt(i) == ')'){
                if(Q == ""){
                    switch(AppendSrch.charAt(i)){
                        case ' ':
                            retStr.add(spc);
                            break;
                        case '+':
                            retStr.add(pls);
                            break;
                        case '!':
                            retStr.add(exl);
                            break;
                        case '\"':
                            retStr.add(qot);
                            break;
                        case '(':
                            retStr.add(opr);
                            break;
                        case ')':
                            retStr.add(cpr);
                            break;
                        default:
                            break;
                    }
                }
                else {
                    retStr.add(Q);
                    if(AppendSrch.charAt(i) != '$'){
                        switch(AppendSrch.charAt(i)){
                            case ' ':
                                retStr.add(spc);
                                break;
                            case '+':
                                retStr.add(pls);
                                break;
                            case '!':
                                retStr.add(exl);
                                break;
                            case '\"':
                                retStr.add(qot);
                                break;
                            case '(':
                                retStr.add(opr);
                                break;
                            case ')':
                                retStr.add(cpr);
                                break;
                            default:
                                break;
                        }
                    }
                    Q = "";
                }
            }
            else {
                Q += AppendSrch.charAt(i);
            }
        }
        
        /*for(int i=0; i<retStr.size(); i++){
            System.out.print(retStr.get(i));
        }
        System.out.print(" -> Final output from String list\n");
        System.out.print("\n");*/
        
        if (V)
        {
            StemStopVectorQuery();
            System.out.println("VECTOR SEARCH INITIATED!!!!!!!!!!!!!!!!!!!!!!!!!!");
            calculateQLength(docT);
        }
        else 
        {
            StemStopBoolQuery();
            System.out.println("BOOLEAN SEARCH INITIATED-----------------------------------");
            ItoP = Infix2PostFix();  
        }     
       
    }
    public void StemStopVectorQuery(){
        String ZCV = retStr.toString();
        ArrayList <String> backToRet = new ArrayList<>();
        
        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_30, new StringReader(ZCV));
        tokenStream = new StopFilter(true, tokenStream, stopWords);
        tokenStream = new PorterStemFilter(tokenStream);

        StringBuilder AppendSrch = new StringBuilder();
        TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
        try {
            while (tokenStream.incrementToken()){
                if (AppendSrch.length() > 0){
                    //AppendSrch.append(" ");
                    backToRet.add(" ");
                }
                //AppendSrch.append(termAttr.term());
                backToRet.add(termAttr.term());
            }
        } catch(IOException e){
            //nothing
        }
        retStr = backToRet;
        
        
    }
    public void StemStopBoolQuery(){
        String ZCV = retStr.toString();
        ArrayList <String> backToRet = new ArrayList<>();
        
        TokenStream tokenStream = new StandardTokenizer(Version.LUCENE_30, new StringReader(ZCV));
        tokenStream = new StopFilter(true, tokenStream, stopWords);
        tokenStream = new PorterStemFilter(tokenStream);

        StringBuilder AppendSrch = new StringBuilder();
        TermAttribute termAttr = tokenStream.getAttribute(TermAttribute.class);
        try {
            while (tokenStream.incrementToken()){
                if (AppendSrch.length() > 0){
                    //AppendSrch.append(" ");
                    backToRet.add(termAttr.term());
                }
                //AppendSrch.append(termAttr.term());
                backToRet.add(termAttr.term());
            }
        } catch(IOException e){
            //nothing
        }
        retStr = backToRet;
    }
    public void calculateQLength(DocTable docT){ 
        boolean AOW = false ;  
        
        for (int y = 0; y < retStr.size(); y++)
        {
            if (Words.containsKey(retStr.get(y)))
            {
                AOW = true; 
            }

        
        }
        
        if (AOW)
        {
            for (int i = 0; i < retStr.size(); i++)
            {
                if (retStr.get(i)==  "+" || retStr.get(i)==  " " || retStr.get(i)==  "\"" || retStr.get(i)==  "!")
                {

                } 
                else 
                {
                    if (QFrequency.containsKey(retStr.get(i)))
                    {           
                       // x += QFrequency.get(retStr.get(i));                


                    }
                    else {



                       QFrequency.put(retStr.get(i), 1); 


                    }

                }
            }

            for(Map.Entry<String, Integer> e : QFrequency.entrySet())
            {            
                QLength += Math.pow(e.getValue(), 2);
            }

            QLength = (float)Math.sqrt(QLength);   

            System.out.print("\nQLength-->"+QLength + "\n");
            DisplaySim(docT);
        }
        else 
        {
        
            ItoP = "[]";
        }
    }
    public void DisplaySim(DocTable DocT){

        ArrayList<Float> SortedArray = new ArrayList(); 
        //Float[] x;
        HashMap<Float, String> unsort = new HashMap<Float, String>();
        for(Map.Entry<String, document> q : DocT.docTable.entrySet())
        {
            unsort.put(Sim(DocT, q.getValue().docID), q.getKey());
        }
        float[] sortedList = new float[unsort.size()];
        ///////////////////////////////////////////////////////////////////////////////////
        
        for(Map.Entry<Float, String> e : unsort.entrySet()){
            SortedArray.add(e.getKey());
        }
        for(int j=0; j<SortedArray.size(); j++){
            sortedList[j] = SortedArray.get(j);
        }
        
        Arrays.sort(sortedList);
        
        Stack<Float> tempStack = new Stack<>();
        
        for (int x = 1; x < sortedList.length; x++)
        {
        
            tempStack.push(sortedList[x]);
        }
        tempStack.pop();
        
        HashSet results = new HashSet();
        for (int i = 0; i < sortedList.length-2; i++)
        {
            if (unsort.containsKey(sortedList[i]))
            {
                float z = tempStack.pop();
                String y = unsort.get(z);
                
                results.add(y);
                
                System.out.print("DOCT:" +y + " Weight: "+ Math.floor(z*100)+"% relevancy\n");
                System.out.print(DocT.docTable.get(y).summary +"\n\n======================================================================\n");
                
                
            }
        }
        //System.out.println(sortedList);
        ItoP = results.toString();
        
    }
    public float Sim(DocTable docT, String docID){        
        Copy = new HashMap<>();
        Numerator = 0;
        for(Map.Entry<String, Integer> q : QFrequency.entrySet())
        {            
            Copy.put(q.getKey(),  Words.get(q.getKey()));        
        }       
            
        for(Map.Entry<String, wordNode> e : Copy.entrySet())
        { 
            float TFF = 0;
            float FileDfi = 0;
            
            wordNode temp = e.getValue(); 
            //System.out.println("NEW WORD  " + e.getKey() + " @doc: " + docID + "   ________________________________________________________\n\n");
            
 
                //System.out.println("DocID = " + docID + " ...w.getVal.docnum = " + w.getValue().docnum);
            if (temp.x.containsKey(docID))
            {
                //System.out.println(" " + docID + " is equal to " + w.getValue().docnum);
//                System.out.println(e.getKey());
//                System.out.println("FileCount "+docT.FileCount);
//                System.out.println("DocFreq "+ temp.Dfi);
                TFF = temp.x.get(docID).wordFreq/Tfi.get(docID);

                FileDfi = docT.FileCount/temp.Dfi; 

                FileDfi = (float)((Math.log(FileDfi)/Math.log(2)));                                          

                Numerator += (QFrequency.get(e.getKey())* TFF * FileDfi); 
            }
                
        }
        //System.out.println("This document: " + docID + " ...QLength = " + QLength + " & Doc Length = " + docT.docTable.get(docID).doclength);
        float Denominator = docT.docTable.get(docID).doclength * QLength;
        //System.out.print("\n n AND d: "+Numerator+ "/"+ Denominator +"\n");
        float Simularity = Numerator/Denominator;
        
        
            
        return Simularity;
    }
    
    
}