package org.example.helper;

import cn.hutool.core.io.FileUtil;
import org.example.config.ConfigProperties;
import org.example.domain.DocChapter;
import org.example.domain.DocUnit;
import org.example.entity.LawArticle;
import org.example.utils.ChapterExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
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
            List<DocChapter> docChapters = ChapterExtractor.extractChapters(path);

            List<IndexQuery> queries = docChapters.stream().map((c) -> {
                        List<DocUnit> docUnitList = c.getDocUnitList();
                        return docUnitList.stream().map((u) -> {
                            LawArticle lawArticle = new LawArticle();
                            lawArticle.setLawName(fileName.substring(0,fileName.indexOf(".")));
                            lawArticle.setUnitName(u.getUnitName());
                            lawArticle.setUnitContent(u.getContent());
                            lawArticle.setChapterName(c.getChapterName());
                            return lawArticle;
                        }).toList();
                    }).flatMap(Collection::stream)
                    .map((a) -> new IndexQueryBuilder().withObject(a).build())
                    .toList();

            elasticsearchOperations.bulkIndex(queries, LawArticle.class);
        }
    }

}
