package com.upai.smartneib;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.upai.smartneib.alipay.AliOrderRepository;
import com.upai.smartneib.alipay.AliOrderResult;
import com.upai.smartneib.alipay.Aliorder;
import com.upai.smartneib.notification.Notification;
import com.upai.smartneib.notification.NotificationRepository;
import com.upai.smartneib.notification.NotificationResult;
import com.upai.smartneib.repair.Repair;
import com.upai.smartneib.repair.RepairRepository;
import com.upai.smartneib.repair.RepairResult;
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
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private RepairRepository repairRepository;
    @Autowired
    private AliOrderRepository aliOrderRepository;

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

    /**
     * 获取全部通知
     *
     * @return 返回结果
     */
    @PostMapping(path = "/notification")
    public @ResponseBody
    NotificationResult notification() {
        List<Notification> notificationList = notificationRepository.findAll();
        String result;
        if (notificationList == null) {
            result = "获取失败";
        } else {
            result = "获取成功";
        }
        return new NotificationResult(result, notificationList);
    }

    /**
     * 用户报修接口
     *
     * @param id      主键
     * @param user    用户名
     * @param name    姓名
     * @param phone   电话号码
     * @param address 地址
     * @param content 报修详情
     * @return 报修结果
     */
    @PostMapping(path = "/repair")
    public @ResponseBody
    RepairResult repair(@RequestParam String id, @RequestParam String user, @RequestParam String name,
                        @RequestParam String phone, @RequestParam String address, @RequestParam String content) {
        Repair repair = new Repair();
        repair.setId(id);
        repair.setUser(user);
        repair.setName(name);
        repair.setPhone(phone);
        repair.setAddress(address);
        repair.setContent(content);
        repairRepository.save(repair);
        return new RepairResult(user, name, "报修成功");
    }

    /**
     * 获取订单列表
     *
     * @param user 用户名
     * @return 返回结果
     */
    @PostMapping(path = "/order")
    public @ResponseBody
    AliOrderResult orderList(@RequestParam String user) {
        List<Aliorder> aliorders = aliOrderRepository.findOrderByUser(user);
        String result;
        if (aliorders != null) {
            result = "success";
        } else {
            result = "fail";
        }
        return new AliOrderResult(aliorders, result);
    }

    /**
     * 支付宝订单
     *
     * @param des    订单描述
     * @param id     id
     * @param amount 金额
     * @return 返回结果
     */
    @PostMapping(path = "/pay")
    public @ResponseBody
    String aliPayOrder(@RequestParam String des, @RequestParam String id, @RequestParam String amount) {
        String json = "";
        final String APP_ID = "2016102400752776";
        final String APP_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwHpJSrGwd1lNXg+smOwHttMzFWBor/k53PEdLS2llqslF9mGMZYZWMoiYJcxHEeHJ5C8SYxypa8X7h2YOYXUrnMmFtFsuDqZAcdF82SL5zV+fMN7d+3GqebKAtRpBWO3KxuzgEkeVaV8j/rz3b5jNwOhpkvhLJd3pFEV+BrxBxYe8OfSPB6G/dopGw0ihx/zl6tYBcurRbwmVbFzGsYF9j770eiT5e/YgGCDA33E6llUsGNuRkv2s9ZMg0iwUk/NpRD3ArtLUA1OSQJ0aOhR/Yag8KpeTxkvENZ5/bnZn95Mq6WxdGWyVzyHYmyiOEh9xEz5un7EV1ecH/zPNjqStQIDAQAB";
        final String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCV9xoN1166o+e6/3yuy3V6q5jQuT/iGyAA0hgBy2xyW6FF0y8ycWYYGjsqEM2RbGEW8kbdGTDSvqObx1eTSOdu35JMAczYoxfFRhD5pmZi5zxA5bBVr408qseBsVXnWaxnQ+CX/ILmoqY9IEfvatuN46JOUcj7dNhCWNUVXpheW4Z7SsUAT0cyzEnFOIW/d5YfKk5SsaCBM+4IuSu05YmDLCg+525t1ue7Krc6mMQEPdxjhP5FZ0L20LjEY7HjP2q3OMY2cpgoMwN+k6YkX0InsMV+Uxg4An0h5L19J0qulQCOYudRUoRh2xrk4MZq9H2qpRr19RKAskYOI2Bm5WlVAgMBAAECggEAB2u521N7+Ypgzi0UU2X4A0D4h+OS2LJ06/V5gKU7UZFdMqJXpIxd+/VYViWMtpSBXWxRu2EivFr4p52+pKgn46GmkKJKK7IuW/Gyp1fnmx9MKP30stjECWt+AdnvoUobsOR3+a1iIN2NklrFlIBaKvRHtM5xPpgyl13j9XG/4nNbmPDDxbkYT+73rxKFa9ro0P5EAVwbM9DXnVFMn1VYzmfds15TsEX/gMPeompIEDadGurYK6mRUrjKTLH0Tcbp6bUA51X+8O3cBhMn5XEV7cO0IBsklgImYHx0PtuDaB3Dl/Ve5NnHygy4ns0UlAlqAuVamJfenrNPYd4ZAlaPZQKBgQDFBpVc8bCpwIkT5iQfcQRiElb4mDU24olU7MFYGlRbnnleHr4VrKFlwzMqQPjQCYDbSkNWOLEguqMTL1gZixClYOrc29bBZ4TU//Q9mC4L+GS160GRy67u/KcaaKsYUR7676ZWkTZvK/ZpSc9aOtxJ9b0rnwBblyLB8j7DgYm4XwKBgQDC2m7HTFxNovIrhvegdHj958vqKfB84xpryRayUgY8M208VQxzT1MMcl9TAt4KW/AhvNKU/Dvbo/l3XUtMlYBW7lkSnMSMtqu8/DNr+UmnUQh/XhQ7vBNtGUFeTKFG/1oZsUpF5ga3/yXsdARJ60KWrAk1XOslYd6bTZY8ZdOKywKBgCySYrPWEWs1nU0dIUrjnGQ7VeWDOXajJQJLVSoDOtZHMZmzRrlMhm6pDCgg7qjRnY7+a+FTje6jikTKzxloNmnTVQ6FxT2Xl5tAFBbjGHeox8/H2tuKwpZaHcuBpkMoBuQp1u16iF/6CBKlmf0Fl9Q7fYIixEf0Fu7dSImeAnPtAoGAY/MJ2f9IZaaM1FkCEnNMUOmixrXGnzkbJ2jZ1JSQkDbM9KKnpUpuTjcowHr7DJNGZPfniPateaft4hWf92PEllLiq9JwW8Gj7GfttJgF+OZvFm5asJ1z1YWb61Qhcjqvq/guIhaxIZamjFiijow39vO+MXo9QEqolj9BLjAJR+sCgYEAhu326rUvlRmGgvv2KGBH8cHAulWWuoHyDM2No2slV/MKSGb+2u6s3jvWtq7GWcU/PYwQnSHlnuS+J5CJNjNLK1ZELH4jp8Evg2sgUv491dALemQ/Rok9P0xGJYrGyUJB+3O2QGqHFMebPv/+X8n2G0r4t2J+Q/CqabvwbDeR0LU=";
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do",
                APP_ID, APP_PRIVATE_KEY, "json", "UTF-8", APP_PUBLIC_KEY, "RSA2");
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setSubject(des);
        model.setOutTradeNo(id);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(amount);
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        try {
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            json = response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return json;
    }

}
