// --- Change the package to integrate into the other pieces *******
package displaytweets;

/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 * @author Kenan Blackerby
 * 
 * This is a test program that demonstrates how to display the tweets
 */

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JFrame;
import static javax.swing.JFrame.*;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

public class DisplayTweets {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {
        try {
            // Create a Frame --- FOR TEST *****
            JFrame tweetList = new JFrame();
            tweetList.setDefaultCloseOperation(EXIT_ON_CLOSE);
            
            // Container Box for the tweet panels to go in (Display Vertically)
            Box contain = Box.createVerticalBox();
            
            // Get the JSON files frem the Tweets folder --- REPLACE WITH SEARCH RESULT *******
            File[] files = new File("C:/Users/Kenan/Documents/School"
                    +"/ECE Classes/Info Retrieval/displayTweets/Tweets")
                    .listFiles((File dir, String name) -> name.endsWith(".json"));
            
            // Iterate through the files, displaying the tweets
            for (File file : files) {
                // Create Status Object from the file
                String rawJSON = readFirstLine(file);
                Status tweet = TwitterObjectFactory.createStatus(rawJSON);              
       
                TweetStatus stat = new TweetStatus(tweet);
                contain.add(stat);
            }
            tweetList.add(contain);
            tweetList.pack();
            tweetList.setVisible(true);
            
          // Exceptions if errors in getting tweets
        } catch (IOException ioe) {
            System.out.println("Failed to store tweets: " + ioe.getMessage());
        } catch (TwitterException te) {
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
    
    // method to retrieve the tweets from the files (UTF-8 format)
    private static String readFirstLine(File fileName) throws IOException {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(fileName);
            isr = new InputStreamReader(fis, "UTF-8");
            br = new BufferedReader(isr);
            return br.readLine();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ignore) {
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
}
