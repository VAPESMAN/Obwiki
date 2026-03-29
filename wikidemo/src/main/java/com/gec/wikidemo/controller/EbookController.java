package com.gec.wikidemo.controller;

import com.gec.wikidemo.entity.Ebook;
import com.gec.wikidemo.mapper.EbookMapper;
import com.gec.wikidemo.req.EbookQueryReq;
import com.gec.wikidemo.resp.CommonResp;
import com.gec.wikidemo.resp.PageResp;
import com.gec.wikidemo.service.IEbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 电子书 前端控制器
 * </p>
 *
 * @author yq
 * @since 2025-11-26
 */

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private IEbookService ebookService;
    @GetMapping("/list")
    public CommonResp list(EbookQueryReq req) {
        CommonResp resp = new CommonResp<>(true,"查询成功",null);
        List<Ebook> list = ebookService.listByname(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/listByPage")
    public CommonResp listByPage(EbookQueryReq req) {
        CommonResp<PageResp<Ebook>> resp = new CommonResp<>(true,"查询成 功",null);
        PageResp<Ebook> pageResp = ebookService.listByPage(req);
        resp.setContent(pageResp);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody Ebook req) {
        CommonResp resp = new CommonResp<>(true,"成功",null);
        ebookService.saveBook(req);
        return resp;
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        ebookService.delete(id);
        return resp;
    }

}
