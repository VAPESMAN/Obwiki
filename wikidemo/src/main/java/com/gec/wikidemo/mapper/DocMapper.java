package com.gec.wikidemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gec.wikidemo.entity.Doc;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yq
 * @since 2025-12-17
 */
public interface DocMapper extends BaseMapper<Doc> {

    void increaseViewCount(Long id);

    void increaseVoteCount(Long id);
}
