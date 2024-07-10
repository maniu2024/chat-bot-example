package org.example.web;

import com.alibaba.fastjson.JSON;
import org.example.web.domain.WebRetrieveResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author Martin
 * @date 2024/7/10 下午4:16
 * @desciption:
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DefaultWebRetrieverTest {

    @Autowired
    private DefaultWebRetriever defaultWebRetriever;

    @Test
    public void retrieve() {
        WebRetrieveResult result = defaultWebRetriever.retrieve("入室盗窃怎么判罚？");
        System.out.println("result = " + JSON.toJSONString(result));
    }
}