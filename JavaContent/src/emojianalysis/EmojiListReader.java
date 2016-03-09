package emojianalysis;

import filemerger.FileValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Created by Bukunmi on 2/28/2016.
 */
public class EmojiListReader {

    /*private String getEmojiListByDescription(String transCharset){
        return "";
    }*/

    public String getEmojiListByUniCode(String fileNameStr, String transUniCode) throws IOException {


        FileInputStream fis = new FileInputStream(fileNameStr);
        //Path fileName = Paths.get(fileNameStr);
        HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(fileNameStr));
        HSSFSheet emojiSheet = workbook.getSheetAt(0);

        int lastRowNum = emojiSheet.getLastRowNum();
        System.out.print("Compare UniCode  " + transUniCode);
        Row row;

        Iterator<Row> rowIterator = emojiSheet.iterator();
        String uniNameStr;
        while (rowIterator.hasNext()) {
            row = rowIterator.next();

            Cell uniCodeRow = row.getCell(1);
            String uniCodeVal = cellDataType(uniCodeRow);

            //System.out.println("UniCode " + uniCodeVal);

            if(uniCodeVal.equalsIgnoreCase(transUniCode)){
            Cell uniName = row.getCell(2);
            uniNameStr = uniName.getStringCellValue();
                System.out.println("\t Name Description: " + uniNameStr);
                return uniNameStr;
            }
        }
        System.out.println("\nTotal Number of Rows "+lastRowNum);
        return "";
    }

    private String cellDataType(Cell uniCodeRow) {
        String uniCellVal = " ";
        switch (uniCodeRow.getCellType())
        {
            case Cell.CELL_TYPE_NUMERIC:
                String uniCell = String.valueOf(uniCodeRow.getNumericCellValue());
                uniCellVal = uniCell.substring(0,uniCell.lastIndexOf(".") );
                //System.out.println(uniCellVal);
                break;

            case Cell.CELL_TYPE_STRING:
                String uniCell2 = uniCodeRow.getStringCellValue();
                uniCellVal = uniCell2;
                if (uniCell2.startsWith("U+") || uniCell2.contains("U+")){
                    uniCellVal = uniCell2.substring(uniCell2.lastIndexOf("+") + 1, uniCell2.length());
                }
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
        String fileNameString = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\JavaContent2\\emojilist\\EmojiListReader.xls";
        Path fileName2 = Paths.get(fileNameString);
        File fileName = new File(fileNameString);

        ConvertEmojiUTFCode converter = new ConvertEmojiUTFCode();

        FileValidation fileValidation = new FileValidation();
        EmojiListReader emojiListReader = new EmojiListReader();


        if((fileValidation.checkDirectory(fileName2))){
            try {
                //System.out.println(fileName.toString());
                emojiListReader.getEmojiListByUniCode(fileName.toString(), "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
