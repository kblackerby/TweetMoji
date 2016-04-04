package emojireader;

import emojisentimentanalysis.SentimentRankAssignement;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
     * @param emojiFileNameStr
     * @param transUniCode
     * @return Sting[] emojiData -> Code - Description - Tags - Sentiment Rank if it exist on the list
     * @throws IOException
     */
    public String[] getEmojiDataByUniCode(String emojiFileNameStr, String transUniCode) throws IOException {

        HSSFSheet emojiSheet = openEmojiSheet(emojiFileNameStr);
        //System.out.print("Compare UniCode  " + transUniCode);
        Row row;

        Iterator<Row> rowIterator = emojiSheet.iterator();
        String[] emojiData = new String[0];

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Cell uniCodeRow = row.getCell(1);
            String uniCodeVal = cellDataType(uniCodeRow);

            if (transUniCode.equalsIgnoreCase(uniCodeVal)) {
                Cell emoji = row.getCell(1);
                Cell name = row.getCell(2);
                Cell tags = row.getCell(4);
                Cell rank = row.getCell(5);
                emojiData = new String[]{cellDataType(emoji), cellDataType(name),
                        cellDataType(tags), cellDataType(rank)};
            }

        }
        closeEmojiSheet(emojiFileNameStr);
        return emojiData;
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
        //Total List of Emojis in file
        int lastRowNum = emojiSheet.getLastRowNum();
        System.out.println("\nTotal Number of Rows " + lastRowNum);

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
            System.out.println("Emoji Description "+ (i+1) +": "+emojiData[1]);
        }

        return emojiDataList;
    }

    public void assingSentimentRank(String emojiFileNameStr ) throws IOException {
        HSSFSheet emojiSheet = openEmojiSheet(emojiFileNameStr);
        Row row;
        SentimentRankAssignement.init();
        Iterator<Row> rowIterator = emojiSheet.iterator();
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Cell tags = row.getCell(4);
            Cell ranks = row.getCell(5);
            if (cellDataType(tags).isEmpty()) {
                ranks.setCellValue(
                        SentimentRankAssignement.findSentimentRank(cellDataType(tags)));
            }
        }
        writeChangeToEmojiSheet(emojiFileNameStr);
        closeEmojiSheet(emojiFileNameStr);
        //return 1;
    }

    /**
     * Open the Excel Sheet with Emojis listed
     * @param fileNameStr
     * @return HSSFSheet open xls object
     * @throws IOException
     */
    private HSSFSheet openEmojiSheet(String fileNameStr) throws IOException {
        if(fileNameStr.equalsIgnoreCase("")){
            System.out.println("No file selected");
            return null;
        }
        //Path fileName = Paths.get(fileNameStr);
        HSSFWorkbook workbook = new HSSFWorkbook(
                new FileInputStream(new File(fileNameStr)));
        return workbook.getSheetAt(0);
    }

    /**
     * closes the open Excel Sheet
     * @param fileNameStr
     * @throws IOException
     */
    private void closeEmojiSheet (String fileNameStr) throws IOException {
        FileInputStream fis = new FileInputStream(fileNameStr);
        fis.close();
    }

    /**
     * write changes to the emoji file list
     * @param fileNameStr
     * @throws IOException
     */
    private void writeChangeToEmojiSheet (String fileNameStr) throws IOException {
        closeEmojiSheet(fileNameStr);
        FileOutputStream fos = new FileOutputStream(fileNameStr);

        HSSFWorkbook workbook = openEmojiSheet(fileNameStr).getWorkbook();
        workbook.write(fos);
        fos.close();
    }

    /**
     * Get return value of cell to String
     * @param cellRow
     * @return String cellStrVal
     */
    private String cellDataType(Cell cellRow) {
        String cellVal = " ";
        String cellStrVal = " ";
        switch (cellRow.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                cellVal = String.valueOf(cellRow.getNumericCellValue());
                cellStrVal = cellVal.substring(0, cellVal.lastIndexOf("."));
                //System.out.println(cellStrVal);
                break;

            case Cell.CELL_TYPE_STRING:
                cellStrVal = cellRow.getStringCellValue();
                break;

            case Cell.CELL_TYPE_BLANK:
                //System.out.println(cellStrVal);
                break;

            default:
                //System.out.println(cellRow.getStringCellValue());
        }
        return cellStrVal;
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
