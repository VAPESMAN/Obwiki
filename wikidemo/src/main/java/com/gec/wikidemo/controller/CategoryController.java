package com.gec.wikidemo.controller;

import com.gec.wikidemo.entity.Category;
import com.gec.wikidemo.resp.CommonResp;
import com.gec.wikidemo.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yq
 * @since 2025-12-10
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;
    @GetMapping("/all")
    public CommonResp all() {
        CommonResp<List<Category>> resp = new CommonResp<>(true,"查询成 功",null);
                List<Category> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }
}
