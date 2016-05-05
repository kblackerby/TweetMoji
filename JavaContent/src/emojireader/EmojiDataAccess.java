package emojireader;

import tweetaccess.FileValidation;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by Bukunmi on 3/31/2016.
 */
public class EmojiDataAccess {
    /**
     * Give Access to the EmojiListReader, the class prevents direct access to the EmojiListDate
     * @param emojiList
     * @return
     */
    public static ArrayList<String[]> showAllEmojiFromList(ArrayList<String> emojiList) throws IOException {
        //emoji directory
        String baseDir = System.getProperty("user.dir");
        String emojiListFile = baseDir + "\\resources\\emojilist\\EmojiList.xls";

        String emojiPicDir = baseDir + "\\resources\\emojilist\\Pics";

        ArrayList<String[]> emojiDataList = new ArrayList<>();
        EmojiListReader emojiListReader = new EmojiListReader();
        //emojiListReader.assingSentimentRank(emojiListFile);
        emojiDataList = emojiListReader.getEmojiDataListByUniCode(emojiListFile, emojiList);
        ArrayList<String[]> duplicateList = new ArrayList<>();
        duplicateList.addAll(emojiDataList);
        if (!duplicateList.isEmpty()) {
            collectUnavailableEmojiCode(duplicateList, emojiPicDir);
        }

        return  emojiDataList;
    }

    /**
     * the method collect list of emojis not included in the picture library
     * @param duplicateDataArrayList
     * @param emojiPicFiles
     */
    private static void  collectUnavailableEmojiCode(ArrayList<String[]> duplicateDataArrayList, String emojiPicFiles){
        String emojiPicList = emojiPicFiles +"\\listOf\\collectedEmojis.txt";

        FileValidation fileValidation = new FileValidation();
        ArrayList<File> emojiPics = fileValidation.listJPEGFileInDirectory(emojiPicFiles);

        for (int i = duplicateDataArrayList.size() - 1; i >= 0; i--) {
            if (duplicateDataArrayList.size() == i){
                i--;
            }
            String emojiCode = duplicateDataArrayList.get(i)[0]
                    .replace("U+", "").replace(" ", "");

            for (File emojiPic : emojiPics) {
                String picFile = emojiPic.getName();
                String picName = picFile.substring(0, picFile.lastIndexOf("."));

                //if the emoji picture filename is equals the emojicode in list remove from list
                if (emojiCode.equalsIgnoreCase(picName)) {
                    if (duplicateDataArrayList.size()-1 == i &&
                            duplicateDataArrayList.size() == 1 &&
                            picName.equalsIgnoreCase(emojiCode)) {
                        duplicateDataArrayList = null;
                    }
                    else{
                        duplicateDataArrayList.remove(i);
                        //i++;

                    }
                }
            }
        }
        //Collected Emojis not in Pic List there is any
        if (duplicateDataArrayList != null) {
            ArrayList<String[]> newCollectedList = duplicateDataArrayList;
            for (String newCollection[] : newCollectedList) {
                System.out.println("Collected " + newCollection[0] + " For observation");
                if (readEmojiTxt(newCollection[0], emojiPicList) == null) {
                    writeEmojiTxt(newCollection[0], emojiPicList);
                }
            }
        }


    }

    private static String readEmojiTxt(String compareString, String fileName){

        FileReader reader;
        String fileLine = null;

        try {
            reader = new FileReader(fileName);
            BufferedReader bR = new BufferedReader(reader);

            while ((fileLine = bR.readLine()) != null) {
                if ((fileLine.equalsIgnoreCase(compareString))) {
                    System.out.println("Emoji Code is Already in File");
                    return fileLine;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLine;
    }

    private static void writeEmojiTxt(String inputEmoji, String emojiTxtFile) {
        FileWriter w;
        try {
            w = new FileWriter(emojiTxtFile, true);
            BufferedWriter bW = new BufferedWriter(w);

            bW.append(inputEmoji);
            bW.newLine();
            bW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
