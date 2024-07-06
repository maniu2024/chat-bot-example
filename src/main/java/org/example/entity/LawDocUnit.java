package org.example.entity;

import lombok.Data;
import lombok.ToString;
import org.example.domain.DocUnit;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Document(indexName = "law_doc_unit")
@Data
@ToString
public class LawDocUnit extends DocUnit {

    @Id
    private String id;

    private String lawName;

    private String chapterName;

    private List<Double> contentVector;


}
