package tweetaccess;

import edu.stanford.nlp.util.CollectionUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
     * @param directoryPath
     * @return
     */
    public ArrayList<File> listFilesInDirectory(String directoryPath){
        //List the files present in a particular directory

        File parentDir;
        File[] fileName;
        File[] childDir;
        ArrayList<File> paths = new ArrayList<>();

        parentDir = new File(directoryPath);
        //for every file/folder in directory if the file is a directory, read form the folder
        // and return the file path as String
        for (File file: parentDir.listFiles()){
            if(file.isDirectory()){
                System.out.println("Available Directory - "+file.getName());
                childDir = file.listFiles(
                        new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                return name.endsWith("json");
                            }
                        });
                Collections.addAll(paths, childDir);
                }
        }

        fileName = parentDir.listFiles(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        if (!new File(name).isDirectory()){
                            return false;
                        }
                        return name.endsWith("json");
                    }
                });
        Collections.addAll(paths, fileName);

        System.out.println("Reading File List: "+ paths.size()+" in Directory");

        return paths;
    }

    public static void main(String[] args) {

        //main method was used to test FileValidation class
        String baseDir = System.getProperty("user.dir");
        String tweetDir = baseDir + "\\resources\\IndividualTweetsDaniel";


        FileValidation fileValidation = new FileValidation();
        fileValidation.listFilesInDirectory(tweetDir);


    }
}
