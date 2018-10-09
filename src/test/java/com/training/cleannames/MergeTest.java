package com.training.cleannames;

import org.apache.commons.io.FileUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import static org.testng.Assert.*;

public class MergeTest {
    private Merge merge;

    @BeforeMethod
    public void setup() {
        File destDirectory = new File("./testing/dest");
        if (destDirectory.exists()) {
            try {
                FileUtils.deleteDirectory(destDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.merge = new Merge();
    }

    @Test
    public void shouldCopyFilesWhenSourceDirectoryHasFiles() {
        File sourceDir = new File("./testing/test_source");
        sourceDir.mkdirs();
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();

        String destDirectory = year + "-" + month + "-" + day;
        File destFile = new File("./testing/dest/"+destDirectory+"/txt");

        File file1 = new File(sourceDir.getPath() + "/file1.txt");
        try {
            FileUtils.writeStringToFile(file1,"File 1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file2 = new File(sourceDir.getPath() + "/file2.txt");
        try {
            FileUtils.writeStringToFile(file2,"File 2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        merge.copy(new File("./testing/test_source"),true, new ArrayList<>());
        assertEquals(2,destFile.listFiles().length);


    }

    @Test
    public void shouldCopyFilesWhenSourceDirectoryHasDuplicateFiles() {
        File sourceDir = new File("./testing/test_source");
        sourceDir.mkdirs();
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int month = localDate.getMonthValue();
        int day   = localDate.getDayOfMonth();

        String destDirectory = year + "-" + month + "-" + day;
        File destFile = new File("./testing/dest/"+destDirectory+"/txt");


        File file1 = new File(sourceDir.getPath() + "/file1.txt");
        try {
            FileUtils.writeStringToFile(file1,"File 1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file2 = new File(sourceDir.getPath() + "/file2.txt");
        try {
            FileUtils.writeStringToFile(file2,"File 2");
        } catch (IOException e) {
            e.printStackTrace();
        }
        merge.copy(new File("./testing/test_source"),true, new ArrayList<>());
        merge.copy(new File("./testing/test_source"),true, new ArrayList<>());
        assertEquals(4,destFile.listFiles().length);

    }
}