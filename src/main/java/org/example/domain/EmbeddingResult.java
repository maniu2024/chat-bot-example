package org.example.domain;

import lombok.Data;

import java.util.List;

@Data
public class EmbeddingResult {

    private String text;

    /**
     * embedding的处理结果，返回向量化表征的数组，数组长度为1024
     */
    private List<Double> embedding;

}
