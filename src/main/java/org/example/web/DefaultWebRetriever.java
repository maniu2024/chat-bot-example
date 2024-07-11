package org.example.web;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.web.domain.WebRetrieveResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Martin
 * @date 2024/7/10 下午4:06
 * @desciption: 默认的网络检索器
 */
@Slf4j
@Component
public class DefaultWebRetriever implements WebRetriever {

    @Value("${app.retriever.web.tavily.base_url}")
    private String baseUrl;

    @Value("${app.retriever.web.tavily.api_key}")
    private String apiKey;


    @SneakyThrows
    @Override
    public WebRetrieveResult retrieve(String query) {

        String bodyTemplate = STR."""
                {
                  "api_key": "\{apiKey}",
                  "query": "\{query}",
                  "search_depth": "advanced",
                  "include_answer": true,
                  "include_images": true,
                  "include_raw_content": false,
                  "max_results": 5,
                  "include_domains": [],
                  "exclude_domains": []
                }
                """;

        log.info("正在网络检索:{}", bodyTemplate);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(baseUrl))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(bodyTemplate))
                .build();


        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode != HttpStatus.OK.value()) {
            log.error("网络检索失败:{}-{}", statusCode, response.body());
            return null;
        }
        String body = response.body();
        WebRetrieveResult webRetrieveResult = JSON.parseObject(body, WebRetrieveResult.class);
        return webRetrieveResult;
    }
}
