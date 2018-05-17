/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wordlist;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
 import java.nio.file.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.File;
/**
 *
 * @author Heromachine
 */
public class FileReadParser {   
    
    FileReadParser()       
    {
    }
    
    FileReadParser(int x)throws FileNotFoundException            
    {
    } 
    
    public Boolean isExisting(Path path) 
    {
        if (path == null || !Files.exists(path))
        {
            return false;
        }
        else 
        {
            return true;
        }
    }
    
        
    public Boolean isExistingDirectory(String path) 
    {
        File file = new File(path);
        if (file.isDirectory())
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    
    public Boolean isExistingFile(String path) 
    {
        File file = new File(path);
        if (file.isFile())
        {
            return true;
        }
        else 
        {
            return false;
        }
    }
    
    ArrayList<StringBuilder> URLlist;
    HashMap<String, String> URLResults = new HashMap<>(); 
    int key = 1;
    
   
    public  String[] parserDoc(String file) throws FileNotFoundException 
    {
    String text="";

    //read the input file
        Scanner scan=new Scanner(new FileReader(file));
            while(scan.hasNextLine())
            {
                text+="\n"+scan.nextLine();
            }

    //make paragraphs
    //Solution to split paragraphs http://stackoverflow.com/a/9327740/966044
        String[] paragraphs = text.split("\n\n+");
        return paragraphs;
    }
    
   
    public String parserLocalHTML(String file, String p)throws FileNotFoundException 
    {
        String content = "";
         try { 
                File in = new File(file);
                Document doc = Jsoup.connect(in.toString()).get();
                System.out.println("Size");
         
                org.jsoup.select.Elements words = doc.select(p);
                System.out.println("Size" +  words.size());
                
                for (Element e: words)
                {
                    System.out.print(e.text());
                }
                } catch (IOException ex) {
                   // Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
        
        
        return content;
    }
    public void UrlCrawler(Path file, String a)throws FileNotFoundException
    {    try 
         {  if (isExistingDirectory(file.getFileName().toString()))
            {       
            }
            else 
            {   Document doc = Jsoup.parse(file.toString(), null);
                org.jsoup.select.Elements urls = doc.select(a);

                for (Element e: urls)
                {   if (!URLResults.containsKey(e.attr("href")))
                    {   key++;
                        URLResults.put(key+"", e.text());
                        Path x = Paths.get(e.text());
                        UrlCrawler (x, a);
                    }
                    System.out.print("ERROR01 \n");
                }
            }        
        } 
        catch (IOException ex) 
        {
            System.out.print("ERROR \n");
           // Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
         
        }
    }
    public String parserLocalTest(String file, String p)throws FileNotFoundException 
    {
        String content = "";
         try { 
                File in = new File("Page.html");
                Document doc = Jsoup.parse(in, null);
         
                org.jsoup.select.Elements words = doc.select(p);
                for (Element e: words)
                {
                    content += e.text();
                }
                } catch (IOException ex) 
                {
                   // Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
                }
        
        
        return content;
    }
    

}
