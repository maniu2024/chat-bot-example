package org.example.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ESHelperTest {


    @Autowired
    private ESHelper esHelper;

    @Test
    public void indexData() throws IOException {
        esHelper.indexData();
    }
}