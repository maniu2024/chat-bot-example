package org.example.utils;

import cn.hutool.core.util.StrUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ChapterExtractor {

    public static void main(String[] args) {
        String filePath = "/data/datasets/中华人民共和国劳动法.docx";
        extractChapters(filePath);
    }


    public static Map<String, List<String>> extractChapters(String filePath) {
        Map<String, List<String>> resultMap = new LinkedHashMap<>();
        try (FileInputStream fis = new FileInputStream(filePath);
             XWPFDocument document = new XWPFDocument(fis)) {

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            List<String> chapterTitles = new ArrayList<>();
            List<StringBuilder> chapterContents = new ArrayList<>();
            int currentChapterIndex = -1;

            for (XWPFParagraph paragraph : paragraphs) {
                String text = paragraph.getText();

                if (text.matches("^第.*章.*$")) {
                    chapterTitles.add(text);
                    chapterContents.add(new StringBuilder());
                    currentChapterIndex++;
                } else {
                    if (currentChapterIndex >= 0) {
                        text = StrUtil.trim(text);
                        if (StringUtils.isEmpty(text)) {
                            continue;
                        }
                        StringBuilder stringBuilder = chapterContents.get(currentChapterIndex);
                        if (text.matches("第.+条.+$")) {  // which article
                            if (StrUtil.isNotEmpty(stringBuilder.toString())) {
                                stringBuilder.append("$$");
                            }

                        }
                        stringBuilder.append(text).append("\n");
                    }
                }
            }

            // 打印章节标题和内容
            for (int i = 0; i < chapterTitles.size(); i++) {
                String contents = chapterContents.get(i).toString();
                List<String> contentList = (List<String>) Arrays.asList(contents.split("\\$\\$"));
                resultMap.put(chapterTitles.get(i), contentList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
}