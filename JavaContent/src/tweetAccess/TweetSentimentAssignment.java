package tweetaccess;

import emojireader.EmojiDataAccess;
import emojireader.UnicodeEmojiSampler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sentimentanalysis.SentimentRankAssignement;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bukunmi on 4/6/2016.
 */
public class TweetSentimentAssignment {

    private static  String[] fileList(String directory){
        FileValidation fileValidation = new FileValidation();
        ArrayList<File> fileArrayList = fileValidation.listJSONFilesInDirectory(directory);
        String[] fileName = new String[fileArrayList.size()];
        for(int i = 0; i <fileArrayList.size(); i++) {
            fileName[i] = fileArrayList.get(i).getPath();
            //System.out.println(fileName);
        }
        return fileName;
    }

    private static String removeUrl(String commentStr)
    {
        String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(commentStr);
        int i = 0;
        while (m.find()) {
            commentStr = commentStr.replaceAll(m.group(i),"").trim();
            i++;
        }
        return commentStr;
    }

    /**
     * gets the respective score of the tweet text and the emojis included in the text
     * @param fileDirc
     * @return
     * @throws IOException
     */
    private static ArrayList<Double> getSentimentScore(String fileDirc, String tweetText) throws IOException {
        FileReader reader;
        JSONParser parser = new JSONParser();

        ArrayList<Double> sentRank = new ArrayList<>();
        double sumRank = 0;

        //init Sentiment Rank
        SentimentRankAssignement.init();

        //remove urls before finding the sentiment on the tweet
        //sentRank.add(SentimentRankAssignement.findSentimentRank(removeUrl(tweetText)) );
        // but due to shorthand url codes, the remove url doesn't work for all url.
        sentRank.add(SentimentRankAssignement.findSentimentRank(tweetText));

        //getemoji() is used here to get the sentiment score off each tweet
        ArrayList<String[]> emojiDataList = getEmojis(tweetText);
        for (String[] emojiData :
                emojiDataList) {
            sumRank += new Double(emojiData[4]);
        }
        //average Sentiment rank
        if (emojiDataList.size() != 0) {
            double aveEmojiRank = sumRank / emojiDataList.size();
            System.out.println("Average Emoji Sentiment Rank " + aveEmojiRank);
            sentRank.add(aveEmojiRank);
        } else {
            //sentRank;
        }
        return sentRank;
    }

    private static ArrayList<String[]> getEmojis(String text) throws IOException {
        ArrayList<String> emojiList = UnicodeEmojiSampler.identifyEmojiCode(text);
        return EmojiDataAccess.showAllEmojiFromList(emojiList);
    }

    private static void insertTweetSentimentAndEmoji(String fileDirc) throws IOException, ParseException {

        FileReader reader = new FileReader(fileDirc);
        JSONParser parser = new JSONParser();

        Object parse = parser.parse(reader);
        JSONObject jsonObject = (JSONObject) parse;

        if (!jsonObject.containsKey("total_sentiment_rank")) {

            String text = (String) jsonObject.get("text");
            double sumSent = 0;
            double sentimentRank;
            double avrSentRank;
            ArrayList<Double> indvSentRank = getSentimentScore(fileDirc, text);

            ///add up tweets sentiment for text and emojis

            for (int i = 0; i < indvSentRank.size(); i++) {
                if (indvSentRank.get(i).isNaN()) {
                    sentimentRank = 0;
                } else {
                    sentimentRank = indvSentRank.get(i);
                }
                sumSent += sentimentRank;
            }
            avrSentRank = sumSent / indvSentRank.size();
            if (indvSentRank.get(0).isNaN() && indvSentRank.size() == 2) {
                avrSentRank = indvSentRank.get(1);

            }
            FileWriter writer = new FileWriter(fileDirc);
            JSONObject obj = new JSONObject();

            //write these to json file
            if (!indvSentRank.get(0).isNaN() && indvSentRank.size() == 2) {
                double textSentRank = indvSentRank.get(0);
                jsonObject.put("text_sentiment_rank_str", String.valueOf(textSentRank));
                jsonObject.put("text_sentiment_rank", textSentRank);
            }

            if (indvSentRank.size() == 2) {
                double emojiSentRank = indvSentRank.get(1);
                jsonObject.put("emoji_sentiment_rank_str", String.valueOf(emojiSentRank));
                jsonObject.put("emoji_sentiment_rank", emojiSentRank);

                JSONArray emojiList = new JSONArray();
                ArrayList<String[]> eL = getEmojis(text);
                for (int i = 0; i < eL.size(); i++) {
                    if (i != 0 && eL.get(i)[0].equals(eL.get(i - 1)[0])) {
                        i--;
                        eL.remove(i + 1);
                    }
                }
                for (String[] emojiData :
                        eL) {
                    JSONObject emojiItem = new JSONObject();
                    String[] tagList = emojiData[3].replace(", ", ",").split(",");
                    emojiItem.put("code", emojiData[0].replace("U+", "").replace(" ", ""));
                    emojiItem.put("alias", emojiData[2]);
                    JSONArray tags = new JSONArray();
                    for (String tag : tagList) {
                        tags.add(tag.replace(" ", ""));
                    }
                    emojiItem.put("tags", tags);

                    emojiList.add(emojiItem);
                }
                jsonObject.put("emojiList", emojiList);
            } else {
                jsonObject.put("emoji_sentiment_rank_str", null);
                jsonObject.put("emoji_sentiment_rank", null);
                jsonObject.put("emojiList", null);

            }

            jsonObject.put("total_sentiment_rank_str", String.valueOf(avrSentRank));
            jsonObject.put("total_sentiment_rank", avrSentRank);

            System.out.println("\n Total Average Sentiment Rank " + avrSentRank);

            jsonObject.writeJSONString(jsonObject, writer);
            writer.close();
        }
    }


    public static void main(String[] args) {

        long lStartTime = new Date().getTime();

        String baseDir = System.getProperty("user.dir");
        String tweetDir = baseDir + "\\resources\\RankedTweet";
        String[] fileList = fileList(tweetDir);

        //Test sentiment Assignment
        String tweetDir2 = baseDir + "\\resources\\sampleTweet.json";
        try {
            insertTweetSentimentAndEmoji(tweetDir2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*for (String val :
                fileList) {
            try {
                insertTweetSentimentAndEmoji(val);
            } catch (IOException e) {
                System.out.println("IO Exception Error = " + e);
                e.printStackTrace();
            } catch (ParseException e) {
                System.out.println("Parser Exception Error= " + e);
                e.printStackTrace();
            }
            System.out.println(val);
        }*/
        long lEndTime = new Date().getTime();

        long difference = lEndTime - lStartTime;
        long second = (difference / 1000) % 60;
        long minute = (difference / (1000 * 60)) % 60;
        long hour = (difference / (1000 * 60 * 60)) % 24;

        System.out.println("Elapsed Time: " + hour+":"+minute+":"+second);

    }
}
