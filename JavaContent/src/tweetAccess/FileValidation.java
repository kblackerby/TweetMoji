package tweetaccess;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by Bukunmi on 2/14/2016.
 */

/**
 * Class provides system with proof that the directory reference exist
 * Validate the file format and also
 */
public class FileValidation {

    /**
     *
     * @param file
     * @return
     */
    public File[] checkDirectory(String file){
        //method checks the file directory exist
        File name = new File(file);
        //if this is a directory, open it and list the files in the directory

        if (name.isDirectory()){
            return listFilesInDirectory(file);

        }
        //else return the files in the
        else return listFilesInDirectory(file);

        /*if (Files.exists(file)){
            //Check what directory you are exactly
            System.out.println("File " + file.getFileName() + " Exist");
            return true;
        }
        else {
            System.out.println("The Selected file doesn't Exist.");
            return false;
        }*/
    }

    /**
     *
     * @param directoryPath
     * @return
     */
    public File[] listFilesInDirectory(String directoryPath){
        //List the files present in a particular directory

        File files;
        File[] paths;


        files = new File(directoryPath);

        // return  only files with .json extensions in directory
        paths = files.listFiles(
                new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith("json");
            }
        });
        System.out.println("Reading File List: "+ paths.length+" in Directory");
        //System.out.println(paths);

        return paths;
    }

    public static void main(String[] args) {

        //main method was used to test FileValidation class
        String homePath = System.getProperty("User dir");
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

    }
}
