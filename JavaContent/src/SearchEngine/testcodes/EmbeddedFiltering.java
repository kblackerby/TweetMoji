package testcodes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jfree.ui.RefineryUtilities;

import parser.JSONIndexCreator;

//Author : Roshan Ramprasad Shetty

public class EmbeddedFiltering {
	
	/***************************************************************************************************
	 *  Variable List                                                                                  *                       
	 ***************************************************************************************************/
	private StandardAnalyzer analyzer;
	private Directory index;
	private FSDirectory dir;
	
	/***************************************************************************************************
	 *  Default Constructor                                                                            *                       
	 ***************************************************************************************************/
	public EmbeddedFiltering()
	{
		
		Path p = Paths.get("C:/Users/Pareshan/Downloads/standardindex");
		try
		{
			dir = FSDirectory.open(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/***************************************************************************************************
	 *  Methods                                                                                        *                       
	 ***************************************************************************************************/
	
	
	/***************************************************************************************************
	 *  Method Name: searchEmojiInText                                                                 *
	 *  Description: The method will search for emoji's in the text field index created                *
	 *  Inputs     : String,StandardAnalyzer                                                           *                                                                                              *
	 *  Returns    : void                                                                              *                                                                                             *
	 *  Called by  : Main method                                                                       *	                                                                                          
	 ***************************************************************************************************/
	public int searchEmojiInText(String query, Filter filter, StandardAnalyzer analyzer)
	{
		int totalTweets=0;
		try
		{
			Query mainQuery = new QueryParser("text", analyzer).parse(query);
			int hitsPerPage = 1000;
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(mainQuery, filter, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
					    
			//	Code to display the results of search
			
			totalTweets = hits.length;
					    
						    
			// reader can only be closed when there is no need to access the documents any more
				reader.close();
			
		}
		catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
		
		return totalTweets;
		
	}
	
	
	
	public StandardAnalyzer getAnalyzer()
	{
		JSONIndexCreator indexerObject = new JSONIndexCreator();
		return indexerObject.getAnalyzer();
		
	}
	
	
	public static void generateGraph(String searchQuery, int happyCount, int angryCount, int neutralCount)
	{
		GeneratePieChart demo = new GeneratePieChart( searchQuery, happyCount, angryCount, neutralCount );  
	    //demo.setSize( 560 , 367 );
		demo.setSize( 800 , 420 ); 
	    RefineryUtilities.centerFrameOnScreen( demo );    
	    demo.setVisible( true ); 
	}
	
	
	
	/****************************************************************************************************
	 *  Starter Method                                                                                  *                       
	 ****************************************************************************************************/
	public static void main(String[] args) {
		
		String searchString = "wreck* ttu red";
		
		
		EmbeddedFiltering searchObject = new EmbeddedFiltering();
		
	// initializing the boolean query.		
		BooleanQuery booleanQuery = new BooleanQuery(); 
		
	
	// getting happy count	
	// initialization and setup for U+1F600: GRINNING FACE		
				
		PhraseQuery happyFaceQuery = new PhraseQuery();
		happyFaceQuery.setSlop(0);		
		happyFaceQuery.add(new Term("text", "ud83d")); 
		happyFaceQuery.add(new Term("text", "ude00")); 
		
	// initialization and setup for U+1F601: GRINNING FACE WITH SMILING EYES
		
		PhraseQuery slightSmileyFaceQuery = new PhraseQuery();
		slightSmileyFaceQuery.setSlop(0);		
		slightSmileyFaceQuery.add(new Term("text", "ud83d")); 
		slightSmileyFaceQuery.add(new Term("text", "ude01"));
		
	// initialization and setup for U+1F603: SMILING FACE WITH OPEN MOUTH
		
		PhraseQuery moneybagQuery = new PhraseQuery();
		moneybagQuery.setSlop(0);		
		moneybagQuery.add(new Term("text", "ud83d")); 
		moneybagQuery.add(new Term("text", "ude03")); 
		
		
	// adding to boolean query
		
		booleanQuery.add(happyFaceQuery,Occur.SHOULD);
		booleanQuery.add(slightSmileyFaceQuery,Occur.SHOULD);
		booleanQuery.add(moneybagQuery,Occur.SHOULD);
		
	// intialize and set up the filter
		
		Filter filter = new QueryWrapperFilter(booleanQuery);
		
		String query = args.length > 0 ? args[0] : searchString;
		
		// slightly smiley face - \ud83d\ude42
		// smiley face - \uD83D\uDE0A
		// money bag - //ude42 //udcb0						
		
		int happyCount = searchObject.searchEmojiInText(query, filter, searchObject.getAnalyzer());
		
		
	// getting sad count
	
		// initialization and setup for U+1F620: ANGRY FACE		
		
				PhraseQuery angryFaceQuery = new PhraseQuery();
				angryFaceQuery.setSlop(0);		
				angryFaceQuery.add(new Term("text", "ud83d")); 
				angryFaceQuery.add(new Term("text", "ude20")); 
				
			// initialization and setup for U+1F621: POUTING FACE
				
				PhraseQuery poutingFaceQuery = new PhraseQuery();
				poutingFaceQuery.setSlop(0);		
				poutingFaceQuery.add(new Term("text", "ud83d")); 
				poutingFaceQuery.add(new Term("text", "ude21"));
				
							
				
			// adding to boolean query
				BooleanQuery angryBooleanQuery = new BooleanQuery(); 
				
				angryBooleanQuery.add(angryFaceQuery,Occur.SHOULD);
				angryBooleanQuery.add(poutingFaceQuery,Occur.SHOULD);
				
				
			// Initialize and set up the filter
				
				filter = new QueryWrapperFilter(angryBooleanQuery);
				
				query = args.length > 0 ? args[0] : searchString;
				
				// slightly smiley face - \ud83d\ude42
				// smiley face - \uD83D\uDE0A
				// money bag - //ude42 //udcb0						
				
				int angryCount = searchObject.searchEmojiInText(query, filter, searchObject.getAnalyzer());
		
		// get neutral count
				
				// initialization and setup for U+1F610: NEUTRAL FACE		
				
				PhraseQuery neutralFaceQuery = new PhraseQuery();
				neutralFaceQuery.setSlop(0);		
				neutralFaceQuery.add(new Term("text", "ud83d")); 
				neutralFaceQuery.add(new Term("text", "ude10")); 
				
			// initialization and setup for U+1F611: EXPRESSIONLESS FACE
				
				PhraseQuery expressionlessFaceQuery = new PhraseQuery();
				expressionlessFaceQuery.setSlop(0);		
				expressionlessFaceQuery.add(new Term("text", "ud83d")); 
				expressionlessFaceQuery.add(new Term("text", "ude11"));
				
							
				
			// adding to boolean query
				BooleanQuery neutralBooleanQuery = new BooleanQuery(); 
				
				neutralBooleanQuery.add(neutralFaceQuery,Occur.SHOULD);
				neutralBooleanQuery.add(expressionlessFaceQuery,Occur.SHOULD);
				
				
			// Initialize and set up the filter
				
				filter = new QueryWrapperFilter(neutralBooleanQuery);
				
				query = args.length > 0 ? args[0] : searchString;
				
				// slightly smiley face - \ud83d\ude42
				// smiley face - \uD83D\uDE0A
				// money bag - //ude42 //udcb0						
				
				int neutralCount = searchObject.searchEmojiInText(query, filter, searchObject.getAnalyzer());
				
				generateGraph(searchString,happyCount,angryCount,neutralCount);
		

	}
	/***************************************************************************************************
 	 *  Method End                                                                                      *                       
 	 ****************************************************************************************************/
}
