package sentimentanalysis;

import emojireader.EmojiDataAccess;
import emojireader.UnicodeEmojiSampler;
import filemerger.FileValidation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Bukunmi on 4/6/2016.
 */
public class TweetSentimentAssignment {

    private static  String[] fileList(String directory){
        FileValidation fileValidation = new FileValidation();
        File[] fileList = fileValidation.listFilesInDirectory(directory);
        String[] fileName = new String[fileList.length];
        for(int i = 0; i <fileList.length; i++) {
            fileName[i] = directory+"\\"+fileList[i].getName();
            //System.out.println(fileName);
        }
        return fileName;
    }

    /**
     * gets teh respective score of the tweet text and the emojis included in the text
     * @param fileDirc
     * @return
     * @throws IOException
     */
    private static ArrayList<Double> getSentimentScore(String fileDirc) throws IOException {
        FileReader reader;
        JSONParser parser  = new JSONParser();

        ArrayList<Double> sentRank = new ArrayList<>();
        double sumRank = 0;

        //init Sentiment Rank
        SentimentRankAssignement.init();
        try {
            reader = new FileReader(fileDirc);
            Object parse = parser.parse(reader);
            JSONObject jsonObject = (JSONObject) parse;

            String text = (String) jsonObject.get("text");
            sentRank.add(SentimentRankAssignement.findSentimentRank(text));

            ArrayList<String[]> emojiDataList = getEmojis(text);
            for (String[] emojiData :
                    emojiDataList) {
                System.out.println("Emoji for: "+emojiData[2]);
                sumRank += new Double(emojiData[4]);
            }
            reader.close();
            //average Sentiment rank
            if (emojiDataList.size() != 0){
                double aveRank = sumRank/emojiDataList.size();
                System.out.println("\n Average Emoji Sentiment Rank " + aveRank);
                sentRank.add(aveRank);
            }
            else {
                //sentRank;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return sentRank;
    }

    private static ArrayList<String[]> getEmojis(String text) {
        ArrayList<String> emojiList = UnicodeEmojiSampler.identifyEmojiCode(text);
        return EmojiDataAccess.showAllEmojiFromList(emojiList);
    }

    private static void insertTweetSentimentAndEmoji(String fileDirc) throws IOException, ParseException {

        FileReader reader = new FileReader(fileDirc);
        JSONParser parser = new JSONParser();

        Object parse = parser.parse(reader);
        JSONObject jsonObject = (JSONObject) parse;

        String text = (String) jsonObject.get("text");
        double sumSent = 0;
        ArrayList<Double> indvSentRank = getSentimentScore(fileDirc);
        for (double sentimentRank: indvSentRank){
            sumSent += sentimentRank;
        }
        double avrSentRank = sumSent/indvSentRank.size();
        double textSentRank = indvSentRank.get(0);

        FileWriter writer =new FileWriter(fileDirc);
        JSONObject obj = new JSONObject();

        //write these to json file
        jsonObject.put("text_sentiment_rank_str", String.valueOf(textSentRank) );
        jsonObject.put("text_sentiment_rank", textSentRank);

        if (indvSentRank.size() == 2) {
            double emojiSentRank= indvSentRank.get(1);
            jsonObject.put("emoji_sentiment_rank_str", String.valueOf(emojiSentRank));
            jsonObject.put("sentiment_rank", emojiSentRank);

            JSONArray emojiList = new JSONArray();
            for (String[] emojiData:
                    getEmojis(text)){
                JSONObject emojiItem = new JSONObject();
                String[] tagList = emojiData[3].replace(", ", ",").split(",");
                emojiItem.put("code", emojiData[0].replace("U+", "").replace(" ", ""));
                emojiItem.put("alias", emojiData[2]);
                JSONArray tags = new JSONArray();
                for (String tag: tagList) {
                    tags.add(tag.replace(" ", ""));
                }
                emojiItem.put("tags", tags);

                emojiList.add(emojiItem);
            }

            jsonObject.put("emojiList",  emojiList);

        }
        else {
            jsonObject.put("emoji_sentiment_rank_str", null);
            jsonObject.put("sentiment_rank", null);
            jsonObject.put("emojiList",  null);

        }

        jsonObject.put("total_sentiment_rank_str", String.valueOf(avrSentRank) );
        jsonObject.put("total_sentiment_rank",  avrSentRank);

        System.out.println("\n Total Average Sentiment Rank " + avrSentRank);


        jsonObject.writeJSONString(jsonObject,writer);
        writer.close();
    }


    public static void main(String[] args) {

        String baseDir = System.getProperty("user.dir");
        String tweetDir = baseDir + "\\resources\\DTweets_02162016";
        System.out.println("Tweet file just analysed: "+tweetDir);
        for (String val :
                fileList(tweetDir)) {
            try {
                insertTweetSentimentAndEmoji(val);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println(val);
        }

    }
}
