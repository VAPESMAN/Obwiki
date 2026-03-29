package com.gec.wikidemo.controller;

import com.gec.wikidemo.req.DocQueryReq;
import com.gec.wikidemo.req.DocSaveReq;
import com.gec.wikidemo.resp.CommonResp;
import com.gec.wikidemo.resp.DocQueryResp;
import com.gec.wikidemo.resp.PageResp;
import com.gec.wikidemo.service.IDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yq
 * @since 2025-12-17
 */
@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private IDocService docService;
    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>(true,"查询成功",null);
                List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }
    @GetMapping("/list")
    public CommonResp list(DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>(true,"查询成功",null);
                PageResp<DocQueryResp> pageResp = docService.listByname(req);
        resp.setContent(pageResp);
        return resp;
    }
    @PostMapping("/save")
    public CommonResp save(@RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>(true,"成功",null);
        docService.save(req);
        return resp;
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>(true,"删除成功",null);
        docService.delete(id);
        return resp;
    }
    @GetMapping("/remove")
    public CommonResp delete(@RequestParam("ids") List<Long> ids) {
        CommonResp resp = new CommonResp<>(true,"删除成功",null);
        docService.delete(ids);
        return resp;
    }
    @GetMapping("/all/{ebookId}")
    public CommonResp allByEbookId(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.allbyEbookId(ebookId);
        resp.setContent(list);
        return resp;
    }
    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        CommonResp commonResp = new CommonResp();
        docService.vote(id);
        return commonResp;
    }
}