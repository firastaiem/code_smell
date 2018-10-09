package com.training.cleannames;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Merge {
    public static void main(String[] args) {
//        String s = "testing/SimpleTextFile.txt";
//        File f = new File(s);
        new Merge().readAndWrite();
    }

    public void readAndWrite() {
        //read the input path
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        List<String> passedFiles = new ArrayList<>();
        copy(new File(s), true, passedFiles);

    }

    //using for flag arguments should be avoided, and use another method instead
    // passedFiles is ued to return values, it should not be used like that, and instead us the return type
    public boolean copy(File fileToLoadOrig, boolean withDuplicate, List<String> passedFiles) {
        //Let us get the current Date
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int day = localDate.getDayOfMonth();

        String destDirectoryPath = year + "-" + month + "-" + day;

        //check if directory is created

        File destDirecoty = new File("./testing/dest/" + destDirectoryPath);
        System.out.println("the file path:" + destDirecoty.getAbsolutePath());

        if (!destDirecoty.exists()) {
            System.out.println("creating a new directory with path:" + destDirecoty.getAbsolutePath());
            boolean created = destDirecoty.mkdirs();
            System.out.println("is file created:" + created);
        }
        System.out.println("Directory is created");
        //Great, now let us check if the file already in the dirctory


        boolean duplicateFound = false;


        File[] sourceFiles = fileToLoadOrig.listFiles();

        for (File sourceFile : sourceFiles) {
            //let us get the file extension
            String sourceFileType = "";
            int location = sourceFile.getName().lastIndexOf('.');
            if (location > 0) {
                sourceFileType = sourceFile.getName().substring(location + 1);
            }
            //cool, let us go on
            String nestedDirectory;
            switch (sourceFileType) {
                case "txt":
                    nestedDirectory = "txt";
                    break;
                case "pdf":
                    nestedDirectory = "pdf";
                    break;
                case "xls":
                    nestedDirectory = "xls";
                    default:
                        nestedDirectory = "others";
            }

            //check if the type directory is created or not
            File nestedDirecotyFile = new File(destDirecoty.getAbsolutePath()+"/" + nestedDirectory);
            System.out.println("the file path:" + nestedDirecotyFile.getAbsolutePath());

            if (!nestedDirecotyFile.exists()) {
                System.out.println("creating a new directory with path:" + nestedDirecotyFile.getAbsolutePath());
                boolean created = nestedDirecotyFile.mkdirs();
                System.out.println("is file created:" + created);
            }



            File[] destinationFiles = nestedDirecotyFile.listFiles();

            for (File destinationFile : destinationFiles) {
                if (destinationFile.getName().equals(sourceFile.getName())) {
                    if (withDuplicate) {
                        int maxSeqNum = 0;
                        //Let us find the last sequence of the destinationFile
                        for (File fileForDuplicate : destinationFiles) {
                            String[] fileParts = fileForDuplicate.getName().split("_");
                            if (fileParts.length == 2) {
                                //got a split
                                if (fileParts[0].equals(destinationFile.getName())) {
                                    maxSeqNum = Math.max(maxSeqNum, Integer.parseInt(fileParts[1]));
                                }
                            }else{
                                new RuntimeException("Files were found with multiple _");
                            }
                        }
                        String newFilePath = sourceFile.getName() + "_" + (maxSeqNum + 1);
                        try {
                            FileUtils.copyFile(sourceFile, new File(nestedDirecotyFile.getAbsoluteFile() + "/" + newFilePath));
                            passedFiles.add(sourceFile.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    } else {
                        // let us delete the destinationFile and replace it
                        destinationFile.delete();
                        try {
                            FileUtils.copyFile(sourceFile, new File(nestedDirecotyFile.getAbsoluteFile() + "/" + sourceFile.getName()));
                            passedFiles.add(sourceFile.getAbsolutePath());
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                    break;
                } else {
                    try {
                        FileUtils.copyFile(sourceFile, new File(nestedDirecotyFile.getAbsoluteFile() + "/" + sourceFile.getName()));
                        passedFiles.add(sourceFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }

            try {
                FileUtils.copyFile(sourceFile, new File(nestedDirecotyFile.getAbsoluteFile() + "/" + sourceFile.getName()));
                passedFiles.add(sourceFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }
        return true;
    }
}
