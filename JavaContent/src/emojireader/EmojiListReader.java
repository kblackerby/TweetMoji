package emojireader;

import sentimentanalysis.SentimentRankAssignement;
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
            String uniCodeVal = stringCellDataType(uniCodeRow);

            if (transUniCode.equalsIgnoreCase(uniCodeVal)) {
                Cell emoji = row.getCell(1);
                Cell desc = row.getCell(2);
                Cell alias = row.getCell(3);
                Cell tags = row.getCell(4);
                Cell rank = row.getCell(5);
                emojiData = new String[]{stringCellDataType(emoji), stringCellDataType(desc),
                        stringCellDataType(alias), stringCellDataType(tags), stringCellDataType(rank)};
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

        String[] emojiData;
        /**
         * emojiData[0] = emojiCode;
         * emojiData[1] = emojiDescription;
         * emojiData[2] = emojiAlias;
         * emojiData[3] = emojiTags;
         * emojiData[4] = emojiSentiment;
         */
        ArrayList<String[]> emojiDataList = new ArrayList<>();

        double sumRank = 0;
        for (int i = 0; i < emojiCodeList.size(); i++) {
            emojiCodeList.get(i);
            //emojiDataList is basically an array of emoji description form the excel file
            emojiData = getEmojiDataByUniCode(emojFilleNameStr, emojiCodeList.get(i));
            emojiDataList.add(emojiData);
            System.out.println((i+1) +") - Emoji Code: "+ emojiCodeList.get(i) +" - Description: "+ ": "
                    + emojiData[1] +" - Score: "+emojiData[4]);
            sumRank += new Double(emojiData[4]);
        }

        //Total List of Emojis in file
        int lastRowNum = emojiSheet.getLastRowNum();
        System.out.println("\nTotal Number of Emojis in list " + lastRowNum);

        return emojiDataList;
    }

    /**
     * Assigns Sentiment Score to List of Emojis where there is no value initially assigned.
     * @param emojiFileNameStr
     * @throws IOException
     */
    public void assingSentimentRank(String emojiFileNameStr ) throws IOException {
        if(emojiFileNameStr.equalsIgnoreCase("")){
            System.out.println("No file selected");
            return;
        }
        HSSFWorkbook workbook = new HSSFWorkbook(
                new FileInputStream(new File(emojiFileNameStr)));
        HSSFSheet emojiSheet = workbook.getSheetAt(0);
        Row row;
        SentimentRankAssignement.init();
        Iterator<Row> rowIterator = emojiSheet.iterator();
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Cell desc = row.getCell(2);
            Cell alias = row.getCell(3);
            Cell tags = row.getCell(4);
            Cell ranks = row.getCell(5);
            String val = stringCellDataType(ranks);
            String val2 = stringCellDataType(tags);


            if (stringCellDataType(ranks) == " "
                    && !(stringCellDataType(tags) == " ")
                    && !(stringCellDataType(desc) == " ")) {
                //average the sentimentRank for the emoji from the emojiDescription and the Annoteded tags
                double aveRank = (new Double(SentimentRankAssignement.findSentimentRank(stringCellDataType(desc)))
                                 + new Double(SentimentRankAssignement.findSentimentRank(stringCellDataType(alias)))
                                 + new Double(SentimentRankAssignement.findSentimentRank(stringCellDataType(tags))))/3 ;
                ranks.setCellValue(aveRank);
                //ranks.setCellValue(
                        //SentimentRankAssignement.findSentimentRank(stringCellDataType(tags)));

            }

        }
        FileOutputStream fos = new FileOutputStream(new File(emojiFileNameStr));

        workbook.write(fos);
        closeEmojiSheet(emojiFileNameStr);
        fos.close();
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
     * Get return value of cell to String
     * @param cellRow
     * @return String cellStrVal
     */
    private String stringCellDataType(Cell cellRow) {
        String cellStrVal = " ";
        switch (cellRow.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC:
                cellStrVal = String.valueOf(cellRow.getNumericCellValue());
                //cellStrVal = cellVal.substring(0, cellVal.lastIndexOf("."));
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

}
