package org.example.helper;

import org.example.config.ConfigProperties;
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
    public void indexDataByPath() throws IOException {
        lawDocHelper.indexDataByFilePath(ConfigProperties.DATA_DIR + "中华人民共和国宪法_2018修正版本.docx" );
    }

    @Test
    public void indexDataByDir() throws IOException {
        lawDocHelper.indexDataByDir(ConfigProperties.DATA_DIR);
    }
}