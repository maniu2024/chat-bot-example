package org.example.utils;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;

/**
 * @author Martin
 * @date 2024/7/11 下午3:39
 * @desciption: 操作PDF文档工具类
 */
public class PdfUtils {

    /**
     * 将文件中全部的文本内容提取出来
     * @param path pdf文件路径
     * @return 全部文本内容
     */
    public static String getText(String path) {
        StringBuilder sb = new StringBuilder();
        File file = new File(path);
        try(PDDocument document = PDDocument.load(file)) {
            int pageSize = document.getNumberOfPages();
            for (int i = 0; i < pageSize; i++) {
                // 文本内容
                PDFTextStripper stripper = new PDFTextStripper();
                // 设置按顺序输出
                stripper.setSortByPosition(true);
                stripper.setStartPage(i + 1);
                stripper.setEndPage(i + 1);
                String text = stripper.getText(document);
                sb.append(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
