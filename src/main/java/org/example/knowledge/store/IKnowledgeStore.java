package org.example.knowledge.store;


import org.example.domain.DocUnit;
import org.example.domain.SearchedDocUnitResult;

import java.util.List;

public interface IKnowledgeStore {


    /**
     * Dense Retriever
     * @param embedding
     * @param clazz
     * @return
     * @param <T>
     */
    public <T extends DocUnit> List<SearchedDocUnitResult<T>> retrieveByVector(List<Double> embedding, Class<T> clazz);


    /**
     * Sparse Retriever
     * @param text
     * @param clazz
     * @return
     * @param <T>
     */
    public <T extends DocUnit> List<SearchedDocUnitResult<T>> retrieveByMatch(String text, Class<T> clazz);


    /**
     * mix DR and SR
     * @param text
     * @param clazz
     * @return
     * @param <T>
     */
    public <T extends DocUnit> List<SearchedDocUnitResult<T>> retrieveByMix(String text,List<Double> embedding, Class<T> clazz);


}
