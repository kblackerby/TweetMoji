package emojianalysis;

import filemerger.FileValidation;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Bukunmi on 3/6/2016.
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
     * @param emojiTxtFile
     * @param inputUTF
     */
    private static void writeEmojiTxt(String emojiTxtFile, String inputUTF) {
        FileWriter w= null;
        try {
            w = new FileWriter(emojiTxtFile);
            w.write(Long.toHexString(inputUTF.codePointAt(0)));
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param inputUTF
     * @return
     */

    /*private String convertUniCode(String inputUTF){
        String outputUTF = Long.toHexString(inputUTF.codePointAt(0));
        return outputUTF;
    }*/

    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private String readEmojiTxt(String fileName){

        FileReader reader = null;
        String fileLine = "";
        try {
            reader = new FileReader(fileName);
            BufferedReader bR = new BufferedReader(reader);

            fileLine= bR.readLine();

            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileLine;
    }



    /**
     * Get input code in string and convert it to UTF-32
     */
    public String compareUniCode(){
        String fileNameString = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\JavaContent2\\emojilist\\EmojiListReader.xls";
        Path fileName2 = Paths.get(fileNameString);
        File fileName = new File(fileNameString);

        FileValidation fileValidation = new FileValidation();
        EmojiListReader emojiListReader = new EmojiListReader();


        if((fileValidation.checkDirectory(fileName2))){
            try {
                String codeVal = emojiListReader.getEmojiListByUniCode(fileName.toString(), getOutputUTF_Hex());
                System.out.println(codeVal);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getOutputUTF_Hex();

    }

    public void breakupUniCode(String s){
    //public char[] breakupUniCode(String s){

    }

    public static void main(String[] args){
        String stringInput = "\uD83D\uDE02";
        String emojiTxtFile = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\JavaContent2\\emojilist\\emojiSample.txt";

        ConvertEmojiUTFCode convertEmojiUTFCode = new ConvertEmojiUTFCode();
        convertEmojiUTFCode.setInputUTF_16(stringInput);

        writeEmojiTxt(emojiTxtFile, convertEmojiUTFCode.getInputUTF_16());
        System.out.println("Input Code: " +convertEmojiUTFCode.getInputUTF_16());

        String convEmojiCode =convertEmojiUTFCode.readEmojiTxt(emojiTxtFile);
        System.out.println("Converted Code: " +convEmojiCode);

        convertEmojiUTFCode.setOutputUTF_Hex(convertEmojiUTFCode.readEmojiTxt(emojiTxtFile));

        convertEmojiUTFCode.compareUniCode();

    }


}
