package com.gec.wikidemo.service.impl;

import com.gec.wikidemo.entity.Content;
import com.gec.wikidemo.mapper.ContentMapper;
import com.gec.wikidemo.mapper.DocMapper;
import com.gec.wikidemo.service.IContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yq
 * @since 2025-12-17
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements IContentService {
    @Autowired
    DocMapper docMapper;
    @Override
    public String findContent(Long id) {
        Content content = this.baseMapper.selectById(id);
        docMapper.increaseViewCount(id);
        if(content !=null){
            return content.getContent();
        }
        return null;
    }
}
