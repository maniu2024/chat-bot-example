package org.example.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "law_doc_unit")
@Data
@ToString
public class LawArticle {

    @Id
    private Integer id;

    private String lawName;

    private String chapterName;

    private String unitName;

    /**
     * which article and content
     */
    private String unitContent;


}
