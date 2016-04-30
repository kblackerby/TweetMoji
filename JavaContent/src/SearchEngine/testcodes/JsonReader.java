package testcodes;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {
	
	private final int SEVEN = 7;
	private final int THREE = 3;
	
	
	
	public void simpleReadUsingBufferedReader() throws IOException
	{
		StringBuilder sb = new StringBuilder();
		
		try {
			
			
			String sCurrentLine;

			BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Pareshan\\Downloads\\datastore\\TestTweets\\happyface.json"));

			while ((sCurrentLine = br.readLine()) != null)
			{
				sb.append(sCurrentLine);				
			}
						
			sCurrentLine = sb.toString();
			
			int textStartLocation = sCurrentLine.indexOf("text");
			int sourceStartLocation = sCurrentLine.indexOf("source");
			int startLocation = textStartLocation + SEVEN;
			int endLocation = sourceStartLocation - THREE;
			
			String textValue = sCurrentLine.substring(startLocation, endLocation);
			System.out.println(textValue);
			
			System.out.println(textStartLocation + "..." + sourceStartLocation);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String message = sb.toString();
		
		System.out.println(message);
		
		
		
	}
	
	public void readUsingJSobject()
	{
		JSONParser parser = new JSONParser();
		
		try {
			Object obj = parser.parse(new FileReader("C:\\Users\\Pareshan\\Downloads\\datastore\\TestTweets\\happyface.json"));
			
			
			JSONObject jsonObject = (JSONObject) obj;
			String name = (String) jsonObject.get("text");
			System.out.println(name);
		} catch (IOException | ParseException e) {
			
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		JsonReader obj1 = new JsonReader();
		obj1.readUsingJSobject();
		obj1.simpleReadUsingBufferedReader();
		
		
		

	}

	

}
