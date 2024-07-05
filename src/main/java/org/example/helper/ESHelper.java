package org.example.helper;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.micrometer.common.util.StringUtils;
import org.example.config.ConfigProperties;
import org.example.entity.LawArticle;
import org.example.entity.LawChapter;
import org.example.utils.DocUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ESHelper {


    public static List<LawChapter> extractChapter(String text) {

        ArrayList<LawChapter> list = new ArrayList<>();
        return list;
    }

    public static List<String> extractArticle(String text) {
        // 定义章节的正则表达式模式
        String pattern = "第(.+)条.+\\n";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(text);

        List<String> list = new ArrayList<>();

        int start = 0;
        while (m.find()) {
            int end = m.start();
            String chapterContent = m.group() + text.substring(start, end).trim();
            if(StrUtil.isNotEmpty(chapterContent)){
                list.add(chapterContent);
            }
            start = m.end();
        }
        return list;
    }


    public static void indexData() throws IOException {
        // read files
        List<String> strings = FileUtil.listFileNames(ConfigProperties.DATA_DIR);
        for (String fileName : strings) {
            String path = ConfigProperties.DATA_DIR + "/" + fileName;
            String content = DocUtils.readDocFile(path);
            List<LawChapter> lawChapterList = extractChapter(content);
            for (LawChapter lawChapter : lawChapterList) {
                List<String> contents = extractArticle(lawChapter.getChapterContent());
                for (String article : contents) {
                    LawArticle lawArticle = new LawArticle();
                    lawArticle.setLawName(fileName);
                    lawArticle.setChapterName(lawChapter.getTitle());
                    lawArticle.setArticleContent(article);
                    System.out.println("lawArticle = " + lawArticle);
                }
            }


        }


        // split file

        // write to es

    }






    public static void main(String[] args) throws IOException {
        indexData();
    }

}
