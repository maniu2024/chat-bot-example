package org.example.domain;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;

@Data
public class DocUnit {

    private String id;

    @Field(name = "lawName")
    private String docName;

    private String chapterName;

    private String unitName;

    private String unitContent;

}
