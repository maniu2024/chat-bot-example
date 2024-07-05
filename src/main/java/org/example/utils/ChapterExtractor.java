package org.example.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.example.domain.DocChapter;
import org.example.domain.DocUnit;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ChapterExtractor {

    public static void main(String[] args) {
        String filePath = "/data/datasets/中华人民共和国劳动法.docx";
        extractChapters(filePath);
    }


    public static List<DocChapter> extractChapters(String filePath) {
        Map<String, List<String>> resultMap = new LinkedHashMap<>();
        List<DocChapter> docChapters = new ArrayList<>();

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
                List<String> unitList = Arrays.asList(contents.split("\\$\\$"));

                List<DocUnit> docUnitList = unitList.stream()
                        .filter(StrUtil::isNotEmpty)
                        .map((u) -> {
                            u = MyStrUtils.delBlank(u);
                            DocUnit docUnit = new DocUnit();
                            String[] split = u.split("\\s+");
                            if (split.length < 2) {
                                return null;
                            }
                            String unitName = split[0];
                            docUnit.setUnitName(unitName);
                            docUnit.setContent(split[1]);
                            return docUnit;
                        }).filter(ObjectUtil::isNotNull).toList();

                DocChapter docChapter = new DocChapter();
                docChapter.setChapterName(chapterTitles.get(i));
                docChapter.setDocUnitList(docUnitList);
                docChapters.add(docChapter);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return docChapters;
    }
}