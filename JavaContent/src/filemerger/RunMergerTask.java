package filemerger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Bukunmi on 2/19/2016.
 */
public class RunMergerTask {
    public List<String> filesDueForMerging(String fromMain, String fromD, String fromK ) {
        FileValidation fileValidation = new FileValidation();
        MergeServices mergeServices = new MergeServices();

        String mergedFileName;
        String fileDate;

        String extractFromD = fromMain+fromD;
        String extractFromK = fromMain+fromK;
        String extractedFileDir = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\JavaContent\\Tweets\\ParsedMerged\\";

        //Path path = Paths.get(extractFromMain);


        File[] filesD = fileValidation.listFilesInDirectory(extractFromD);
        File[] filesK = fileValidation.listFilesInDirectory(extractFromK);

        List<File> files = new ArrayList<>(Arrays.asList(filesD));
        files.addAll(Arrays.asList(filesK));


        List<String> fileDateList = new ArrayList<>();
        for (File file :
                files) {
            String fileNameExt = file.getName();
            String fileNameX = fileNameExt.substring(0, fileNameExt.lastIndexOf("."));
            fileDate = fileNameX.substring(fileNameX.lastIndexOf("_") + 1, fileNameX.length());
            mergedFileName = "New" + mergeServices.getFileName(fileDate);


            Path extractedFilePath = Paths.get(extractedFileDir + mergedFileName);
            Path indexFile = Paths.get(extractedFileDir + "MergerIndex.txt");

            try {
                if (mergeServices.checkIndexFile(mergedFileName, indexFile.toFile())) {
                } else {
                    if (!(fileDateList.contains(fileDate))) {
                        fileDateList.add(fileDate);
                    } else {
                        fileDateList.remove(fileDate);
                        fileDateList.add(fileDate);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String val : fileDateList) {
            System.out.println(val+"'s feeds need to be Added");
        }
        return fileDateList;
    }


    public static void main(String arg[]){
        MergeServices mergeServices = new MergeServices();
        RunMergerTask tester = new RunMergerTask();

        String extractFromMain = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\JavaContent\\Tweets\\UnParsed\\";
        String extractFromD = "DanielFeeds\\";
        String extractFromK = "KenanFeeds\\";


        List<String> fileDateList = tester.filesDueForMerging(extractFromMain,extractFromD,extractFromK);

        for (String filedate :
                fileDateList) {
            String fileName1 = "D"+mergeServices.getFileName(filedate);
            String fileName2 = "K"+mergeServices.getFileName(filedate);
            System.out.println(fileName2 + " and "+fileName1+" needs to be indexed");

            String mergedFilePath = "New" + mergeServices.getFileName(filedate);
            boolean extractedFile;

            Path file1Path = Paths.get(extractFromMain+extractFromD+fileName1);
            Path file2Path = Paths.get(extractFromMain+extractFromK+fileName2);

            File[] files = new File[2];
            files[0] = file1Path.toFile().getAbsoluteFile();
            files[1] = file2Path.toFile().getAbsoluteFile();

            System.out.println("mergedFilePath Stated "+mergedFilePath+" where");
            try {
                extractedFile = mergeServices.mergeTweetFiles(files, mergedFilePath);
                System.out.println("File Extracted? " + extractedFile);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }







    }

}


