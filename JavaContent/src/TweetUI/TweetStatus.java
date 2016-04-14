// --- Change the package to integrate into the other pieces *******
package displaytweets;

/**
 *
 * @author Kenan Blackerby
 */

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import twitter4j.Status;

public class TweetStatus extends javax.swing.JPanel {

    /**
     * Creates new form tweetstatus
     * @param tweet
     */
    public TweetStatus(Status tweet) {
        initComponents();
        // Check for retweet and fill tweet accordingly
        if(tweet.isRetweet())
            fillInRetweet(tweet);
        else
            fillInTweet(tweet);
    }

    // Fills in the Tweet form with the tweet information 
    private void fillInTweet(Status tweet) {
        // Get the username, screen name, and text for the tweet
        String uname = tweet.getUser().getName();
        String sname = tweet.getUser().getScreenName();
        String tweetText = tweet.getText();
        Date createdAt = tweet.getCreatedAt();
        
        // Print the tweet in the Console --- FOR TEST *****
        System.out.println("@" + sname + " - " + tweetText);
                
        username.setText(uname);
        screenname.setText("@" + sname);
        date.setText(createdAt.toString());
        
        
        ArrayList<String> textList = guiUnicodeEmoji.identifyEmojiCode(tweetText);
        
        if(textList.isEmpty())
            jPanel1.add(new JLabel(tweetText));
        
        // Repeat to the end of the List
        for (int i = 0; i < textList.size(); i++) {
            if(textList.get(i).startsWith("TEXT"))
                // Add substring to the statusText panel
                jPanel1.add(new JLabel(textList.get(i).substring(4)));
            else {
                jPanel1.add(new JLabel(new javax.swing.ImageIcon(getClass().getResource("/displaytweets/twitter_logo.png"))));
            }
        }
        
        
        
        try {
            String location = tweet.getPlace().getFullName();
            Location.setText(location);
        } catch(NullPointerException e) {
            Location.setText("");
        }
        
        retweetPanel.setPreferredSize(new Dimension());
    }
    
    // Fills in the Tweet form with the tweet information 
    private void fillInRetweet(Status tweet) {     
        // Get the username, screen name, and text for the tweet
        String uname = tweet.getUser().getName();
        String sname = tweet.getUser().getScreenName();
        Date createdAt = tweet.getCreatedAt();

        username.setText(uname);
        screenname.setText("@" + sname);
        date.setText(createdAt.toString());
        retweet.setText("Retweeted");
        Status retweeted = tweet.getRetweetedStatus();
        String tweetText = retweeted.getText();
        username_r.setText(retweeted.getUser().getName());
        screenname_r.setText(retweeted.getUser().getScreenName());
       // StatusText.setText(retweeted.getText());

        // Print the tweet in the Console --- FOR TEST *****
        System.out.println("@" + retweeted.getUser().getScreenName() + " - " + tweetText);
        
        ArrayList<String> textList = guiUnicodeEmoji.identifyEmojiCode(tweetText);
        javax.swing.JLabel text = new JLabel();
        if(textList.isEmpty())
            text.setText(tweetText);
        
        // Repeat to the end of the List
        for (int i = 0; i < textList.size(); i++) {
            if(textList.get(i).startsWith("TEXT"))
                // Add substring to the statusText panel
                jPanel1.add(new JLabel(textList.get(i).substring(4)));
            else {
                int iconHeight = 20, iconWidth = 20;
                // NOTE: Change the file address based on final build structure ---
                BufferedImage img;
                try {
                    img = ImageIO.read(new File("C:/Users/Kenan/Documents/School"
                    +"/ECE Classes/Info Retrieval/TweetMoji/JavaContent/emojilist/Pics/"
                                                +textList.get(i).replace("U+", "")+".jpg"));
                } catch (IOException e) {
                    img = new BufferedImage(iconWidth, iconHeight, BufferedImage.TYPE_3BYTE_BGR);
                }
                if (img.getHeight() < img.getWidth())
                    iconHeight = -1;
                else if (img.getHeight() > img.getWidth())
                    iconWidth = -1;
                jPanel1.add(new JLabel(new javax.swing.ImageIcon(img.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH))));
            }
        }
        
        try {
            String location = tweet.getPlace().getFullName();
            Location.setText(location);
        } catch(NullPointerException e) {
            Location.setText("");
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        profile = new javax.swing.JLabel();
        username = new javax.swing.JLabel();
        screenname = new javax.swing.JLabel();
        twicon = new javax.swing.JLabel();
        date = new javax.swing.JLabel();
        Location = new javax.swing.JLabel();
        retweet = new javax.swing.JLabel();
        retweetPanel = new javax.swing.JPanel();
        username_r = new javax.swing.JLabel();
        screenname_r = new javax.swing.JLabel();
        profile_r = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(255, 255, 255));

        profile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/displaytweets/twitter_logo.png"))); // NOI18N

        username.setText("user");

        screenname.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        screenname.setText("screen");

        twicon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/displaytweets/twitter_logo.png"))); // NOI18N
        twicon.setMaximumSize(new java.awt.Dimension(60, 60));
        twicon.setMinimumSize(new java.awt.Dimension(45, 45));
        twicon.setPreferredSize(new java.awt.Dimension(52, 52));

        date.setText("date");

        Location.setText("Location");

        retweetPanel.setBackground(new java.awt.Color(255, 255, 255));

        username_r.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        username_r.setText("user");
        username_r.setToolTipText("");

        screenname_r.setFont(new java.awt.Font("Dialog", 1, 10)); // NOI18N
        screenname_r.setText("screen");

        profile_r.setIcon(new javax.swing.ImageIcon(getClass().getResource("/displaytweets/twitter_logo.png"))); // NOI18N
        profile_r.setDisabledIcon(null);
        profile_r.setMinimumSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout retweetPanelLayout = new javax.swing.GroupLayout(retweetPanel);
        retweetPanel.setLayout(retweetPanelLayout);
        retweetPanelLayout.setHorizontalGroup(
            retweetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retweetPanelLayout.createSequentialGroup()
                .addComponent(profile_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(retweetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(screenname_r)
                    .addComponent(username_r))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        retweetPanelLayout.setVerticalGroup(
            retweetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(retweetPanelLayout.createSequentialGroup()
                .addGroup(retweetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(profile_r, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(username_r))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(screenname_r))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(310, 100));
        FlowLayout flow = new FlowLayout(FlowLayout.LEADING);
        jPanel1.setLayout(flow);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(profile)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(screenname)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(username)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(retweet)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(twicon, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(retweetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(date)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Location))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(56, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(profile)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(username)
                            .addComponent(retweet)))
                    .addComponent(twicon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(screenname)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(retweetPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(date)
                    .addComponent(Location))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel Location;
    javax.swing.JLabel date;
    javax.swing.JPanel jPanel1;
    javax.swing.JLabel profile;
    javax.swing.JLabel profile_r;
    javax.swing.JLabel retweet;
    javax.swing.JPanel retweetPanel;
    javax.swing.JLabel screenname;
    javax.swing.JLabel screenname_r;
    javax.swing.JLabel twicon;
    javax.swing.JLabel username;
    javax.swing.JLabel username_r;
    // End of variables declaration//GEN-END:variables
}
