package com.gec.wikidemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gec.wikidemo.entity.Content;
import com.gec.wikidemo.entity.Doc;
import com.gec.wikidemo.mapper.DocMapper;
import com.gec.wikidemo.mapper.EbookMapper;
import com.gec.wikidemo.req.DocQueryReq;
import com.gec.wikidemo.req.DocSaveReq;
import com.gec.wikidemo.resp.DocQueryResp;
import com.gec.wikidemo.resp.PageResp;
import com.gec.wikidemo.service.IContentService;
import com.gec.wikidemo.service.IDocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec.wikidemo.utils.CopyUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yq
 * @since 2025-12-17
 */
@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements IDocService {
    @Autowired
    IContentService contentService;
    @Autowired
    private EbookMapper ebookMapper;
    @Override
    public PageResp<DocQueryResp> listByname(DocQueryReq req) {
        QueryWrapper<Doc> queryWrapper = new QueryWrapper<Doc>();
        // 第一个参数：该参数是一个布尔类型，只有该参数是true时，才将like条件拼接到sql中；本
        // 例中，如果name字段不为空，则拼接name字段的like查询条件；
        queryWrapper.like(StringUtils.isNotBlank(req.getName()), "name", req.getName());
        // 创建分页对象
        Page<Doc> page = new Page<>(req.getPage(), req.getSize());
        page = this.baseMapper.selectPage(page, queryWrapper);
        List<DocQueryResp> resps = CopyUtil.copyList(page.getRecords(), DocQueryResp.class);
        // 创建返回对象
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setList(resps);
        pageResp.setTotal(page.getTotal());
        return pageResp;
    }

    @Override
    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增文档
            // 删除了 long id = snowFlake.nextId(); 和 doc.setId(id);
            doc.setViewCount(0);//默认查看点赞为0
            doc.setVoteCount(0);
            this.baseMapper.insert(doc);
            // 新增内容
            // 数据库插入 doc 后会自动回填自增 id 到 doc 对象，直接取用即可
            content.setId(doc.getId());
            contentService.save(content);
        } else {
            // 更新文档
            this.baseMapper.updateById(doc);
            boolean b = contentService.updateById(content);//更新内容
            if(!b){//根据id更新失败,执行新增功能
                contentService.save(content);
            }
        }
    }

    @Override
    public void delete(Long id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        this.baseMapper.deleteBatchIds(ids);
    }

    @Override
    public List<DocQueryResp> allbyEbookId(Long ebookId) {
//该电子书阅读数+1
        ebookMapper.increaseViewCount(ebookId);
        QueryWrapper<Doc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("ebook_id",ebookId).orderByAsc("sort");
        List<Doc> doclist = this.baseMapper.selectList(queryWrapper);
// 列表复制
        List<DocQueryResp> list = CopyUtil.copyList(doclist,
                DocQueryResp.class);
        return list;
    }

    @Override
    public void vote(Long id) {
        this.baseMapper.increaseVoteCount(id);
    }

    @Override
    public List<DocQueryResp> all() {
        List<Doc> categories = this.baseMapper.selectList(new QueryWrapper<Doc>().orderByAsc("sort"));
        List<DocQueryResp> list = CopyUtil.copyList(categories, DocQueryResp.class);
        return list;
    }


}