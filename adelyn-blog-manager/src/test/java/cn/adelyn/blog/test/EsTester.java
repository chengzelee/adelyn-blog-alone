package cn.adelyn.blog.test;

import cn.adelyn.blog.search.constant.EsBlogConstant;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.mapping.KeywordProperty;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.mapping.TextProperty;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class EsTester {

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class BlogModel extends HashMap {
    }

    private static ElasticsearchClient elasticsearchClient;

    private String indexName = "blog_index";
    private String aliasIndexName = "blog_alias_index";

    private Long userId = 1798703760764178432L;
    private Long blogId = 1619995941174120400L;

    @BeforeAll
    static void init() {
        // Create the low-level client
        RestClient restClient = RestClient.builder(
                        new HttpHost("192.168.31.191", 9200, "http"))
                .build();

        // Create the transport with a Jackson mapper
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        // And create the API client
        ElasticsearchClient client = new ElasticsearchClient(transport);

        elasticsearchClient = client;
    }

    @Test
    public void createIndex() throws IOException {

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

        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(createIndexRequest);
        log.info(String.valueOf(createIndexResponse.acknowledged()));
    }

    @Test
    public void recreateIndex() throws IOException {
        List<String> indexList = Collections.singletonList(indexName);
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest.Builder().index(indexList).build();
        DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(deleteIndexRequest);
        log.info(String.valueOf(deleteIndexResponse.acknowledged()));
        createIndex();
        insertObj();
    }

    @Test
    public void insertObj() throws IOException{
        BlogModel blogModel = new BlogModel();
        blogModel.put(EsBlogConstant.CONTENT_FIELD,
                """
                放一个小段落 中文段落 测试段落
                When the data you want to index comes from external sources, having to create domain objects may be cumbersome or outright impossible with semi-structured data.
                                
                You can index data from an arbitrary source using withJson(). Using this method will read the source and use it for the index request’s document property. See Creating API objects from JSON data for additional details.
                                
                Elasticsearch Java API Client客户端中的 中文 模糊查询，使用fuzzy而不是like，其中field代表需要判断的字段名称，value代表需要模糊查询的关键词，fuzziness代表可以与关键词有误差的字数，可选值为0、1、2这三项。
                与批量添加文档类似，首先需要创建ES客户端，同样使用BulkOperation集合来存储批量操作的内容，不同的是这次使中文用BulkOperationBuilder的delete方法构建批量删除操作，最后调用ES客户端的bulk方法执行，获取的响应结果同样为BulkResponse类型。
                ————————————————
                版权声明：本文为CSDN博主「无枫丶」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
                原文链接：https://blog.csdn.net/anjiongyi/article/details/123391835
                """);
        blogModel.put(EsBlogConstant.TITLE_FIELD,"中文测试标题test 中文 中文");
        blogModel.put(EsBlogConstant.USER_ID_FIELD, userId);
        blogModel.put(EsBlogConstant.VISIBLE_FIELD, EsBlogConstant.VISIBLE_PUBLIC);
        IndexResponse response = elasticsearchClient.index(i -> i
                .index(indexName)
                .id(blogId.toString())
                .document(blogModel)
        );
        log.info("uniqId is {}, version is {}", response.id(), response.version());
    }

    @Test
    void updateObj() throws IOException {
        BlogModel blogModel = new BlogModel();
        blogModel.put(EsBlogConstant.CONTENT_FIELD,
                """
                更新成功
                """);
        blogModel.put(EsBlogConstant.TITLE_FIELD,"中文测试标题test 中文 中文");
        blogModel.put(EsBlogConstant.USER_ID_FIELD, userId.toString());
        blogModel.put(EsBlogConstant.VISIBLE_FIELD, EsBlogConstant.VISIBLE_PRIVATE);

        UpdateResponse response = elasticsearchClient.update(
                updateRequest -> updateRequest
                        .index(indexName)
                        .id(blogId.toString())
                        .doc(blogModel)
                , BlogModel.class
        );

        log.info("uniqId is {}, version is {}", response.id(), response.version());
    }

    @Test
    public void delObjById() throws IOException {
        DeleteResponse response = elasticsearchClient.delete(d -> d
                .index(indexName)
                .id(blogId.toString())
        );
    }

    @Test
    public void getObjById() throws IOException{
        GetResponse<BlogModel> response = elasticsearchClient.get(g -> g
                        .index(indexName)
                        .id(blogId.toString()),
                BlogModel.class
        );
        if (response.found()) {
            BlogModel blogModel = response.source();
            log.info("the result is {}",blogModel);
        } else {
            log.info ("result not found");
        }
    }

    @Test
    public void search() throws IOException {
        Map<String, HighlightField> highlightFieldMap = new HashMap<>();
        HighlightField highlightField = new HighlightField.Builder()
                .preTags("<span style='color:#EA4335'>")
                .postTags("</span>")
                .build();
        highlightFieldMap.put(EsBlogConstant.TITLE_FIELD,highlightField);
        highlightFieldMap.put(EsBlogConstant.CONTENT_FIELD,highlightField);

        Query queryTitel = new Query.Builder().match(m -> m
                .field(EsBlogConstant.TITLE_FIELD)
                .query("测试")
                .analyzer(EsBlogConstant.CONTENT_ANALYZER)
        ).build();
        Query queryContent = new Query.Builder().match(m -> m
                .field(EsBlogConstant.CONTENT_FIELD)
                .query("测试")
                .analyzer(EsBlogConstant.CONTENT_ANALYZER)
        ).build();
/*        Query queryUserId = new Query.Builder().term(t -> t
                .field("authorId")
                .value("12312312")
        ).build();*/
        Query queryVisible = new Query.Builder().term(t -> t
                .field(EsBlogConstant.VISIBLE_FIELD)
                .value(EsBlogConstant.VISIBLE_PUBLIC)
        ).build();
        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(EsBlogConstant.INDEX_NAME)
                .query(q -> q
                        .bool(b -> b
                                .should(queryTitel,queryContent)
                                .filter(queryVisible)
                                // 解决 should 和 filter 组合使用, should 失效的问题
                                // https://blog.csdn.net/zhengchanmin/article/details/113723890
                                .minimumShouldMatch("1")
                        )
                )
                // https://www.elastic.co/guide/en/elasticsearch/reference/8.3/highlighting.html
                .highlight(h -> h
                        .fields(highlightFieldMap)
                )
                // 从第几条开始查
                .from(0)
                // 查多少条
                .size(5)
/*                .sort(sort -> sort
                        .field(f -> f
                                .field("id")
                                .order(SortOrder.Desc)
                        )
                )*/
                .build();
        SearchResponse<Object> personSearchResponse = elasticsearchClient.search(searchRequest, Object.class);
        List<Hit<Object>> hits = personSearchResponse.hits().hits();
        hits.forEach(hit ->{
 /*           BlogModel blogModel = hit.source();
            log.info("title: {}, content: {}",blogModel.getTitle(), blogModel.getContent());*/
            log.info("highlighted fild is : {}",hit.highlight().get("content"));
        });
    }
}
