package org.example.helper;

import cn.hutool.core.io.FileUtil;
import org.example.config.ConfigProperties;
import org.example.domain.DocChapter;
import org.example.domain.DocUnit;
import org.example.domain.EmbeddingResult;
import org.example.entity.LawDocUnit;
import org.example.knowledge.embd.IVectorEmbedding;
import org.example.knowledge.store.IKnowledgeStore;
import org.example.utils.ChapterExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
public class LawDocHelper {

    @Autowired
    private IVectorEmbedding vectorEmbedding;

    @Autowired
    private IKnowledgeStore knowledgeStore;

    public void indexData() throws IOException {
        // read files
        List<String> strings = FileUtil.listFileNames(ConfigProperties.DATA_DIR);
        for (String fileName : strings) {
            String path = ConfigProperties.DATA_DIR + "/" + fileName;
            List<DocChapter> docChapters = ChapterExtractor.extractChapters(path);

            List<LawDocUnit> list = docChapters.stream().map((c) -> {
                        List<DocUnit> docUnitList = c.getDocUnitList();
                        return docUnitList.stream().map((u) -> {
                            LawDocUnit lawDocUnit = new LawDocUnit();
                            lawDocUnit.setDocName(fileName.substring(0, fileName.indexOf(".")));
                            lawDocUnit.setUnitName(u.getUnitName());
                            lawDocUnit.setUnitContent(u.getUnitContent());
                            // embedding
                            EmbeddingResult embeddingResult = vectorEmbedding.embedding(u.getUnitContent());
                            lawDocUnit.setContentVector(embeddingResult.getEmbedding());
                            lawDocUnit.setChapterName(c.getChapterName());
                            return lawDocUnit;
                        }).toList();
                    }).flatMap(Collection::stream)
                    .toList();
            knowledgeStore.bulkStore(list,LawDocUnit.class);
        }
    }
}
