package org.example.domain;

import lombok.Data;

import java.util.List;

@Data
public class DocChapter {

    private String docType;

    private String chapterName;

    private List<DocUnit> docUnitList;

}
