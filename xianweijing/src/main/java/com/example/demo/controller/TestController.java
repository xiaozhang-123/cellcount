package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.AjaxResponse;
import com.example.demo.util.HttpUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.StaticConfig.PIC_SERVER_URL;

/**
 * @author zhangduhuang
 * @date 2021/7/18 17:33
 */
@Controller
public class TestController {
//    @Autowired
//    RedisService redisService;
//
//
//    @RequestMapping("test1")
//    @ResponseBody
//    public void test1(
//             @RequestBody User user,
//             @RequestBody UserVoPost userVoPost
//            ){
//        redisService.save("1","2");
//        System.out.println("test");
//        System.out.println(redisService.get("1")+"结果");
//    }
    @RequestMapping("test2")
    @ResponseBody
    public String test2(

    ){

        System.out.println("test");
        return "Hello World";
    }

    @PostMapping("getRealPath")
    public AjaxResponse getPicSource(
            @RequestParam("path") String path
    ){
        return AjaxResponse.success(PIC_SERVER_URL +"/image/"+path.substring(path.lastIndexOf("/")));
    }

    @PostMapping("test3")
    @ResponseBody
    public String test3(){

//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                System.out.println("开始执行");
//                HttpUtil httpUtil=new HttpUtil();
//                String path="/root/image/cell1.jpg";
//                String result=httpUtil.sendPost("http://101.37.204.129:8082/count","path="+path);
//                System.out.println(result);
//                System.out.println("开始解析json");
//
//                JSONObject obj = (JSONObject) JSON.parse(result);
//                String status=(String) obj.get("status");
//                if("ok".equals(status)) {
//                    int alivenum=(Integer) obj.get("alive");
//                    int deadnum=(Integer) obj.get("dead");
//                    System.out.println(alivenum+"   fsdadfsaf   "+deadnum);
//                }else {
//                    System.out.println("失败了");
//                }
//                System.out.println("多线程的线程结束了");
//            }
//        };
//        thread.start();
        return "hhh";
    }
}
