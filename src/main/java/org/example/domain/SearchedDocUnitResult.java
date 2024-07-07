package org.example.domain;

import lombok.Builder;
import lombok.Data;
import org.example.enums.RetrievalType;

import java.util.List;

@Data
public class SearchedDocUnitResult<T> {

    private RetrievalType retrievalType;

    private double score;

    private DocUnit docUnit;

}
