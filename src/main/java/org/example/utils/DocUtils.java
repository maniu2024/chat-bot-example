package org.example.utils;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DocUtils {


    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public String readDocFile(String fileName) throws IOException {
        File file = new File(fileName);
        FileInputStream fis = null;
        XWPFDocument document = null;
        XWPFWordExtractor extractor = null;
        fis = new FileInputStream(file);
        document = new XWPFDocument(fis);
        extractor = new XWPFWordExtractor(document);
        return extractor.getText();
    }

}
