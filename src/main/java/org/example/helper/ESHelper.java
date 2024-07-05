package org.example.helper;

import cn.hutool.core.io.FileUtil;
import org.example.config.ConfigProperties;
import org.example.entity.LawArticle;
import org.example.utils.ChapterExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class ESHelper {


    // 直接注入使用
    @Autowired
    private ElasticsearchOperations elasticsearchOperations;


    public void indexData() throws IOException {
        // read files
        List<String> strings = FileUtil.listFileNames(ConfigProperties.DATA_DIR);
        for (String fileName : strings) {
            String path = ConfigProperties.DATA_DIR + "/" + fileName;
            Map<String, List<String>> contentMap = ChapterExtractor.extractChapters(path);

            LinkedList<LawArticle> articles = new LinkedList<>();
            contentMap.forEach((k, v) -> {
                v.forEach((a) -> {
                    LawArticle lawArticle = new LawArticle();
                    lawArticle.setLawName(fileName);
                    lawArticle.setChapterName(k);
                    lawArticle.setArticleContent(a);
                    articles.add(lawArticle);
                });
            });

            List<IndexQuery> queries = articles.stream().map((a) -> {
                return new IndexQueryBuilder().withObject(a).build();
            }).toList();

            elasticsearchOperations.bulkIndex(queries, LawArticle.class);
        }
    }

}
