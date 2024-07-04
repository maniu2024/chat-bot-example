package org.example.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {


    private String sku;

    private String name;

    private double price;


}
