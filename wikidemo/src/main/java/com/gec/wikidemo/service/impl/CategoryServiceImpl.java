package com.gec.wikidemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gec.wikidemo.entity.Category;
import com.gec.wikidemo.mapper.CategoryMapper;
import com.gec.wikidemo.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yq
 * @since 2025-12-10
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Override
    public List<Category> all() {
        List<Category> categories = this.baseMapper.selectList(new QueryWrapper<Category>().orderByAsc("sort"));
        return list();
    }
}
