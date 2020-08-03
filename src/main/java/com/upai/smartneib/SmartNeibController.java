package com.upai.smartneib;

import com.upai.smartneib.update.Apk;
import com.upai.smartneib.update.ApkResult;
import com.upai.smartneib.update.UpdateRepository;
import com.upai.smartneib.user.Result;
import com.upai.smartneib.user.User;
import com.upai.smartneib.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/smart-neib")
public class SmartNeibController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UpdateRepository updateRepository;

    /**
     * 用户注册
     *
     * @param username 用户名
     * @param password 密码
     * @param email    邮箱
     * @return 返回Result结果
     */
    @PostMapping(path = "/register")
    public @ResponseBody
    Result register(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        String result;
        User user = new User();
        List<User> userList = userRepository.findByUsername(username);
        if (userList.size() == 1) {
            result = "用户名重复";
        } else {
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            userRepository.save(user);
            result = "注册成功";
        }
        return new Result(username, result);
    }

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回登录结果
     */
    @PostMapping(path = "/login")
    public @ResponseBody
    Result login(@RequestParam String username, @RequestParam String password) {
        String result;
        List<User> userList = userRepository.findByUsername(username);
        if (userList.size() == 1) {
            if (userList.get(0).getPassword().equals(password)) {
                result = "登录成功!";
            } else {
                result = "密码不正确!";
            }
        } else {
            result = "用户名不存在!";
        }
        return new Result(username, result);
    }

    /**
     * 更新APK
     *
     * @param currentVersion 当前版本号
     * @return 返回结果
     */
    @PostMapping(path = "/update")
    public @ResponseBody
    ApkResult update(@RequestParam String currentVersion) {
        List<Apk> updateApkList = updateRepository.findAll();
        String newVersion = currentVersion;
        String url = "";
        String result = "";
        if (updateApkList.size() == 1) {
            Apk apk = updateApkList.get(0);
            if (Float.parseFloat(apk.getVersion()) > Float.parseFloat(currentVersion)) {
                newVersion = apk.getVersion();
                url = apk.getUrl();
                result = "有新版本可升级, 当前版本号为: " + currentVersion + "; 最新版本号为: " + newVersion;
            } else {
                result = "当前版本为最新版本";
            }
        }
        return new ApkResult(newVersion, url, result);
    }

}