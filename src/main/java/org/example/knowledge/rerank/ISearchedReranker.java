package org.example.knowledge.rerank;

import org.example.domain.DocUnit;
import org.example.domain.SearchedDocUnitResult;

import java.util.List;


public interface ISearchedReranker {


    public <T extends DocUnit> List<SearchedDocUnitResult<T>> rerank(List<SearchedDocUnitResult<T>> list);

}
