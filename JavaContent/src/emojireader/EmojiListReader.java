package emojireader;

import sentimentanalysis.SentimentRankAssignment;
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
     * Method compared the input file line with the Excel Sheet list and extract the emoji information
     * @param emojiFileNameStr
     * @param transUniCode
     * @return Sting[] emojiData -> Code - Description - Tags - Sentiment Rank if it exist on the list
     * @throws IOException
     */
    private String[] getEmojiDataByUniCode(String emojiFileNameStr, String transUniCode) throws IOException {

        HSSFSheet emojiSheet = openEmojiSheet(emojiFileNameStr);
        //System.out.print("Compare UniCode  " + transUniCode);
        Row row;

        assert emojiSheet != null;
        Iterator<Row> rowIterator = emojiSheet.iterator();
        String[] emojiData = new String[0];

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Cell uniCodeRow = row.getCell(1);
            String uniCodeVal = stringCellDataType(uniCodeRow);

            //
            if (transUniCode.equalsIgnoreCase(uniCodeVal) ) {
                Cell emoji = row.getCell(1);
                Cell desc = row.getCell(2);
                Cell alias = row.getCell(3);
                Cell tags = row.getCell(4);
                Cell rank = row.getCell(5);
                emojiData = new String[]{stringCellDataType(emoji), stringCellDataType(desc),
                        stringCellDataType(alias), stringCellDataType(tags), stringCellDataType(rank)};
            }


        }

        if(!rowIterator.hasNext()&&
                emojiData.length==0) {
            emojiData = null;
        }
        closeEmojiSheet(emojiFileNameStr);
        return emojiData;
    }

    /**
     * An ArrayList of EmojiData gotten from getEmojiByUniCode(...) Arrary of each Emoji
     * @param emojFilleNameStr
     * @param emojiCodeList
     * @return ArrayList<String[]> of of emojicode list read form the String
     * @throws IOException
     */
    public ArrayList<String[]> getEmojiDataListByUniCode(String emojFilleNameStr,
                                                         ArrayList<String> emojiCodeList) throws IOException {
        String[] emojiData;
        /**
         * mojiDataList is an array of emoji description form the excel file
         * emojiData[0] = emojiCode;
         * emojiData[1] = emojiDescription;
         * emojiData[2] = emojiAlias;
         * emojiData[3] = emojiTags;
         * emojiData[4] = emojiSentiment;
         */
        ArrayList<String[]> emojiDataList = new ArrayList<>();

        //loops throught the emoji codes pass from the parsed String
        for (int i = 0; i < emojiCodeList.size(); i++) {
            emojiCodeList.get(i);
            //get the emojiData from the excel sheet
            emojiData = getEmojiDataByUniCode(emojFilleNameStr, emojiCodeList.get(i));

            if (emojiData != null) {
                emojiDataList.add(emojiData);
                System.out.println((i + 1) + ") - Emoji Code: " + emojiCodeList.get(i) + " - Description: " + ": "
                        + emojiData[1] + " - Score: " + emojiData[4]);
            }
            else {
                //a Unicode pattern matched but not yet included in file or just not an emoji
                System.out.println(emojiCodeList.get(i) +" is not an emoji in List (but a Unicode character) ");

            }
        }
       return emojiDataList;
    }

    /**
     * Assigns Sentiment Score to List of Emojis where there is no value initially assigned.
     * Initially used in emojiReader.EmojiDataAccess
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
        SentimentRankAssignment.init();
        Iterator<Row> rowIterator = emojiSheet.iterator();
        //reads every row in the list
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            Cell desc = row.getCell(2);
            Cell alias = row.getCell(3);
            Cell tags = row.getCell(4);
            Cell ranks = row.getCell(5);

            if (stringCellDataType(ranks).equals(" ")
                    && !(stringCellDataType(tags).equals(" "))
                    && !(stringCellDataType(desc).equals(" "))) {
                //average the sentimentRank for the emoji from the emojiDescription and the Annoteded tags
                double aveRank = (SentimentRankAssignment.findSentimentRank(stringCellDataType(desc))
                                 + SentimentRankAssignment.findSentimentRank(stringCellDataType(alias))
                                 + SentimentRankAssignment.findSentimentRank(stringCellDataType(tags)))/3 ;
                ranks.setCellValue(aveRank);
                //compute and set the sentiment score
                ranks.setCellValue(
                        SentimentRankAssignment.findSentimentRank(stringCellDataType(tags)));

            }

        }
        FileOutputStream fos = new FileOutputStream(new File(emojiFileNameStr));

        //write to data to the sheets and close file
        workbook.write(fos);
        closeEmojiSheet(emojiFileNameStr);
        fos.close();
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
     * Return value of cell as String
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
