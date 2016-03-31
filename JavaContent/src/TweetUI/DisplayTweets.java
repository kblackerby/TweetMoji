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
package displaytweets;

import java.awt.Component;
import java.io.*;
import javax.swing.Box;
import javax.swing.JFrame;
import static javax.swing.JFrame.*;
import javax.swing.JPanel;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

/**
 *
 * @author Kenan
 * 
 * This is a test program that demonstrates how to display the tweets
 */

public class DisplayTweets {

 
    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {
        try {
            // Create a Frame --- FOR TEST *****
            JFrame tweet = new JFrame();
            tweet.setDefaultCloseOperation(EXIT_ON_CLOSE);
            
            // Container Box for the tweet panels to go in (Display Vertically)
            Box contain = Box.createVerticalBox();
            
            // Get the JSON files frem the Tweets folder --- FOR TEST *****
            File[] files = new File("Tweets").listFiles((File dir, String name) -> name.endsWith(".json"));
            
            // Iterate through the files, displaying the tweets
            for (File file : files) {
                // Create Status Object from the file
                String rawJSON = readFirstLine(file);
                Status status = TwitterObjectFactory.createStatus(rawJSON);
                // Get the username, screen name, and text for the tweet
                String uname = status.getUser().getName();
                String sname = status.getUser().getScreenName();
                String tweetText = status.getText();
                
                // Print the tweet in the Console --- FOR TEST *****
                System.out.println("@" + sname + " - " + tweetText);
                
                // Check for retweet and stack retweets accordingly
                Status retweet = status.getRetweetedStatus();
                boolean rTflag = false;
                if(null != retweet) {
                    rTflag = true;
                }
                // Create the Tweet Status Object 
                if(rTflag == true) {
                    JPanel retweetStat = makeRetweet(retweet);
                }
                else {
                    TweetStatus stat = new TweetStatus(uname, sname, tweetText);
                    contain.add(stat);
                }
                
                
                
            }
            tweet.add(contain);
            tweet.pack();
            tweet.setVisible(true);
            
        } catch (IOException ioe) {
            System.out.println("Failed to store tweets: " + ioe.getMessage());
        } catch (TwitterException te) {
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        }
    }
    
    private static TweetStatus makeRetweet(Status tweet) {
        if(tweet == null)
            return null;
        else {
            // Get the username, screen name, and text for the tweet
            String uname = tweet.getUser().getName();
            String sname = tweet.getUser().getScreenName();
            String tweetText = tweet.getText();

            // Print the tweet in the Console --- FOR TEST *****
            System.out.println("@" + sname + " - " + tweetText);

            TweetStatus rTstat = makeRetweet(tweet.getRetweetedStatus());
            boolean rTflag = false;
            if(rTstat != null)
                rTflag = true;

            TweetStatus stat = new TweetStatus(uname, sname, tweetText);

            if(rTflag == true)
                stat.add(rTstat);

            return stat;
            }
    }
     
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
