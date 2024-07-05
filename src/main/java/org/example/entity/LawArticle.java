package org.example.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "law_article")
@Data
@ToString
public class LawArticle {

    @Id
    private Integer id;

    private String lawName;

    private String chapterName;

    /**
     * which article and content
     */
    private String articleContent;


}
