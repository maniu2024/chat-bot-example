package org.example.entity;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "law_doc_unit")
@Data
@ToString
public class LawDocUnit {

    @Id
    private String id;

    private String lawName;

    private String chapterName;

    private String unitName;

    /**
     * which article and content
     */
    private String unitContent;

    private List<Double> contentVector;


}
