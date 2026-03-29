package com.gec.wikidemo.service;

import com.gec.wikidemo.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yq
 * @since 2025-12-10
 */
public interface ICategoryService extends IService<Category> {

    List<Category> all();
}
