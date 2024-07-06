package org.example.vector.store;


public interface IVectorStore {


    String retrieval(String collectionName,Double[] vector);


}
