package filemerger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

/**
 * Created by Bukunmi on 2/14/2016.
 */
public class FileValidation {

    //checks the file directory exist
    public boolean checkDirectory(Path file){
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

    //check unmerged files
    public boolean checkFileNameExt(Path file) {

        //check the kind is in good shape
        if (checkDirectory(file) || Files.isRegularFile(file) || Files.isReadable(file) || Files.isWritable(file)) {

            String fileNameExt;
            String fileNameX;
            String fileExt ;
            String ext = "txt" ;

            fileNameExt = file.getFileName().toString();
            //Check file extension
            fileExt =fileNameExt.substring(fileNameExt.lastIndexOf(".") + 1, fileNameExt.length());
            fileNameX = fileNameExt.substring(0, fileNameExt.lastIndexOf("."));

            if(ext.equalsIgnoreCase(fileExt)){

                if (fileFormat(fileNameX)){
                    return true;
                }
                else {
                    System.out.println("The Selected file format isn't allowed.");
                    return false;
                }

            }
            else {
                System.out.println("The Selected file extension isn't allowed.");
                return false;
            }

        }
        else {
            System.out.println("File Permission doesn't allow action");
            return false;
        }

    }

    //checkFileNameExt call this to verify file format after checking file extension
    private boolean fileFormat(String fileNameX) {
        String fileName;
        String fileDate;
        fileName = fileNameX.substring(0, fileNameX.lastIndexOf("_"));
        fileDate = fileNameX.substring(fileNameX.lastIndexOf("_") + 1, fileNameX.length());
        //Twitter Feeds downloaded
        boolean fileOwner = (fileName.startsWith("K") || fileName.startsWith("D"));
        boolean fileForm = fileName.endsWith("Tweets") ;

        //check date format
        if (checkFileDate(fileDate) || fileOwner || fileForm){
            System.out.println("Correct File naming Format? " + fileForm);
            return true;
        }

        else{
            System.out.println("Error with File naming Format");
            return false;
        }


    }

    //List the files present in a particular directory
    public File[] listFilesInDirectory(String directoryPath){

        File files;
        File[] paths;

        files = new File(directoryPath);

        // return  pathname to files in directory
        paths = files.listFiles();
        System.out.println("Reading File List");
        System.out.println(paths);

        return paths;
    }

    //Check the file date is not today's
    private boolean checkFileDate(String fileDate) {
        Date date = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            //file Date from String to Date following the above format
            Date filedate =  format.parse(fileDate);

            //System Date from Date to String and then String to Date following format
            String currentdate =  format.format(date);
            Date currentDate =  format.parse(currentdate);

            boolean before = filedate.before(currentDate);

            if (before) {
                System.out.println("File  Date is before Current Sys Date? " + before);
                return true;
            }
            else{
                System.out.println("File  Date is before Current Sys Date? " + before);
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Date Passer Exception ");
            return  false;
        }
    }

    /*public static void main(String[] args) {

        //main method was used to test FileValidation class
        String homePath ="C:\\Users\\Bukunmi\\workspace\\TweetMoji\\Tweets";
        String extractedFilePath = "\\ParsedMerged";//\\Tweets
        String rawFilePath = "\\unParsed";//\\Tweets

        String extractedFile = homePath+extractedFilePath+"\\file3.txt";
        //String rawFile = homePath+rawFilePath+"\\file_02142016.txt";
        String rawFile = homePath+rawFilePath+"\\KTweetFeeds_20160215.txt";


        FileValidation fileValidation = new FileValidation();
        //fileValidation.checkFileNameExt(Paths.get(rawFile));

        int  i = 0;
        for (File filepath :
                fileValidation.listFilesInDirectory(homePath+rawFilePath)) {
            i += 1;
            String filename = filepath.getName();
        }

    }*/
}
