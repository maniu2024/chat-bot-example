package org.example.vector.store;


import org.example.domain.DocUnit;

import java.util.List;

public interface IVectorStore {


    List<DocUnit> retrieval(String collectionName, List<Double> embedding);


}
