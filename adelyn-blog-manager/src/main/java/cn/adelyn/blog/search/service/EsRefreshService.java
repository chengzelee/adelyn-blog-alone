package cn.adelyn.blog.search.service;

import cn.adelyn.blog.search.constant.EsBlogConstant;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EsRefreshService {


    private final ElasticsearchClient elasticsearchClient;

    private final String indexName = "blog_index";
    private final String aliasIndexName = "blog_alias_index";

    public void recreateIndex() {
        deleteIndex();
        createIndex();
    }

    private void deleteIndex() {
        List<String> indexList = Collections.singletonList(indexName);
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(indexList).build();

        try {
            DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(deleteIndexRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createIndex() {
        Map<String, Property> propertyMap = new HashMap<>();
        // .index 字段是否可以被查询
        propertyMap.put(EsBlogConstant.TITLE_FIELD,new Property(new TextProperty.Builder().index(true).analyzer("ik_max_word").build()));
        propertyMap.put(EsBlogConstant.CONTENT_FIELD,new Property(new TextProperty.Builder().index(true).analyzer("ik_max_word").build()));
//        propertyMap.put("tenantId", new Property(new KeywordProperty.Builder().build()));
        propertyMap.put(EsBlogConstant.USER_ID_FIELD, new Property(new KeywordProperty.Builder().build()));
        propertyMap.put(EsBlogConstant.VISIBLE_FIELD, new Property(new KeywordProperty.Builder().build()));

        TypeMapping typeMapping = new TypeMapping.Builder().properties(propertyMap).build();
        IndexSettings indexSettings = new IndexSettings.Builder().numberOfShards(String.valueOf(1)).numberOfReplicas(String.valueOf(0)).build();
        CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder()
                .index(indexName)
                .aliases(aliasIndexName, new Alias.Builder().isWriteIndex(true).build())
                .mappings(typeMapping)
                .settings(indexSettings)
                .build();

        try {
            CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(createIndexRequest);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
