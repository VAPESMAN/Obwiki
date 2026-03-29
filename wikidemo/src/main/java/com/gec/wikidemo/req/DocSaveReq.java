package com.gec.wikidemo.req;

import lombok.Data;

@Data
public class DocSaveReq {
    private Long id;
    //电子书id
    private Long ebookId;
    //父id
    private Long parent;
    //名称
    private String name;
    //顺序
    private Integer sort;
    //阅读数
    private Integer viewCount;
    //点赞数
    private Integer voteCount;
    private String content;
}