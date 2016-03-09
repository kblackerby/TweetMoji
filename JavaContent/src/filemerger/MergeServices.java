package filemerger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Bukunmi on 2/14/2016.
 */


public class MergeServices {

    public String getFileName(String fileDate){
        String fileName = "Tweets_" + fileDate +".txt";
        return fileName;
    }

    public boolean mergeTweetFiles(File[] extractFile, String newMergerFile) throws FileNotFoundException {

        FileValidation fileValidation = new FileValidation();
        String extractedFileDir = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\JavaContent\\Tweets\\ParsedMerged\\";

        Path extractedFilePath = Paths.get(extractedFileDir+newMergerFile);
        Path indexFile = Paths.get(extractedFileDir+"MergerIndex.txt");

        FileWriter nFWP = null;
        BufferedWriter nBWP = null;
        try {
            nFWP = new FileWriter(extractedFilePath.toFile().getAbsoluteFile(), true);
            nBWP = new BufferedWriter(nFWP);
        } catch (IOException e1) {
            System.out.println("newFile FileWriter Error");
            e1.printStackTrace();

        }

        //Extract the file and save in newTweet file and index
        if(fileValidation.checkDirectory(extractedFilePath)) {

            File indexFileName = indexFile.toFile().getAbsoluteFile();
            try {
                //Check if mergedFile as been added to the Index previously
                if (checkIndexFile(newMergerFile, indexFileName)) {
                    return false;}
                else {
                    for (File f : extractFile) {
                        if (fileValidation.checkFileNameExt(f.toPath())) {

                            System.out.println("Reading: " + f.getName());
                            FileInputStream fIS;
                            try {
                                fIS = new FileInputStream(f);
                                BufferedReader eBRP = new BufferedReader(new InputStreamReader(fIS));

                                String aLine;
                                while ((aLine = eBRP.readLine()) != null) {
                                    nBWP.write(aLine);
                                    nBWP.newLine();
                                }
                                eBRP.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        else { System.out.println(f.toPath().getFileName() +" not accessed, Extraction not completed"); }
                    }
                    try {
                        indexNewFile(newMergerFile);
                        nBWP.close();
                    } catch (IOException e) {
                        System.out.println("NewTweetFeed not closed or Index file not created");
                        e.printStackTrace();
                    }
                    return true;
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        else{
            System.out.println("Extraction file was not accessed, Extraction not completed");
            return false;
        }
    }

    public static boolean indexNewFile(String mergedFile) throws IOException {

        FileValidation fileValidation = new FileValidation();
        String fileDir = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\JavaContent\\Tweets\\ParsedMerged\\";
        Path indexFile = Paths.get(fileDir+"MergerIndex.txt");

        if (fileValidation.checkDirectory(indexFile)) {
            FileWriter fW = new FileWriter(indexFile.toFile().getAbsoluteFile(), true);
            BufferedWriter bW = new BufferedWriter(fW);

            bW.append(mergedFile);
            bW.newLine();
            bW.close();
            System.out.println("Extracted File Index added");
        }
        else {
            Files.createFile(indexFile);
            FileWriter fw = new FileWriter(indexFile.toFile().getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(mergedFile);
            bw.newLine();
            bw.close();
            System.out.println(" Index File Created and Extracted File Index added");
        }
        return true;

    }

    public boolean checkIndexFile(String mergedFile, File indexFileName) throws IOException {
        FileReader fR = new FileReader(indexFileName);
        BufferedReader bR = new BufferedReader(fR);

        String fileLine;
        while((fileLine = bR.readLine())!= null){
            System.out.println(fileLine);
            if ((fileLine.equalsIgnoreCase(mergedFile)))
            {
                System.out.println(mergedFile +" is in "+ indexFileName.getName() +" file");
                return true;
            }
        }
        System.out.println(mergedFile+" not found in " +indexFileName.getName() + " file");
        //checkMergerFile(indexFileName);
        return false;
    }

    /*public static void main(String[] args) {
        MergeServices mergeServices = new MergeServices();

        String extractFromDir = "C:\\Users\\Bukunmi\\workspace\\TweetMoji\\Tweets\\UnParsed\\";
        String fileName1 = "D"+mergeServices.createFileName();
        String fileName2 = "K"+mergeServices.createFileName();
        String mergedFilePath = "New" + mergeServices.createFileName();
        boolean extractedFile;

        Path file1Path = Paths.get(extractFromDir+fileName1);
        Path file2Path = Paths.get(extractFromDir+fileName2);

        File[] files = new File[2];
        files[0] = file1Path.toFile().getAbsoluteFile();
        files[1] = file2Path.toFile().getAbsoluteFile();

        try {
            extractedFile = mergeServices.mergeTweetFiles(files, mergedFilePath);
            System.out.println("File Extracted? " + extractedFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
