package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.config.AjaxResponse;
import com.example.demo.model.Picture;
import com.example.demo.service.PicService;
import com.example.demo.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import static com.example.demo.config.StaticConfig.*;

/**
 * @author zhangduhuang
 * @date 2021/8/19 10:02
 */
@Slf4j //日志服务
@Api(tags = "图片处理接口")
@RequestMapping("/picture")
@Controller
public class PictureController {
    @Autowired
    PicService picService;
    @ApiOperation(value = "上传文件", notes = "上传显微镜文件,该接口使用 multipart file 格式进行上传，然后附带用户名，指定哪一个用户的值，且指定主题和描述")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true),
            @ApiImplicitParam(paramType = "query", name = "picture", value = "上传图片文件", required = true),
            @ApiImplicitParam(paramType = "query", name = "theme", value = "图片主题", required = true),
            @ApiImplicitParam(paramType = "query", name = "picdesc", value = "图片描述", required = true),

    })
    @ResponseBody
    @PostMapping("uploadPic")
    public AjaxResponse addPicture(
            HttpServletRequest request, HttpServletResponse response
            ){

        //接收文件对象
        try{
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request; //文件处理对象
            List<MultipartFile> pic_files = multipartHttpServletRequest.getFiles("picture");//可传多个文件,这个是一个列表
            if(pic_files.size()<1) return AjaxResponse.falseResult("empty picture upload");
            String picname=pic_files.get(0).getOriginalFilename();
            String path=PIC_DIR+picname;
            /*文件存储本地部分*/

            File folder = new File(PIC_DIR);//判断文件夹是否生成
            if (!folder.exists()) {
                folder.mkdirs();
            }
            //存入文件
            InputStream is = pic_files.get(0).getInputStream();
            FileOutputStream fout = new FileOutputStream(new File(path));

            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = (is.read(buffer))) > -1) {
                fout.write(buffer, 0, len);
            }
            fout.close();
            is.close();
            String username=request.getParameter("username");
            String theme=request.getParameter("theme");
            String picdesc=request.getParameter("picdesc");
            /*上传数据库*/
            Picture picture=new Picture();
            picture.setUsername(username);
            picture.setTheme(theme);
            picture.setPicdesc(picdesc);
            System.out.println(path);
            picture.setPath(path);
            picture.setStatus(0);
            picture.setSave(0);
            Picture picture1=picService.addPic(picture);//同时获得一个数据库存储的对象，便于接下来进行操作
            System.out.println(picture1 +""+ picture1.getPid());
            /*执行算法部分*/
            Thread thread = new Thread(() -> {
                System.out.println("开始执行");
                HttpUtil httpUtil=new HttpUtil();
                String result=httpUtil.sendPost("http://101.37.204.129:8082/count","path="+path);
                System.out.println(result);
                System.out.println("开始解析json");

                JSONObject obj = (JSONObject) JSON.parse(result);
                String status=(String) obj.get("status");
                if("ok".equals(status)) {
                    /*数据库操作*/
                    picture1.setAlivenum((Integer) obj.get("alive"));
                    picture1.setDeadnum((Integer) obj.get("dead"));
                    picture1.setStatus(1);
                    picService.addPic(picture1);
                    System.out.println(picture1.getPid());
                }else {
                    System.out.println("失败了");
                }
                System.out.println("多线程的线程结束了");
            });
            thread.start();

            return AjaxResponse.success(picture1);


        }catch (Exception e){
            e.printStackTrace();
            return AjaxResponse.falseResult("picture update failure");
        }

    }
    /*
    request.getParameterMap().get("XXX");

            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        List<MultipartFile> files1 = multipartHttpServletRequest.getFiles("file1");//可传多个文件
        List<MultipartFile> files2 = multipartHttpServletRequest.getFiles("file2");//可传多个文件
        String filename1=files1.get(0).getOriginalFilename();


     */

    @ApiOperation(value = "获取图片列表", notes = "获取该用户传的每一张图片的信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名", required = true)
    })
    @ResponseBody
    @PostMapping("resultList")
    public AjaxResponse getResultList(
            @RequestParam("username") String username
    ){
        System.out.println(username);
        List<Picture> result= picService.getResult(username,1,1);
        String path="";
        for(Picture item:result){
            path=item.getPath();
            item.setPath(PIC_SERVER_URL +PIC_PATTERN+path.substring(path.lastIndexOf("/")+1));
        }
        return AjaxResponse.success(result);
    }

    @ApiOperation(value = "返回图片http地址", notes = "根据path返回http链接")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "path", value = "路径", required = true)
    })
    @ResponseBody
    @PostMapping("getRealPath")
    public AjaxResponse getPicSource(
            @RequestParam("path") String path
    ){
        return AjaxResponse.success(PIC_SERVER_URL +"/image"+path.substring(path.lastIndexOf("/")));
    }

    @ApiOperation(value = "获取图片", notes = "根据图片pid获取图片")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pid", value = "图片id", required = true),

    })
    @ResponseBody
    @PostMapping("getPic")
    public AjaxResponse getPic(
            @RequestParam("pid") int pid

    ){
        Picture picture=picService.getPic(pid);
        if (picture==null) return AjaxResponse.falseResult("找不到图片");
        String path=picture.getPath();
        picture.setPath(PIC_SERVER_URL +PIC_PATTERN+path.substring(path.lastIndexOf("/")+1));
        return AjaxResponse.success(picture);
    }

    @ApiOperation(value = "保存图片", notes = "将临时图片存储为图片值")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "pid", value = "图片id", required = true),
    })
    @ResponseBody
    @PostMapping("savePic")
    public AjaxResponse savePic(
            @RequestParam("pid") int pid

    ){
        Picture picture1=picService.getPic(pid);
        if (picture1==null) return AjaxResponse.falseResult("找不到图片");
        picture1.setSave(1);
        picService.addPic(picture1);
        return AjaxResponse.success(picture1);
    }
}
