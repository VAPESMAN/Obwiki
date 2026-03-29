package com.gec.wikidemo.service;

import com.gec.wikidemo.entity.Doc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gec.wikidemo.req.DocQueryReq;
import com.gec.wikidemo.req.DocSaveReq;
import com.gec.wikidemo.resp.DocQueryResp;
import com.gec.wikidemo.resp.PageResp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yq
 * @since 2025-12-17
 */
public interface IDocService extends IService<Doc> {

    List<DocQueryResp> all();

    PageResp<DocQueryResp> listByname(DocQueryReq req);

    void save(DocSaveReq req);

    void delete(Long id);
    
    void delete(List<Long> ids);

    List<DocQueryResp> allbyEbookId(Long ebookId);

    void vote(Long id);
}