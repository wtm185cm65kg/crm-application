package com.zzk.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Controller
public class FileDownOrUploadTestController {
    /**
     * 对于返回文件而言，应设置返回值类型为void，文件的返回通过手动的方式返回
     */
    @RequestMapping("/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws Exception {
        /**设置响应类型*/
        //设置响应类型为'文本类型的 html格式'
        //response.setContentType("text/html;charset=UTF-8");
        //设置响应类型为'应用程序产生的 二进制文件'
        response.setContentType("application/octet-stream;charset=UTF-8");

        /**设置响应头信息Content-Disposition的值为attachment，以直接激活文件下载窗口，而不是走默认的直接打开文件的步骤
         * 还可以通过filename指定文件的名称及扩展名*/
        response.setHeader("Content-Disposition","attachment;filename=my_student_list.xls");

        /**获取输出流
         * 为了能兼容各类型，要将response.getWriter()得到的Writer对象转为OutputStream对象
         * 或者直接获取一个OutputStream对象*/
        //OutputStream out = new WriterOutputStream(response.getWriter());
        ServletOutputStream out = response.getOutputStream();

        /**
         * 读取(InputStream)Excel文件，将其输出(OutputStream)到浏览器
         */
        FileInputStream fis = new FileInputStream("D:\\Workspace\\CRM-Project\\crm\\src\\test\\java\\file\\studentList.xls");
        byte[] buff=new byte[1024];     //一次缓存1MB
        int len=0;
        while ((len=fis.read(buff)) != -1){
            out.write(buff,0,len);
        }

        /**
         * 关闭流
         * 规则：谁开启的谁关闭
         *      这里程序员只开启了fis，因此只需要关闭fis
         *      out是属于HttpServletResponse的，HttpServletResponse是Tomcat创建/开启的，程序员无需多问，最多就是flush()一下
         */
        fis.close();
        out.flush();
        //out.close();
    }


    /**
     * SpringMVC提供了一个MultipartFile类，专门用于接收文件类型的数据
     * 必须在springmvc.xml中配置SpringMVC的文件上传解析器
     */
    @RequestMapping("/fileUpload.do")
    public void fileUpload(String username, MultipartFile myFile) throws IOException {
        /**把文本数据打印到控制台*/
        System.out.println("username: "+username);

        /**把文件数据在服务器指定的目录中生成一个同样的文件（相当于上传到服务器）
         * MultipartFile的transferTo(File dest)方法：指定文件的上传位置
         * MultipartFile的getOriginalFilename()方法：获取上传文件的名字（包含后缀）
         * MultipartFile的getName()方法：获取上传文件的名字（不包含后缀）*/
        File file = new File("D:\\Workspace\\CRM-Project\\crm\\src\\test\\java\\file\\"+myFile.getOriginalFilename());
        myFile.transferTo(file);
    }
}
