package com.gec.wikidemo.service;

import com.gec.wikidemo.entity.Content;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yq
 * @since 2025-12-17
 */
public interface IContentService extends IService<Content> {

    String findContent(Long id);
}
