package org.example.entity;

import lombok.Data;
import lombok.ToString;
import org.example.domain.DocUnit;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.WriteOnlyProperty;

import java.util.List;

@Document(indexName = "law_doc_unit")
@Data
@ToString
public class LawDocUnit extends DocUnit {

    @WriteOnlyProperty
    private List<Double> contentVector;


}
