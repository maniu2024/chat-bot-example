package org.example.helper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LawDocHelperTest {


    @Autowired
    private LawDocHelper lawDocHelper;

    @Test
    public void indexData() throws IOException {
        lawDocHelper.indexData();
    }
}