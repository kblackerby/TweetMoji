package emojireader;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Bukunmi on 2/28/2016.
 */
public class EmojiListReader {

    /*private String getEmojiListByDescription(String transCharset){
        return "";
    }*/

    /**
     * Method compared the input file line with the Excel Sheet list and extractthe emoji information
     * @param EmojiFileNameStr
     * @param transUniCode
     * @return Sting[] emojiData -> Code - Description - Tags - Sentiment Rank if it exist on the list
     * @throws IOException
     */
    public String[] getEmojiDataByUniCode(String EmojiFileNameStr, String transUniCode) throws IOException {

        HSSFSheet emojiSheet = openEmojiSheet(EmojiFileNameStr);
        //System.out.print("Compare UniCode  " + transUniCode);
        Row row;

        Iterator<Row> rowIterator = emojiSheet.iterator();
        String uniNameStr;
        String[] emojiData;
        while (rowIterator.hasNext()) {
            row = rowIterator.next();

            Cell uniCodeRow = row.getCell(1);
            String uniCodeVal = cellDataType(uniCodeRow);

            //System.out.println("UniCode " + uniCodeVal);

            if (transUniCode.equalsIgnoreCase(uniCodeVal)) {
                Cell emoji = row.getCell(1);
                Cell name = row.getCell(2);
                Cell tags = row.getCell(3);
                Cell rank = row.getCell(4);
                emojiData = new String[]{emoji.getStringCellValue(), name.getStringCellValue(),
                        tags.getStringCellValue(), rank.getStringCellValue()};
                //uniNameStr = uniName.getStringCellValue();
                //System.out.println("\t Name Description: " + uniNameStr);
                return emojiData;
            }

        }
        return null;
    }

    /**
     * Open the Excel Sheet with Emojis listed
     * @param fileNameStr
     * @return HSSFSheet open xls object
     * @throws IOException
     */
    public HSSFSheet openEmojiSheet(String fileNameStr) throws IOException {
        if(fileNameStr.equalsIgnoreCase("")){
            System.out.println("No file selected");
            return null;
        }
        FileInputStream fis = new FileInputStream(fileNameStr);
        //Path fileName = Paths.get(fileNameStr);
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileNameStr));
        return workbook.getSheetAt(0);
    }

    /**
     * An ArrayList of Emoji Data
     * @param emojFilleNameStr
     * @param emojiCodeList
     * @return ArrayList<String[]> of of emojicode list read form the String
     * @throws IOException
     */
    public ArrayList<String[]> getEmojiDataListByUniCode(String emojFilleNameStr,
                                                         ArrayList<String> emojiCodeList) throws IOException {
        HSSFSheet emojiSheet = openEmojiSheet(emojFilleNameStr);
        String[] emojiData;
        /**
         * emojiData[0] = emojiCode;
         * emojiData[1] = emojiDescription;
         * emojiData[2] = emojiTags;
         * emojiData[3] = emojiSentiment;
         */
        ArrayList<String[]> emojiDataList = new ArrayList<>();

        for (int i = 0; i < emojiCodeList.size(); i++) {
            emojiCodeList.get(i);
            //emojiDataList is basically an array of emoji description form the excel file
            emojiData = getEmojiDataByUniCode(emojFilleNameStr, emojiCodeList.get(i));
            emojiDataList.add(emojiData);
        }
        //Total List of Emojis in file
        int lastRowNum = emojiSheet.getLastRowNum();
        System.out.println("\nTotal Number of Rows " + lastRowNum);

        return emojiDataList;
    }

    private String cellDataType(Cell uniCodeRow) {
        String uniCell = " ";
        String uniCell2 = " ";
        String uniCellVal = " ";
        switch (uniCodeRow.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                uniCell = String.valueOf(uniCodeRow.getNumericCellValue());
                uniCellVal = uniCell.substring(0, uniCell.lastIndexOf("."));
                //System.out.println(uniCellVal);
                break;

            case Cell.CELL_TYPE_STRING:
                uniCellVal = uniCodeRow.getStringCellValue();
                /*if (uniCell2.startsWith("U+") || uniCell2.contains("U+")){
                    uniCellVal = uniCell2.substring(uniCell2.lastIndexOf("+") + 1, uniCell2.length());
                }*/
                //System.out.println(uniCellVal);
                break;

            case Cell.CELL_TYPE_BLANK:
                //System.out.println(uniCellVal);
                break;

            default:
                //System.out.println(uniCodeRow.getStringCellValue());
        }
        return uniCellVal;
    }

    public static void main(String[] args){
      /*  String fileNameString = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\JavaContent\\emojilist\\EmojiList.xls";
        Path fileName2 = Paths.get(fileNameString);
        File fileName = new File(fileNameString);

        ConvertEmojiUTFCode converter = new ConvertEmojiUTFCode();

        FileValidation fileValidation = new FileValidation();
        EmojiListReader emojiListReader = new EmojiListReader();


        if((fileValidation.checkDirectory(fileName2))){
            try {
                //System.out.println(fileName.toString());
                emojiListReader.getEmojiDataByUniCode(fileName.toString(), "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

}
}
