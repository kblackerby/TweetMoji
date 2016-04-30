package testcodes;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.Attribute;

public class DisplayingTokens {
	
	
	

	public static void main(String[] args) throws IOException {
		
		StringReader string = new StringReader("Hi how are you. Is today good to talk?");
		Analyzer analyzer = new StandardAnalyzer();   	    
	    TokenStream tokenStream = analyzer.tokenStream("text", string);
	    OffsetAttribute offsetAttribute = tokenStream.addAttribute(OffsetAttribute.class);
	    CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);

	    tokenStream.reset();
	    while (tokenStream.incrementToken()) {
	        int startOffset = offsetAttribute.startOffset();
	        int endOffset = offsetAttribute.endOffset();
	        String term = charTermAttribute.toString();
	        System.out.println(term);
	    }   	    
	    
	}

}
