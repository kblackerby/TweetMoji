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

import javax.swing.JFrame;
import static javax.swing.JFrame.*;

public class DisplayTweetsTest {

    /**
     * @param args the command line arguments
     */
     public static void main(String[] args) {
            // Create a Frame --- FOR TEST *****
            JFrame tweetFrame = new JFrame();
            tweetFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            
            DisplayTweets tweetList = new DisplayTweets();
            tweetFrame.add(tweetList);
            tweetFrame.pack();
            tweetFrame.setVisible(true);
    }
}
