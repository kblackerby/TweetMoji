package emojireader;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Bukunmi on 3/31/2016.
 */
public class EmojiDataAccess {
    /**
     * Give Access to the EmojiListReader, the class prevents direct access to the EmojiListDate
     * @param emojiList
     * @return
     */
    public static ArrayList<String[]> showAllEmojiFromList(ArrayList<String> emojiList) {
        //emoji directory
        String baseDir = System.getProperty("user.dir");
        String emojiListFile = baseDir + "\\emojilist\\EmojiList.xls";

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
