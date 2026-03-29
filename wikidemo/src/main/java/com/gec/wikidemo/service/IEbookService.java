package com.gec.wikidemo.service;

import com.gec.wikidemo.entity.Ebook;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gec.wikidemo.req.EbookQueryReq;
import com.gec.wikidemo.resp.PageResp;

import java.util.List;

/**
 * <p>
 * 电子书 服务类
 * </p>
 *
 * @author yq
 * @since 2025-11-26
 */
public interface IEbookService extends IService<Ebook> {

    List<Ebook> listByname(EbookQueryReq req);

    PageResp<Ebook> listByPage(EbookQueryReq req);

    void saveBook(Ebook req);

    void delete(Long id);
}
