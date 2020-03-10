package com.yangqi.recommendedsystem.request;

/**
 * @author xiaoer
 * @date 2020/2/23 19:15
 */
public class PageQuery {
    /**
     * 查询的页码
     */
    private Integer page = 1;

    /**
     * 每页显示的条数
     */
    private Integer size = 20;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
