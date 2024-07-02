package cn.adelyn.blog.search.service;

import cn.adelyn.blog.manager.pojo.bo.InsertBlogBO;
import cn.adelyn.blog.search.constant.EsBlogConstant;
import cn.adelyn.blog.search.pojo.bo.SearchBO;
import cn.adelyn.blog.search.pojo.dto.SearchBlogDTO;
import cn.adelyn.blog.search.pojo.vo.SearchBlogVO;
import cn.adelyn.framework.core.execption.AdelynException;
import cn.adelyn.framework.core.pojo.vo.PageVO;
import cn.adelyn.framework.core.response.ResponseEnum;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.Result;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class BlogSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public boolean insertBlog(InsertBlogBO insertBlogBO) {
        Map<String, String> blogMap = new HashMap<>();
        blogMap.put(EsBlogConstant.TITLE_FIELD, insertBlogBO.getBlogTitle());
        blogMap.put(EsBlogConstant.CONTENT_FIELD, insertBlogBO.getBlogContent());
        blogMap.put(EsBlogConstant.USER_ID_FIELD, insertBlogBO.getUserId().toString());
        blogMap.put(EsBlogConstant.VISIBLE_FIELD, insertBlogBO.getBlogVisible());

        IndexResponse indexResponse;
        try {
            indexResponse = elasticsearchClient.index(i -> i
                    .index(EsBlogConstant.INDEX_NAME)
                    .id(insertBlogBO.getBlogId().toString())
                    .document(blogMap)
            );
        } catch (IOException e) {
            throw new AdelynException("insert blog err", e.getMessage());
        }

        return Objects.equals(indexResponse.result().jsonValue(), Result.Created.jsonValue());
    }

    public boolean updateBlog(InsertBlogBO insertBlogBO) {
        Map<String, String> blogMap = new HashMap<>();
        blogMap.put(EsBlogConstant.TITLE_FIELD, insertBlogBO.getBlogTitle());
        blogMap.put(EsBlogConstant.CONTENT_FIELD, insertBlogBO.getBlogContent());
        blogMap.put(EsBlogConstant.USER_ID_FIELD, insertBlogBO.getUserId().toString());
        blogMap.put(EsBlogConstant.VISIBLE_FIELD, insertBlogBO.getBlogVisible());

        UpdateResponse updateResponse;
        try {
            updateResponse = elasticsearchClient.update(updateRequest -> updateRequest
                            .index(EsBlogConstant.INDEX_NAME)
                            .id(insertBlogBO.getBlogId().toString())
                            .doc(blogMap)
                    ,Map.class
            );
        } catch (IOException e) {
            throw new AdelynException("update blog err", e.getMessage());
        }

        return Objects.equals(updateResponse.result().jsonValue(), Result.Updated.jsonValue());
    }

    public boolean deleteBlog(Long blogId) {
        DeleteResponse response;

        try {
            response = elasticsearchClient.delete(d -> d
                    .index(EsBlogConstant.INDEX_NAME)
                    .id(Long.toString(blogId))
            );
        } catch (IOException e) {
            throw new AdelynException("delete blog err", e.getMessage());
        }

        return Objects.equals(response.result().jsonValue(), Result.Deleted.jsonValue());
    }

    public PageVO<SearchBlogVO> matchPublic(SearchBlogDTO searchBlogDTO) {

        List<Query> shouldQueryList = genBaseQueryList(searchBlogDTO.getQueryStr());
        List<Query> filterQueryList = genPublicQueryList();

        SearchBO<Hit<Object>> searchBO = match(searchBlogDTO.getFrom(), searchBlogDTO.getSize(), shouldQueryList, filterQueryList);

        List<SearchBlogVO> searchBlogVOList = parseHits(searchBO.getList());

        PageVO<SearchBlogVO> pageVO = new PageVO<>();
        pageVO.setList(searchBlogVOList);
        pageVO.setTotal(searchBO.getTotal());

        return pageVO;
    }

    public PageVO<SearchBlogVO> matchUser(SearchBlogDTO searchBlogDTO, Long userId) {

        List<Query> shouldQueryList = genBaseQueryList(searchBlogDTO.getQueryStr());
        List<Query> filterQueryList = genPrivateQueryList(userId);

        SearchBO<Hit<Object>> searchBO = match(searchBlogDTO.getFrom(), searchBlogDTO.getSize(), shouldQueryList, filterQueryList);

        List<SearchBlogVO> searchBlogVOList = parseHits(searchBO.getList());

        PageVO<SearchBlogVO> pageVO = new PageVO<>();
        pageVO.setList(searchBlogVOList);
        pageVO.setTotal(searchBO.getTotal());

        return pageVO;
    }

    public SearchBO<Hit<Object>> match(Integer pageFrom, Integer pageSize, List<Query> shouldQueryList, List<Query> filterQueryList) {

        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(EsBlogConstant.INDEX_NAME)
                .query(q -> q
                        .bool(b -> b
                                .filter(filterQueryList)
                                .should(shouldQueryList)
                                // 解决 should 和 filter 组合使用, should 失效的问题
                                // https://blog.csdn.net/zhengchanmin/article/details/113723890
                                .minimumShouldMatch("1")
                        )
                )
                // 从第几条开始查
                .from(pageFrom)
                // 查多少条
                .size(pageSize)
                // https://www.elastic.co/guide/en/elasticsearch/reference/8.3/highlighting.html
                .highlight(h -> h
                        .fields(genHighlightFieldMap())
                )
                // 只返回指定字段
                .source(src -> src
                        .filter(f -> f
                                .includes(EsBlogConstant.TITLE_FIELD, EsBlogConstant.USER_ID_FIELD, EsBlogConstant.VISIBLE_FIELD)
                        )
                )
/*                .sort(sort -> sort
                        .field(f -> f
                                .field(searchBlogDTO.getOrderField())
                                .order(SortOrder.Desc)
                        )
                )*/

                .build();

        SearchResponse<Object> searchResponse;

        try {
            searchResponse = elasticsearchClient.search(searchRequest, Object.class);
        } catch (IOException e) {
            throw new AdelynException("search err", e);
        }

        List<Hit<Object>> hits = searchResponse.hits().hits();

        SearchBO<Hit<Object>> searchBO = new SearchBO<>();
        searchBO.setList(hits);
        searchBO.setTotal(searchResponse.hits().total().value());
        return searchBO;
    }

    public SearchBlogVO getBlogById(String blogId, Long userId) {

        GetResponse<SearchBlogVO> response;

        try {
            response = elasticsearchClient.get(g -> g.index(EsBlogConstant.INDEX_NAME).id(blogId), SearchBlogVO.class);
        } catch (IOException e) {
            throw new AdelynException("search err", e);
        }

        if (response.found()) {
            SearchBlogVO searchBlogVO = response.source();
            searchBlogVO.setBlogId(blogId);

            // 鉴权
            if (EsBlogConstant.VISIBLE_PRIVATE.equals(searchBlogVO.getVisible())) {
                if (!searchBlogVO.getUserId().equals(Long.toString(userId))) {
                    throw new AdelynException(ResponseEnum.UNAUTHORIZED);
                }
            }

            return searchBlogVO;
        } else {
            throw new AdelynException("blog not exist");
        }
    }

    private List<Query> genPublicQueryList() {
        List<Query> publicQueryList = new ArrayList<>();

        Query queryVisible = new Query.Builder().term(t -> t
                .field(EsBlogConstant.VISIBLE_FIELD)
                .value(EsBlogConstant.VISIBLE_PUBLIC)
        ).build();

        publicQueryList.add(queryVisible);

        return publicQueryList;
    }

    private List<Query> genPrivateQueryList(Long userId) {
        List<Query> privateQueryList = new ArrayList<>();

        Query queryUserId = new Query.Builder().term(t -> t
                .field(EsBlogConstant.USER_ID_FIELD)
                .value(userId)
        ).build();

        privateQueryList.add(queryUserId);

        return privateQueryList;
    }

    private List<Query> genBaseQueryList(String queryStr) {
        List<Query> publicQueryList = new ArrayList<>();

        Query queryTitle = new Query.Builder().match(m -> m
                .field(EsBlogConstant.TITLE_FIELD)
                .query(queryStr)
                .analyzer(EsBlogConstant.TITLE_ANALYZER)
        ).build();
        Query queryContent = new Query.Builder().match(m -> m
                .field(EsBlogConstant.CONTENT_FIELD)
                .query(queryStr)
                .analyzer(EsBlogConstant.CONTENT_ANALYZER)
        ).build();

        publicQueryList.add(queryTitle);
        publicQueryList.add(queryContent);

        return publicQueryList;
    }

    private Map<String, HighlightField> genHighlightFieldMap() {
        Map<String, HighlightField> highlightFieldMap = new HashMap<>();
        HighlightField highlightField = new HighlightField.Builder()
                .preTags("<span style='color:#EA4335'>")
                .postTags("</span>")
                .build();
        // 注意顺序同 query 一致
        highlightFieldMap.put(EsBlogConstant.TITLE_FIELD,highlightField);
        highlightFieldMap.put(EsBlogConstant.CONTENT_FIELD,highlightField);

        return highlightFieldMap;
    }

    private List<SearchBlogVO> parseHits(List<Hit<Object>> hits) {
        List<SearchBlogVO> list = new ArrayList<>();
        hits.forEach(hit ->{
            LinkedHashMap linkedHashMap = (LinkedHashMap) hit.source();
            SearchBlogVO searchBlogVO = new SearchBlogVO();
            searchBlogVO.setBlogId(hit.id());
            searchBlogVO.setUserId(linkedHashMap.get(EsBlogConstant.USER_ID_FIELD).toString());
            searchBlogVO.setBlogTitle((String) linkedHashMap.get(EsBlogConstant.TITLE_FIELD));
            searchBlogVO.setVisible((String) linkedHashMap.get(EsBlogConstant.VISIBLE_FIELD));

            List<String> titleList = hit.highlight().get(EsBlogConstant.TITLE_FIELD);
            List<String> contentList = hit.highlight().get(EsBlogConstant.CONTENT_FIELD);

            if (Objects.nonNull(titleList) && !titleList.isEmpty()){
                StringBuilder highLightTitle = new StringBuilder();
                titleList.forEach(s -> highLightTitle.append(s).append("......"));
                searchBlogVO.setBlogTitle(highLightTitle.toString());
            }
            if (Objects.nonNull(contentList) && !contentList.isEmpty()){
                StringBuilder highLightContent = new StringBuilder();
                contentList.forEach(s -> highLightContent.append(s).append("......"));
                searchBlogVO.setBlogContent(highLightContent.toString());
            }

            list.add(searchBlogVO);
        });

        return list;
    }

}
