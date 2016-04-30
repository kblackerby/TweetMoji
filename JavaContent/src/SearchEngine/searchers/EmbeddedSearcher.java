package searchers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.print.Doc;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.RangeFilterBuilder;
import org.apache.lucene.queryparser.xml.builders.RangeQueryBuilder;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import parser.JSONIndexCreator;
import query.SimpleQueryHolder;

//Author : Roshan Ramprasad Shetty

public class EmbeddedSearcher {
	
	/***************************************************************************************************
	 *  Variable List                                                                                  *                       
	 ***************************************************************************************************/
	private StandardAnalyzer analyzer;
	private Directory index;
	private FSDirectory dir;
	private SimpleQueryHolder simpleQueryObject;
	private IndexSearcher searcher;
	
	/***************************************************************************************************
	 *  Default Constructor                                                                            *                       
	 * @throws ParseException 
	 ***************************************************************************************************/
	public EmbeddedSearcher(SimpleQueryHolder simpleQueryObject) throws ParseException
	{
		
		this.simpleQueryObject = simpleQueryObject;
		Path p = Paths.get("./Index");
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
	 * @throws ParseException 
	 ***************************************************************************************************/
	@SuppressWarnings("null")
	public ArrayList<File> searchBoth(Query mainQuery, Filter f,  StandardAnalyzer analyzer) throws ParseException
	{
		System.out.println("executing searchBoth");
		ScoreDoc[] hits = null;
		ArrayList<File> fileSet = new ArrayList<>();
		try
		{
			
			int hitsPerPage = 1000;
			IndexReader reader = DirectoryReader.open(dir);
			searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(mainQuery, f , collector);
			
			hits = collector.topDocs().scoreDocs;
			
//			Code to display the results of search
					
				
				File file;
				
				
					for(int i=0;i<hits.length;++i) 
					{
						int docId = hits[i].doc;
						Document d = searcher.doc(docId);
						
						file = new File(d.get("filepath"));
						
						fileSet.add(file);
						
										
						
					}
				
				
					    
			    
			// reader can only be closed when there is no need to access the documents any more
				reader.close();
				
			
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return fileSet;
		
	}
	
	
	
	public ArrayList<File> searchMainQuery(Query mainQuery,  StandardAnalyzer analyzer) throws ParseException
	{
		System.out.println("executing searchmainq");
		ScoreDoc[] hits = null;
		ArrayList<File> fileSet = new ArrayList<>();
		try
		{
			
			int hitsPerPage = 1000;
			IndexReader reader = DirectoryReader.open(dir);
			searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(mainQuery , collector);
			
			hits = collector.topDocs().scoreDocs;
			
//			Code to display the results of search
					
				
				File file;
				
				
					for(int i=0;i<hits.length;++i) 
					{
						int docId = hits[i].doc;
						Document d = searcher.doc(docId);
						
						file = new File(d.get("filepath"));
						
						fileSet.add(file);
						
										
						
					}
				
				
					    
			    
			// reader can only be closed when there is no need to access the documents any more
				reader.close();
				
			
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return fileSet;
		
	}

	
	public ArrayList<File> searchScoreOnly(Query scoreQuery,  StandardAnalyzer analyzer) throws ParseException
	{
		System.out.println("executing searchscoreq");
		ScoreDoc[] hits = null;
		ArrayList<File> fileSet = new ArrayList<>();
		try
		{
			
			int hitsPerPage = 1000;
			IndexReader reader = DirectoryReader.open(dir);
			searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage);
			searcher.search(scoreQuery, collector);
			
			hits = collector.topDocs().scoreDocs;
			
//			Code to display the results of search
					
				
				File file;
				
				
					for(int i=0;i<hits.length;++i) 
					{
						int docId = hits[i].doc;
						Document d = searcher.doc(docId);
						
						file = new File(d.get("filepath"));
						
						fileSet.add(file);
						
										
						
					}
				
				
					    
			    
			// reader can only be closed when there is no need to access the documents any more
				reader.close();
				
			
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return fileSet;
		
	}
	
	public IndexSearcher getIndexSearcher()
	{
		return searcher;
	}
	
	public ArrayList<File> initializeQueries() throws ParseException
	{
		Boolean scoreFlag = false;
		Boolean mainQueryFlag = false;
		BooleanQuery booleanQuery = new BooleanQuery(); 
		ArrayList<File> result = new ArrayList<>();
		Query mainQuery = null;
		
		
		
		
// formating query for Total score Range searches
		
		System.out.println(simpleQueryObject.getMinTotalScore());
		System.out.println(simpleQueryObject.getMaxTotalScore());
		System.out.println(simpleQueryObject.getMinTextScore());
		System.out.println(simpleQueryObject.getMaxTextScore());
		System.out.println(simpleQueryObject.getMinEmojiScore());
		System.out.println(simpleQueryObject.getMaxEmojiScore());
		
		if (!simpleQueryObject.getMinTotalScore().equals("-1.0") && !simpleQueryObject.getMaxTotalScore().equals("-1.0"))
		{

			BytesRef min = new BytesRef(simpleQueryObject.getMinTotalScore());
			BytesRef max = new BytesRef(simpleQueryObject.getMaxTotalScore());
			TermRangeQuery termq = new TermRangeQuery("totalscore", min , max, true, true);
			booleanQuery.add(termq, Occur.MUST);
			
			scoreFlag = true;
			
		}
		else if (simpleQueryObject.getMinTotalScore().equals("-1.0") && !simpleQueryObject.getMaxTotalScore().equals("-1.0") )
		{
			Query q = new QueryParser("totalscore", getAnalyzer()).parse(simpleQueryObject.getMaxTotalScore());
			booleanQuery.add(q, Occur.MUST);
			scoreFlag = true;
		}
		else if ( !simpleQueryObject.getMinTotalScore().equals("-1.0") && simpleQueryObject.getMaxTotalScore().equals("-1.0") )
		{
			Query q = new QueryParser("totalscore", getAnalyzer()).parse(simpleQueryObject.getMinTotalScore());
			booleanQuery.add(q, Occur.MUST);
			scoreFlag = true;
			
		}

// Formatting query for Text score Range search
		
		if (!simpleQueryObject.getMinTextScore().equals("-1.0") && !simpleQueryObject.getMaxTextScore().equals("-1.0"))
		{

			BytesRef min = new BytesRef(simpleQueryObject.getMinTextScore());
			BytesRef max = new BytesRef(simpleQueryObject.getMaxTextScore());
			TermRangeQuery termq = new TermRangeQuery("textscore", min , max, true, true);
			booleanQuery.add(termq, Occur.MUST);
			scoreFlag = true;
			
		}
		else if (simpleQueryObject.getMinTextScore().equals("-1.0") && !simpleQueryObject.getMaxTextScore().equals("-1.0") )
		{
			Query q = new QueryParser("textscore", getAnalyzer()).parse(simpleQueryObject.getMaxTextScore());
			booleanQuery.add(q, Occur.MUST);
			scoreFlag = true;
		}
		else if ( !simpleQueryObject.getMinTextScore().equals("-1.0") && simpleQueryObject.getMaxTextScore().equals("-1.0") )
		{
			Query q = new QueryParser("textscore", getAnalyzer()).parse(simpleQueryObject.getMinTextScore());
			booleanQuery.add(q, Occur.MUST);
			scoreFlag = true;
			
		}
		
		
		
		
// Formatting query for Emoji score Range search	
		
		if (!simpleQueryObject.getMinEmojiScore().equals("-1.0") && !simpleQueryObject.getMaxEmojiScore().equals("-1.0"))
		{

			BytesRef min = new BytesRef(simpleQueryObject.getMinEmojiScore());
			BytesRef max = new BytesRef(simpleQueryObject.getMaxEmojiScore());
			TermRangeQuery termq = new TermRangeQuery("emojiscore", min , max, true, true);
			booleanQuery.add(termq, Occur.MUST);
			scoreFlag = true;
			
		}
		else if (simpleQueryObject.getMinEmojiScore().equals("-1.0") && !simpleQueryObject.getMaxEmojiScore().equals("-1.0") )
		{
			Query q = new QueryParser("emojiscore", getAnalyzer()).parse(simpleQueryObject.getMaxEmojiScore());
			booleanQuery.add(q, Occur.MUST);
			scoreFlag = true;
		}
		else if ( !simpleQueryObject.getMinEmojiScore().equals("-1.0") && simpleQueryObject.getMaxEmojiScore().equals("-1.0") )
		{
			Query q = new QueryParser("emojiscore", getAnalyzer()).parse(simpleQueryObject.getMinEmojiScore());
			booleanQuery.add(q, Occur.MUST);
			scoreFlag = true;
			
		}
		
		
		
// formatting query fir main query		
		
		
		
		if (simpleQueryObject.getMainQuery().length() == 0 )
		{
			//do nothing
		}
		else
		{
			mainQuery = new QueryParser("text", getAnalyzer()).parse(simpleQueryObject.getMainQuery());
			mainQueryFlag = true;
		}
		
		
		
		
		if ( mainQueryFlag && scoreFlag)
		{
			Filter f = new QueryWrapperFilter(booleanQuery);
			
			result = searchBoth(mainQuery, f, getAnalyzer());			
			
		}
		
		else if(mainQueryFlag && scoreFlag == false)
		{
			System.out.println(mainQuery.toString());
			result = searchMainQuery(mainQuery,getAnalyzer());
		}
		
		else if (mainQueryFlag == false && scoreFlag == true)
		{
			result = searchScoreOnly(booleanQuery, getAnalyzer());
		}

		return result;
	}
	
	
	public StandardAnalyzer getAnalyzer()
	{
		JSONIndexCreator indexerObject = new JSONIndexCreator();
		return indexerObject.getAnalyzer();
		
	}
}
