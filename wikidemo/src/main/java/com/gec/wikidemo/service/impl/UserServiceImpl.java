package com.gec.wikidemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gec.wikidemo.entity.User;
import com.gec.wikidemo.exception.BusinessException;
import com.gec.wikidemo.exception.BusinessExceptionCode;
import com.gec.wikidemo.mapper.UserMapper;
import com.gec.wikidemo.req.UserLoginReq;
import com.gec.wikidemo.req.UserQueryReq;
import com.gec.wikidemo.req.UserResetPasswordReq;
import com.gec.wikidemo.req.UserSaveReq;
import com.gec.wikidemo.resp.PageResp;
import com.gec.wikidemo.resp.UserLoginResp;
import com.gec.wikidemo.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec.wikidemo.utils.CopyUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yq
 * @since 2025-12-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);
    @Override
    public PageResp<User> listByname(UserQueryReq req)  {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        // 第一个参数：该参数是一个布尔类型，只有该参数是true时，才将like条件拼接到sql中；
        // 本例中，如果name字段不为空，则拼接name字段的like查询条件；
        queryWrapper.like(StringUtils.isNotBlank(req.getName()), "name", req.getName());
        queryWrapper.or().like(StringUtils.isNotBlank(req.getLoginName()), "login_name", req.getLoginName());

        // 创建分页对象
        Page<User> page = new Page<>(req.getPage(), req.getSize());
        page = this.baseMapper.selectPage(page, queryWrapper);

        List<User> resps = CopyUtil.copyList(page.getRecords(), User.class);

        // 创建返回对象
        PageResp<User> pageResp = new PageResp<>();
        pageResp.setList(resps);
        pageResp.setTotal(page.getTotal());
        return pageResp;
    }
    @Override
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 判断用户名是否已经存在
            User one = this.baseMapper.selectOne(new QueryWrapper<User>().eq("login_name", req.getLoginName()));
            if (ObjectUtils.isEmpty(one)) {
                // 新增
                this.baseMapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            // 更新
            user.setLoginName(null); // 避免绕过前端 修改登录名
            this.baseMapper.updateById(user);
        }
    }
    @Override
    public void delete(Long id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        this.baseMapper.updateById(user);
    }

    @Override
    public UserLoginResp login(UserLoginReq req) {
        //1.根据用户名查询用户信息
        User user = this.baseMapper.selectOne(new QueryWrapper<User>
                ().eq("login_name", req.getLoginName()));
        if (ObjectUtils.isEmpty(user)) {
            // 用户名不存在
            LOG.info("用户名不存在, {}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (user.getPassword().equals(req.getPassword())) {
            // 登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(user,
                        UserLoginResp.class);
                return userLoginResp;
            } else {
                // 密码不对
                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(),
                        user.getPassword());
                throw new
                        BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }
}

