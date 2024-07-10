package org.example.web;


import org.example.web.domain.WebRetrieveResult;

/**
 * @author Martin
 * @date 2024/7/10 下午4:00
 * @desciption: 网络检索，在本地知识库中检索不到相关数据后执行
 */
public interface WebRetriever {

    /**
     * 根据 query 从网络中检索
     * @param query 搜索查询字符串。
     * @return 检索结果
     */
    WebRetrieveResult retrieve(String query);


}
