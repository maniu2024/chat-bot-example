package org.example.web.domain;

import lombok.Data;

import java.util.List;

/**
 * @author Martin
 * @date 2024/7/10 下午4:43
 * @desciption: 网络检索结果
 */
@Data
public class WebRetrieveResult {

    private String query;

    private List<RetrieveContent> results;


    @Data
    public static class RetrieveContent {

        private String title;

        private String url;

        private String content;

    }



}
