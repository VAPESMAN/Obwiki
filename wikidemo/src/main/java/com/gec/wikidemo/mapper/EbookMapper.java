package com.gec.wikidemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gec.wikidemo.entity.Ebook;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 电子书 Mapper 接口
 * </p>
 *
 * @author yq
 * @since 2025-11-26
 */
public interface EbookMapper extends BaseMapper<Ebook> {

    void increaseViewCount(@Param("id") Long ebookId);
}
