package emojireader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Bukunmi on 3/6/2016.
 * No longer using this File, now usingthe UnicodeEmojiSampler Class
 */
public class ConvertEmojiUTFCode {
    private String inputUTF_16;
    private String outputUTF_Hex;

    /**
     *
     * @return
     */
    public String getInputUTF_16() {
        return inputUTF_16;
    }

    /**
     *
     * @param inputUTF_16
     */
    public void setInputUTF_16(String inputUTF_16) {
        this.inputUTF_16 = inputUTF_16;
    }

    /**
     *
     * @return
     */
    public String getOutputUTF_Hex() {
        return outputUTF_Hex;
    }

    /**
     *
     * @param outputUTF_Hex
     */
    public void setOutputUTF_Hex(String outputUTF_Hex) {
        this.outputUTF_Hex = outputUTF_Hex;
    }

    /**
     *
     * @param file
     * @return Boolean
     */
    public boolean checkDirectory(Path file){
        //method checks the file directory exist
        if (Files.exists(file)){
            //Check what directory you are exactly
            System.out.println("File " + file.getFileName() + " Exist");

            return true;
        }
        else {
            System.out.println("The Selected file doesn't Exist.");
            return false;
        }
    }

    /**
     *
     * @param emojiTxtFile
     * @param inputUTF
     */
    public static void writeEmojiTxt(String emojiTxtFile, String inputUTF) {
        FileWriter w;
        try {
            w = new FileWriter(emojiTxtFile, true);
            BufferedWriter bW = new BufferedWriter(w);


            if(inputUTF.startsWith("U+")){
                bW.append(inputUTF);}
            else {
                bW.append("U+"+Long.toHexString(inputUTF.codePointAt(0)));}
            bW.newLine();
            bW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void emptyEmojiTxt(String emojiTxtFile) {
        FileWriter w;
        try {
            w = new FileWriter(emojiTxtFile);
            BufferedWriter bW = new BufferedWriter(w);
            bW.flush();
            bW.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param fileName
     * @return String
     * @throws IOException
     */
    public ArrayList readEmojiTxt(String fileName, String compareString){

        FileReader reader = null;
        String fileLine;
        ArrayList<String> fileLines = new ArrayList<>();

        try {
            reader = new FileReader(fileName);
            BufferedReader bR = new BufferedReader(reader);

            while((fileLine = bR.readLine())!= null) {
                /*if ((fileLine.equalsIgnoreCase(compareString)))
                {
                    System.out.println("Emoji Code is Already in File");
                    return null;
                }*/
                fileLine = bR.readLine();
                fileLines.add(fileLine);
                System.out.println("Added "+ fileLine);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLines;
    }


    /**
     * Get input code in string and convert it to UTF-32
     */
    public String compareUniCode(String emojiListFile){
        Path fileName2 = Paths.get(emojiListFile);
        File fileName = new File(emojiListFile);

        EmojiListReader emojiListReader = new EmojiListReader();


        if((checkDirectory(fileName2))){
            try {
                String[] codeVal = emojiListReader.getEmojiDataByUniCode(fileName.toString(), getOutputUTF_Hex());
                System.out.println(codeVal[1]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getOutputUTF_Hex();

    }

    /**
     * Test  for initial Emoji Identification in String and emoji description for these emoji
     * @param args
     */
    public static void main(String[] args){/*
        String inputCode = "\u270A\uD83C\uDFFE\uD83C\uDFC0";

        String  baseDir = System.getProperty("user.dir");

        String emojiDir = "\\emojilist\\";
        String emojiTxtFile = baseDir+emojiDir+"emojiSample.txt";
        String emojiListFile = baseDir+emojiDir+"EmojiList.xls";

        ConvertEmojiUTFCode convertEmojiUTFCode = new ConvertEmojiUTFCode();
        convertEmojiUTFCode.setInputUTF_16(inputCode);

        writeEmojiTxt(emojiTxtFile, convertEmojiUTFCode.getInputUTF_16());
        System.out.println("Input Code: " +convertEmojiUTFCode.getInputUTF_16());

        /*String convEmojiCode =convertEmojiUTFCode.readEmojiTxt(emojiTxtFile, "");
        System.out.println("Converted Code: " +convEmojiCode);

        convertEmojiUTFCode.setOutputUTF_Hex(convertEmojiUTFCode.readEmojiTxt(emojiTxtFile, ""));

        convertEmojiUTFCode.compareUniCode(emojiListFile);*/

    }


}
