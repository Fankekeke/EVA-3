package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.FileUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/file")
@AllArgsConstructor
public class FileController {

    /**
     *
     * @param file 上传的文件
     * @return
     */
    @Log("图片上传")
    @ResponseBody
    @RequestMapping("/fileUpload")
    public String upload(@RequestParam("avatar") MultipartFile file){
        // 1定义要上传文件 的存放路径
         String localPath="E:/Project/酒店预订/frontend/static/image";
//        String localPath="/usr/project/pro_cos/web/static/image";
        // 2获得文件名字
        String fileName=file.getOriginalFilename();
        // 2上传失败提示
        String warning="";
        String newFileName = FileUtils.upload(file, localPath, fileName);
        MultipartFile file1 = file;
        if(newFileName != null){
            //上传成功
            warning=newFileName;

        }else{
            warning="上传失败";
        }
        System.out.println(warning);
        return warning;
    }

}
