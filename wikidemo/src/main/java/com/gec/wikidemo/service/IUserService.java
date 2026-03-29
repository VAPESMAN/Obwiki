package com.gec.wikidemo.service;

import com.gec.wikidemo.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gec.wikidemo.req.UserLoginReq;
import com.gec.wikidemo.req.UserQueryReq;
import com.gec.wikidemo.req.UserResetPasswordReq;
import com.gec.wikidemo.req.UserSaveReq;
import com.gec.wikidemo.resp.PageResp;
import com.gec.wikidemo.resp.UserLoginResp;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yq
 * @since 2025-12-12
 */
public interface IUserService extends IService<User> {

    PageResp<User> listByname(UserQueryReq req);

    void save(UserSaveReq req);

    void delete(Long id);

    void resetPassword(UserResetPasswordReq req);

    UserLoginResp login(UserLoginReq req);
}
