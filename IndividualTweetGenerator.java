import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;


public class IndividualTweetGenerator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        try {
            File[] files = new File("Tweets").listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.endsWith(".txt");
                }
            });
            for (File file : files) {
            	FileInputStream fis = null;
        	    InputStreamReader isr = null;
        	    BufferedReader br = null;
            	try {
        	        fis = new FileInputStream(file);
        	        isr = new InputStreamReader(fis, "UTF-8");
        	        br = new BufferedReader(isr);
        	        String line = br.readLine();
        	        while (line != null){
	        	        if (line.startsWith("{")){
	        	        	System.out.println(line);
	        	        	String fname = "";
	    	                for(int i = 0; i < 17; i++){
	    	                	fname += line.charAt(i+52);
	    	                }
	    	                String fileName = file.getName().replace(".txt", "")+"/"+fname+".json";
	    	                File f = new File(fileName);
	    	                f.getParentFile().mkdirs();
	    	                PrintWriter writer = new PrintWriter(file.getName().replace(".txt", "")+"/"+fname+".json", "UTF-8");
	    	                writer.print(line);
	    	                writer.close();
	        	        }
	        	        line = br.readLine();
        	        }
        	    } finally {
        	        if (br != null) {
        	            try {
        	                br.close();
        	            } catch (IOException ignore) {
        	            	System.out.print("Exception: " + ignore);
        	            }
        	        }
        	        if (isr != null) {
        	            try {
        	                isr.close();
        	            } catch (IOException ignore) {
        	            }
        	        }
        	        if (fis != null) {
        	            try {
        	                fis.close();
        	            } catch (IOException ignore) {
        	            }
        	        }
        	    }            	               
            }
            System.exit(0);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to store tweets: " + ioe.getMessage());
        }
		
	}
}
