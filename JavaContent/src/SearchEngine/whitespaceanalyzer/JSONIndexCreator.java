package whitespaceanalyzer;

import java.io.BufferedReader;

// author : Roshan Ramprasad Shetty

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONIndexCreator {
	
	/***************************************************************************************************
	 *  Variable List                                                                                  *                       
	 ***************************************************************************************************/	
	private static String datastore = "C:\\Users\\Pareshan\\Downloads\\datastore\\TestTweets";
	private IndexWriter indexWriter = null;
	private WhitespaceAnalyzer analyzer;
	private Directory index;
	private FSDirectory dir;
	private final int SEVEN = 7;
	private final int THREE = 3;
	
	
	
	/***************************************************************************************************
	 *  Default Constructor                                                                            *                       
	 ***************************************************************************************************/
	public JSONIndexCreator()
	{
		
		analyzer = new WhitespaceAnalyzer();
		Path p = Paths.get("C:/Users/Pareshan/Downloads/index");
		try {
			dir = FSDirectory.open(p);
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			indexWriter = new IndexWriter(dir, config);
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	/***************************************************************************************************
	 *  Methods                                                                                        *                       
	 ***************************************************************************************************/
	
	
	/***************************************************************************************************
	 *  Method Name: readAllFilesInFolder                                                              *
	 *  Description: The method will read all the files in a particular folder. if there are subfolder *
	 *               within the main folders, the method recursively goes in the sub folders and reads *
	 *               all the files.                                                                    *
	 *  Inputs     : File                                                                              *                                                                                              *
	 *  Returns    : void                                                                              *                                                                                             *
	 *  Called by  : Main method                                                                       *	                                                                                          
	 ***************************************************************************************************/	
	public void readAllFilesInFolder(File folder) throws  IOException
	{
		 
		
		
	     for (File fileEntry : folder.listFiles())
	     {
	    	 
	         if (fileEntry.isDirectory()) 
	         {
	        	 System.out.println("Processing files in " + fileEntry + " folder");
	        	 readAllFilesInFolder(fileEntry);
	        	 System.out.println("Processing files in " + fileEntry + " folder finished");
	         } 
	         else
	         {
	        	 if (fileEntry.getName().endsWith(".json")
	    				 && !fileEntry.isHidden()
	       	             && fileEntry.canRead()
	    	             && fileEntry.length() > 0.0 )
	        	 {
	        		 readJSONFile(fileEntry); 
	        	 }
	        	 else
	        	 {
	        		 System.out.println(fileEntry.getName() + " is not a .JSON file");
	        	 }
	        	 
	         }
	     }
	    
	 }
	/***************************************************************************************************
	 *  Method End                                                                                     *                       
	 ***************************************************************************************************/
	 
	 
	 
	
	/***************************************************************************************************
	 *  Method Name: readJSONFile                                                                      *
	 *  Description: This method is used for reading the JSON file and extracting text information from*
	 *               the Tweets. This text information is then indexed and will be used for Analysis   *	 
	 *  Inputs     : File, JSONParser                                                                  *                                                                                              *
	 *  Returns    : void                                                                              *                                                                                             *
	 *  Called by  : readAllFilesInFolder                                                              *	                                                                                          
	 ***************************************************************************************************/	
	public void readJSONFile(File fileEntry)
	{
		try {
			
			StringBuilder jsonCotent = new StringBuilder();			
			BufferedReader jsonReader = new BufferedReader(new FileReader(fileEntry));
			String currentLine;
			
			while ((currentLine = jsonReader.readLine()) != null)
			{
				jsonCotent.append(currentLine);
			}
			
			currentLine = jsonCotent.toString();
			
			int textStartLocation = currentLine.indexOf("text");
			int sourceStartLocation = currentLine.indexOf("source");
			int startLocation = textStartLocation + SEVEN;
			int endLocation = sourceStartLocation - THREE;
			
			String textValue = currentLine.substring(startLocation, endLocation);
			createIndex(fileEntry,textValue);
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	/***************************************************************************************************
	 *  Method End                                                                                     *                       
	 ***************************************************************************************************/
	
	
	
	/***************************************************************************************************
	 *  Method Name: createIndex                                                                       *
	 *  Description: This method is used creating the actual Index                                     *
	 *  Inputs     : File                                                                              *                                                                                              *
	 *  Returns    : void                                                                              *                                                                                             *
	 *  Called by  : readJSONFile                                                                      *	                                                                                          
	 ***************************************************************************************************/
	public void createIndex(File file, String name)
	{
				Document doc = new Document();
				
			     System.out.println(name);
			     System.out.println(file.getName());
			     doc.add(new TextField("text", name, Field.Store.YES));
				 doc.add(new Field("nameOfFile", file.getName(), Field.Store.YES, Field.Index.ANALYZED));
			     if (doc != null) 
			     { 
			    	 try 
			    	 {
			    		
			    		System.out.println(doc.toString());
			    		indexWriter.addDocument(doc);
			    		System.out.println(doc.toString());
			    		//System.out.println(file.getName() + " indexed successfully");
					 }
			    	 catch (IOException e)
			    	 {
						e.printStackTrace();
					 }
			     }
	    	   
	 }
	/***************************************************************************************************
	 *  Method End                                                                                     *                       
	 ***************************************************************************************************/
		
	 
	
	/***************************************************************************************************
	 *  Method Name: closeIndexWriter                                                                  *
	 *  Description: This method is used for closing the index writer                                  *	 
	 *  Inputs     : None                                                                              *                                                                                              *
	 *  Returns    : void                                                                              *                                                                                             *
	 *  Called by  : Main Method                                                                       *	                                                                                          
	 ***************************************************************************************************/	
	public void closeIndexWriter() 
	 {
		 try {
			indexWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 /***************************************************************************************************
	 *  Method End                                                                                      *                       
	 ****************************************************************************************************/
	
	public WhitespaceAnalyzer getAnalyzer()
	 {
		 return this.analyzer;
	 }
	
	
	
	
	/****************************************************************************************************
	 *  Starter Method                                                                                  *                       
	 ****************************************************************************************************/
     public static void main(String[] args) {
    	 
    	 JSONIndexCreator obj = new JSONIndexCreator();
    	 File file = new File(datastore);
    	 try {
    		long start = System.currentTimeMillis();
			obj.readAllFilesInFolder(file);
			obj.closeIndexWriter();
			long end = System.currentTimeMillis();
			long milliseconds = (end - start);
			int seconds = (int) (milliseconds / 1000) % 60 ;
			int minutes = (int) ((milliseconds / (1000*60)) % 60);
			int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
			System.out.println("Time Taken: " + seconds);
			System.out.println("Time Taken: " + minutes);
			System.out.println("Time Taken: " + hours);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     } // end of main
     /***************************************************************************************************
 	 *  Method End                                                                                      *                       
 	 ****************************************************************************************************/

}// end of class

