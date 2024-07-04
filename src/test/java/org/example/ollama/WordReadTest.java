package org.example.ollama;

import cn.hutool.poi.word.DocUtil;
import org.junit.Test;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;

public class WordReadTest {

    private final static String FILE_DIR = "/data/datasets/";


    @Test
    public void testReadDocx() {
        File file = new File(FILE_DIR + "中华人民共和国劳动法.docx");
        FileInputStream fis = null;
        XWPFDocument document = null;
        XWPFWordExtractor extractor = null;
        try {
            fis = new FileInputStream(file);
            document = new XWPFDocument(fis);
            extractor = new XWPFWordExtractor(document);
            System.out.println(extractor.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
