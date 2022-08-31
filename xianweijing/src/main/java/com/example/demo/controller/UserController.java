package com.example.demo.controller;

import com.example.demo.config.AjaxResponse;
import com.example.demo.model.User;
import com.example.demo.service.MailService;
import com.example.demo.service.PicService;
import com.example.demo.service.RedisService;
import com.example.demo.service.UserService;
import com.example.demo.util.BeanUtil;
import com.example.demo.util.MD5Util;
import com.example.demo.vo.UserVoPost;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangduhuang
 * @date 2021/7/17 23:35
 */
@Slf4j //日志服务
@Controller
@Api(tags = "用户账号处理接口")
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
//    @Autowired
//    MailService mailService;
//    @Autowired
//    RedisService redisService;

    @ApiOperation(value = "注册账号", notes = "根据表单用户数据进行邮箱注册")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(paramType = "query", name = "password", value = "用户密码", required = true),
            @ApiImplicitParam(paramType = "query", name = "email", value = "用户邮箱", required = true),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "用户电话", required = true),
            @ApiImplicitParam(paramType = "query", name = "activeCode", value = "用户激活码", required = true),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "用户昵称", required = true),
            @ApiImplicitParam(paramType = "query", name = "unit", value = "用户单位", required = true),
            @ApiImplicitParam(paramType = "query", name = "alipay_account", value = "用户支付宝账号", required = true),
    })
    @ResponseBody
    @PostMapping("register")
    public AjaxResponse register(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("activeCode") int activeCode,
            @RequestParam("nickname") String nickname,
            @RequestParam("unit") String unit,
            @RequestParam("alipay_account") String alipay_account
    ) {
        String result;
        User user = new User(username, password, email, phone,nickname,unit,alipay_account);
        if (user == null) return AjaxResponse.falseResult("传入对象为空");
        boolean flag = userService.uniqueUser(username);
        if (flag) return AjaxResponse.falseResult("该账号已经注册");
        else {
//            result = userService.activeUser(username, activeCode);
              result = "ok"; //暂时关闭激活验证

            if ("ok".equals(result)) {
                user.setActive(0);
                user.setPassword(MD5Util.encrypByMD5(user.getPassword()));
                userService.addUser(user);
                return AjaxResponse.success();
            }
        }
        return AjaxResponse.failure(result);
    }

    @ApiOperation(value = "登录账号", notes = "根据账号密码进行登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(paramType = "query", name = "password", value = "用户密码", required = true)
    })
    @PostMapping("login")
    @ResponseBody
    public AjaxResponse login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        log.info("登录输入" + username + " " + password);
        User user = new User();
        user.setPassword(password);
        user.setUsername(username);
        if (user.getUsername() == null || user.getPassword() == null) return AjaxResponse.falseResult("传入参数为空");
        if (!userService.findActive(user.getUsername())) return AjaxResponse.falseResult("该账号未激活");
        String password1 = MD5Util.encrypByMD5(user.getPassword());
        boolean flag = userService.checkLogin(user.getUsername(), password1);
        if (flag) return AjaxResponse.success();
        else return AjaxResponse.falseResult("账号或密码错误");
    }

    @ApiOperation(value = "发送激活码", notes = "使用邮箱发送激活码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户账号名", required = true),
            @ApiImplicitParam(paramType = "query", name = "email", value = "用户邮箱", required = true)
    })
    @ResponseBody
    @PostMapping("activecode")
    public AjaxResponse mailCode(
            @RequestParam("username") String username,
            @RequestParam("email") String email
    ) {
// 关闭邮箱验证
//        Object flag = redisService.get(username);
//        if (flag != null) return AjaxResponse.falseResult("邮件已经发送，请稍后重试");
//        new Thread() {
//            public void run() {
//                log.info("开始发送邮件");
//                mailService.sendMail(email, username);
//            }
//
//        }.start();

        return AjaxResponse.success();


    }

    @ApiOperation(value = "获取用户资料", notes = "发送请求，获取用户基本信息")
    @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true)
    @ResponseBody
    @PostMapping("data")
    public AjaxResponse getData(
            @RequestParam("username") String username
    ) {
        User user = userService.getUser(username);
        if (user == null) return AjaxResponse.falseResult("查询不到用户信息");
        return AjaxResponse.success(BeanUtil.copy(user, UserVoPost.class));
    }

    @ApiOperation(value = "更新用户资料", notes = "发送用户信息，更新资料")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(paramType = "query", name = "email", value = "用户邮箱", required = true),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "用户电话", required = true),
            @ApiImplicitParam(paramType = "query", name = "nickname", value = "用户昵称", required =true),
            @ApiImplicitParam(paramType = "query", name = "unit", value = "用户单位", required = true),
            @ApiImplicitParam(paramType = "query", name = "alipay_account", value = "用户支付宝账号", required = true),
    })
    @ResponseBody
    @PostMapping("update")
    public AjaxResponse updateData(
            @RequestParam("username") String username,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) String alipay_account
    ) {
        User user = userService.getUser(username);
        if (user == null) return AjaxResponse.falseResult("该用户不存在");
        if (password!=null) user.setPassword(password);
        if (email != null) user.setEmail(email);
        if (phone != null) user.setPhone(phone);
        if (nickname != null) user.setNickname(nickname);
        if (unit!=null) user.setUnit(unit);
        if (alipay_account!=null) user.setAlipay_account(alipay_account);

        String password1 = MD5Util.encrypByMD5(user.getPassword());
        user.setPassword(password1);
        boolean flag=userService.addUser(user);
        if(flag) return AjaxResponse.success(BeanUtil.copy(user, UserVoPost.class));
        else return AjaxResponse.falseResult("更新失败");
    }




}
