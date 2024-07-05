package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.*;
import java.util.List;

@Slf4j
public class DocUtils {


    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readDocFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = null;
        XWPFDocument document = null;
        XWPFWordExtractor extractor = null;
        fis = new FileInputStream(file);
        document = new XWPFDocument(fis);
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        String text = paragraphs.get(0).getText();
        extractor = new XWPFWordExtractor(document);
        return extractor.getText();
    }




}
