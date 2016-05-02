package parser;

import java.io.BufferedReader;

// author : Roshan Ramprasad Shetty

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

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
	private static String datastore = "C:\\Users\\Pareshan\\Downloads\\RankedTweet";
	private IndexWriter indexWriter = null;
	private StandardAnalyzer analyzer;
	private Directory index;
	private FSDirectory dir;
	private final int SEVEN = 7;
	private final int THREE = 3;
	
	
	
	/***************************************************************************************************
	 *  Default Constructor                                                                            *                       
	 ***************************************************************************************************/
	public JSONIndexCreator()
	{
		
		analyzer = new StandardAnalyzer();
		Path p = Paths.get("C:/Users/Pareshan/Downloads/testindex");
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
	        		 System.out.println(fileEntry.getName() + " is not a .JSON file OR is empty");
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
	 *  Inputs     : File                                                                              *                                                                                              *
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
			// for Extracting Text
			String place = "\"place\":";
			String text = "\"text\":";

			int placeStartLocation = currentLine.indexOf(place);
			int textStartLocation = 0;
			int temp = 0;	
			
			if (placeStartLocation == -1)
			{
				System.out.println("File Path: " + fileEntry.getAbsolutePath());
				System.out.println("File Name: " + fileEntry.toString());
				System.out.println("Error Message: File not formatted correctly - No place");
			}
			else
			{
				
			

				while(temp < placeStartLocation && temp != currentLine.indexOf(text) )
				{	

					textStartLocation = temp;				
					temp = currentLine.indexOf(text, textStartLocation + 7); 
				}

				int startLocation = textStartLocation + 8;
				int endLocation = placeStartLocation - 2;



				String textValue = currentLine.substring(startLocation, endLocation);

//				System.out.println(textValue);



				// for Extracting sentiment Scores

				String totalSentiment = "\"total_sentiment_rank_str\":";
				String textSentimen = "\"text_sentiment_rank_str\":";
				String emojiSentiment = "\"emoji_sentiment_rank\":";

				int totalSentimentScoreStartLocation = currentLine.indexOf(totalSentiment);
				int totalSentimentScoreEndLocation = currentLine.indexOf(",", totalSentimentScoreStartLocation);
				int textSentimentScoreStartLocation = currentLine.indexOf(textSentimen);
				int textSentimentScoreEndLocation = currentLine.indexOf(",", textSentimentScoreStartLocation);
				int emojiSentimentScoreStartLocation = currentLine.indexOf(emojiSentiment);
				int emojiSentimentScoreEndLocation = currentLine.indexOf(",", emojiSentimentScoreStartLocation);

				String totalSentimentValue = null;
				String textSentimentValue =  null;
				String emojiSentimentValue = null;

//				System.out.println(totalSentimentScoreStartLocation);
//
//				System.out.println(textSentimentScoreStartLocation);
//
//				System.out.println(emojiSentimentScoreStartLocation);

				if (totalSentimentScoreStartLocation == -1 || textSentimentScoreStartLocation == -1 || emojiSentimentScoreStartLocation == -1 )
				{
					totalSentimentValue = "-2";
					textSentimentValue =  "-2";
					emojiSentimentValue = "-2"; 


				}
				else
				{
					totalSentimentValue = currentLine.substring(totalSentimentScoreStartLocation + 28, totalSentimentScoreEndLocation-1);
					textSentimentValue = currentLine.substring(textSentimentScoreStartLocation + 27 , textSentimentScoreEndLocation-1);
					emojiSentimentValue = currentLine.substring(emojiSentimentScoreStartLocation + 23 , emojiSentimentScoreEndLocation); 

					if (totalSentimentValue.equals("null"))
					{
						totalSentimentValue = "-1";
					}
					if (textSentimentValue.equals("null"))
					{
						textSentimentValue = "-1";
					}
					if (emojiSentimentValue.equals("null"))
					{
						emojiSentimentValue = "-1";
					}

//					System.out.println(totalSentimentValue);
//					System.out.println(textSentimentValue);
//					System.out.println(emojiSentimentValue);

				}


				createIndex(fileEntry,textValue,totalSentimentValue,textSentimentValue,emojiSentimentValue);
			}
			
			
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
	 *  Inputs     : File, text to be indexed as string                                                *                                                                                              *
	 *  Returns    : void                                                                              *                                                                                             *
	 *  Called by  : readJSONFile                                                                      *	                                                                                          
	 ***************************************************************************************************/
	public void createIndex(File file, String textField, String totalSentimentValue, String textSentimentValue, String emojiSentimentValue)
	{
				Document doc = new Document();
				
				
				
				
				
			     //System.out.println(name);
			     //System.out.println(file.getName());
			     doc.add(new TextField("text", textField, Field.Store.YES));
			     doc.add(new Field("totalscore",totalSentimentValue , Field.Store.YES, Field.Index.ANALYZED));
			     doc.add(new Field("textscore", textSentimentValue, Field.Store.YES, Field.Index.ANALYZED));
			     doc.add(new Field("emojiscore",emojiSentimentValue , Field.Store.YES, Field.Index.ANALYZED));
				 doc.add(new Field("nameOfFile", file.getName(), Field.Store.YES, Field.Index.ANALYZED));
				 doc.add(new Field("filepath", file.getAbsolutePath(), Field.Store.YES, Field.Index.ANALYZED));
			     if (doc != null) 
			     { 
			    	 try 
			    	 {
			    		
			    		//System.out.println(doc.toString());
			    		indexWriter.addDocument(doc);
			    		//System.out.println(doc.toString());
//			    		System.out.println(file.getName() + " indexed successfully");
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
	
	/***************************************************************************************************
	 *  Method Name: getAnalyzer                                                                       *
	 *  Description: This method returns the current analyzer being used                               *	 
	 *  Inputs     : None                                                                              *                                                                                              *
	 *  Returns    : void                                                                              *                                                                                             *
	 *  Called by  : None                                                                              *	                                                                                          
	 ***************************************************************************************************/
	
	public StandardAnalyzer getAnalyzer()
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
			long seconds =  (long) ((milliseconds / 1000.0) % 60.0) ;
			long minutes =  (long) ((milliseconds / (1000.0*60.0)) % 60.0);
			long hours   =  (long) ((milliseconds / (1000.0*60.0*60.0)) % 24.0);
			System.out.println("Time Taken: " + hours + " hours");
			System.out.println("Time Taken: " + minutes + " minutes");			
			System.out.println("Time Taken: " + seconds + " seconds");
			System.out.println("Time Taken: " + milliseconds + " milliseconds");
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     } // end of main
     /***************************************************************************************************
 	 *  Method End                                                                                      *                       
 	 ****************************************************************************************************/

}// end of class

