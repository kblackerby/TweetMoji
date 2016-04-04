package emojireader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Bukunmi on 3/31/2016.
 */
public class ShowEmojiDataList {
    /**
     * Display Emoji List in tweet
     * @param emojiListFiler
     * @param emojiList
     * @return
     */
    public static ArrayList<String[]> showAllEmojiFromList(String emojiListFiler, ArrayList<String> emojiList) {
        //emoji directory
        String baseDir = System.getProperty("user.dir");
        String emojiListFile = baseDir + "\\\\emojilist\\\\EmojiList.xls";

        ArrayList<String[]> emojiData= new ArrayList<>();
        EmojiListReader emojiListReader = new EmojiListReader();
        try {
            //emojiListReader.assingSentimentRank(emojiListFile);
            emojiData = emojiListReader.getEmojiDataListByUniCode(emojiListFile, emojiList);
            //System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  emojiData;
    }
}
