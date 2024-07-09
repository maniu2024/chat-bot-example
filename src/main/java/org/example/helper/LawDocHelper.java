package org.example.helper;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.DocChapter;
import org.example.domain.DocUnit;
import org.example.domain.EmbeddingResult;
import org.example.entity.LawDocUnit;
import org.example.knowledge.embd.IKnowledgeEmbedding;
import org.example.knowledge.store.IKnowledgeStore;
import org.example.utils.ChapterExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Slf4j
@Component
public class LawDocHelper {

    @Autowired
    private IKnowledgeEmbedding vectorEmbedding;

    @Autowired
    private IKnowledgeStore knowledgeStore;

    // get filePath under dir
    public List<String> getFilePaths(String dirPath) {
        List<String> fileNames = FileUtil.listFileNames(dirPath);
        List<String> paths = fileNames.stream().map((f) -> dirPath + File.separator + f).toList();
        return paths;
    }

    public void indexDataByFilePath(String path) {
        List<DocChapter> docChapters = ChapterExtractor.extractChapters(path);
        String fileName = path.substring(path.lastIndexOf(File.separator),path.lastIndexOf("."));
        log.info("building {}" , fileName);


        List<LawDocUnit> list = docChapters.stream().map((c) -> {
                    List<DocUnit> docUnitList = c.getDocUnitList();
                    return docUnitList.stream().map((u) -> {
                        LawDocUnit lawDocUnit = new LawDocUnit();
                        lawDocUnit.setDocName(fileName);
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

        if (CollectionUtil.isEmpty(list)) {
            return;
        }

        knowledgeStore.bulkStore(list, LawDocUnit.class);
        log.info("build {} success" , fileName);

    }

    public void indexDataByDir(String dirPath) throws IOException {
        List<String> filePaths = getFilePaths(dirPath);
        for (String path : filePaths) {
            indexDataByFilePath(path);
        }
    }
}
