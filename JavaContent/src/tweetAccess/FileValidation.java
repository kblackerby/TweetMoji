package tweetaccess;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Bukunmi on 2/14/2016.
 */

/**
 * Class provides system with proof that the directory reference exist
 * Validate the file format and also
 */
public class FileValidation {

    /**
     * List the files present in a particular directory, it also looks at inner directories
     * @param directoryPath Directory PAth
     * @return ArrayList of  Files in the Directory
     */
    public ArrayList<File> listJSONFilesInDirectory(String directoryPath){
        File parentDir;
        File[] fileName;
        File[] childDir;
        ArrayList<File> paths = new ArrayList<>();

        parentDir = new File(directoryPath);

        //for every file/folder in directory if the file is a directory, read form the folder
        // and return the file path as String
        assert parentDir.listFiles() != null;
        for (File file: parentDir.listFiles()){
            if(file.isDirectory()){
                System.out.println("Available Directory - "+file.getName());
                childDir = file.listFiles(
                        (dir, name) -> {
                            return name.endsWith("json");
                        });
                Collections.addAll(paths, childDir);
                }
        }

        fileName = parentDir.listFiles(
                (dir, name) -> new File(name).isDirectory() && name.endsWith("json"));
        Collections.addAll(paths, fileName);

        System.out.println("Reading File List: "+ paths.size()+" in Directory");

        return paths;
    }

    /**
     * This is used to list .JPG files in a single dirc, - it pays no mind to inner directories
     * @param directoryPath Directory PAth
     * @return ArrayList of  Files in the Directory
     */
    public ArrayList<File> listJPEGFileInDirectory(String directoryPath){
        File parentDir = new File(directoryPath);
        ArrayList<File> fileName = new ArrayList<>(0);
        Collections.addAll(fileName, parentDir.
                listFiles((dir, name) -> {
                    if (new File(name).isDirectory()){
                        return false;
                    }
                    //return only .JPG files
                    return name.endsWith("JPG");
                }));
        return fileName;
    }
}
