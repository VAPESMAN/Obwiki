package com.gec.wikidemo.controller;

import com.gec.wikidemo.req.UserLoginReq;
import com.alibaba.fastjson.JSONObject;
import com.gec.wikidemo.entity.User;
import com.gec.wikidemo.req.UserQueryReq;
import com.gec.wikidemo.req.UserResetPasswordReq;
import com.gec.wikidemo.req.UserSaveReq;
import com.gec.wikidemo.resp.CommonResp;
import com.gec.wikidemo.resp.PageResp;
import com.gec.wikidemo.resp.UserLoginResp;
import com.gec.wikidemo.service.IUserService;
import com.gec.wikidemo.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yq
 * @since 2025-12-12
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private IUserService userService;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private SnowFlake snowFlake;
    @Autowired
    private RedisTemplate redisTemplate;
    @GetMapping("/list")
    public CommonResp list(UserQueryReq req){
        CommonResp<PageResp<User>> resp = new CommonResp<>(true,"查询成 功",null);
        PageResp<User> pageResp = userService.listByname(req);
        resp.setContent(pageResp);
        return resp;
    }
//    @PostMapping("/save")
//    public CommonResp save(@RequestBody UserSaveReq req) {
//        CommonResp resp = new CommonResp<>(true,"成功",null);
//        userService.save(req);
//        return resp;
//    }
    @PostMapping("/save")
    public CommonResp save(@RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>(true,"成功",null);
        userService.save(req);
        return resp;
    }
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>(true,"删除成功",null);
        userService.delete(id);
        return resp;
    }
    @PostMapping("resetPassword")
    public CommonResp resetPassword(@RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp resp = new CommonResp<>(true,"成功",null);
        userService.resetPassword(req);
        return resp;
    }

    //登录
    @PostMapping("/login")
    public CommonResp login(@RequestBody UserLoginReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        CommonResp<UserLoginResp> resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.login(req);
        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token：{}，并放入redis中", token);
        userLoginResp.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(),
                JSONObject.toJSONString(userLoginResp), 3600 * 24, TimeUnit.SECONDS);
        resp.setContent(userLoginResp);
        return resp;
    }
    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token) {
        CommonResp resp = new CommonResp<>();
        redisTemplate.delete(token);
        LOG.info("从redis中删除token: {}", token);
        return resp;
    }


}
