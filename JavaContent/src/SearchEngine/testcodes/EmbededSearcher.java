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

public class EmbededSearcher {
	
	/***************************************************************************************************
	 *  Variable List                                                                                  *                       
	 ***************************************************************************************************/
	private StandardAnalyzer analyzer;
	private Directory index;
	private FSDirectory dir;
	private IndexSearcher searcher;
	
	/***************************************************************************************************
	 *  Default Constructor                                                                            *                       
	 ***************************************************************************************************/
	public EmbededSearcher()
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
	public void searchEmojiInText(String query, Filter filter, StandardAnalyzer analyzer)
	{
		try
		{
			Query mainQuery = new QueryParser("text", analyzer).parse(query);
			int hitsPerPage = 1000;
			IndexReader reader = DirectoryReader.open(dir);
			this.searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(mainQuery,filter, collector);
			
			ScoreDoc[] hits = collector.topDocs().scoreDocs;
					    
			//	Code to display the results of search
			System.out.println("Found " + hits.length + " hits");
					    
			for(int i=0;i<hits.length;++i) 
			{
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				System.out.println((i + 1) + ". " +  d.get("nameOfFile"));
				System.out.println(d.get("text"));
				System.out.println(d.get("filepath"));
			}
					    
			// reader can only be closed when there is no need to access the documents any more
				reader.close();
			
		}
		catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
		
	}
	
	public IndexSearcher getIndexSearcher()
	{
		return this.searcher;
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
		
		EmbededSearcher searchObject = new EmbededSearcher();
		
		PhraseQuery phraseQuery = new PhraseQuery();
		phraseQuery.setSlop(0);
		
		phraseQuery.add(new Term("text", "ud83d")); 
		phraseQuery.add(new Term("text", "ude42")); //ude42 //udcb0
		//String[] phrases = new String[]{"ud83d"};
		Filter happyFace = new QueryWrapperFilter(phraseQuery);
		
		//for(String word:phrases){
	    //     query.add(new Term("text", word));
		//}
	      
		
		String query = args.length > 0 ? args[0] : "+rt";
		searchObject.searchEmojiInText(query, happyFace, searchObject.getAnalyzer());
		

	}
	/***************************************************************************************************
 	 *  Method End                                                                                      *                       
 	 ****************************************************************************************************/
}
