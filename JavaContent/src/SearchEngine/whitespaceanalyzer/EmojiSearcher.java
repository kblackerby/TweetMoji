package whitespaceanalyzer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

//Author : Roshan Ramprasad Shetty

public class EmojiSearcher {
	
	/***************************************************************************************************
	 *  Variable List                                                                                  *                       
	 ***************************************************************************************************/
	
	private Directory index;
	private FSDirectory dir;
	
	/***************************************************************************************************
	 *  Default Constructor                                                                            *                       
	 ***************************************************************************************************/
	public EmojiSearcher()
	{
		
		Path p = Paths.get("C:/Users/Pareshan/Downloads/index");
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
	public void searchEmojiInText(String query, WhitespaceAnalyzer analyzer)
	{
		try
		{
			Query q = new QueryParser("text", analyzer).parse(query);
			int hitsPerPage = 10;
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
					    
			//	Code to display the results of search
			System.out.println("Found " + hits.length + " hits");
					    
			for(int i=0;i<hits.length;++i) 
			{
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				System.out.println((i + 1) + ". " +  d.get("nameOfFile"));
				System.out.println(d.get("text"));
			}
					    
			// reader can only be closed when there is no need to access the documents any more
				reader.close();
			
		}
		catch (ParseException | IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
	
	
	public WhitespaceAnalyzer getAnalyzer()
	{
		JSONIndexCreator indexerObject = new JSONIndexCreator();
		return indexerObject.getAnalyzer();
		
	}
	
	
	
	
	
	/****************************************************************************************************
	 *  Starter Method                                                                                  *                       
	 ****************************************************************************************************/
	public static void main(String[] args) {
		
		EmojiSearcher searchObject = new EmojiSearcher();
		
		String query = args.length > 0 ? args[0] : "U+1F642";
		searchObject.searchEmojiInText(query,searchObject.getAnalyzer());
		

	}
	/***************************************************************************************************
 	 *  Method End                                                                                      *                       
 	 ****************************************************************************************************/
}
