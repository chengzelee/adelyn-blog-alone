package cn.adelyn.blog.search.constant;

public interface EsBlogConstant {

    String INDEX_NAME = "blog_index";

    String USER_ID_FIELD = "userId";
    String TITLE_FIELD = "blogTitle";
    String CONTENT_FIELD = "blogContent";
    String VISIBLE_FIELD = "visible";

    String VISIBLE_PUBLIC = "public";
    String VISIBLE_PRIVATE = "private";

    String TITLE_ANALYZER = "ik_max_word";
    String CONTENT_ANALYZER = "ik_max_word";
}
