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

import parser.JSONIndexCreator;

//Author : Roshan Ramprasad Shetty

public class SingleEmojiSearcher {
	
	/***************************************************************************************************
	 *  Variable List                                                                                  *                       
	 ***************************************************************************************************/
	private StandardAnalyzer analyzer;
	private Directory index;
	private FSDirectory dir;
	
	/***************************************************************************************************
	 *  Default Constructor                                                                            *                       
	 ***************************************************************************************************/
	public SingleEmojiSearcher()
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
	public void searchEmojiInText(PhraseQuery query, StandardAnalyzer analyzer)
	{
		try
		{
			//Query mainQuery = new QueryParser("text", analyzer).parse(query);
			int hitsPerPage = 1000;
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(query, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
					    
			//	Code to display the results of search
			System.out.println("Found " + hits.length + " hits");
					    
			for(int i=0;i<hits.length;++i) 
			{
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				System.out.println("---------------------------------------------------------------------------------------------------------------------");
				System.out.println("Rank      : " + (i + 1));
				System.out.println("File Name : " +  d.get("nameOfFile"));
				System.out.println("File Text : " + d.get("text"));
				System.out.println("File Path : " + d.get("filepath"));
				System.out.println("---------------------------------------------------------------------------------------------------------------------");
			}
					    
			// reader can only be closed when there is no need to access the documents any more
				reader.close();
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	public StandardAnalyzer getAnalyzer()
	{
		JSONIndexCreator indexerObject = new JSONIndexCreator();
		return indexerObject.getAnalyzer();
		
	}
	
	
	
	
	
	/****************************************************************************************************
	 *  Starter Method                                                                                  *                       
	 ****************************************************************************************************/
	public static void main(String[] args) {
		
		SingleEmojiSearcher searchObject = new SingleEmojiSearcher();
		
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.setSlop(0);		
		phraseQuery.add(new Term("text", "ud83d")); 
		phraseQuery.add(new Term("text", "ude01")); //ude42 //udcb0
		
		
		// slightly smiley face - \ud83d\ude42
		// smiley face - \uD83D\uDE0A
		// money bag - //ude42 //udcb0
		
			
		searchObject.searchEmojiInText(phraseQuery, searchObject.getAnalyzer());
		

	}
	/***************************************************************************************************
 	 *  Method End                                                                                      *                       
 	 ****************************************************************************************************/
}
