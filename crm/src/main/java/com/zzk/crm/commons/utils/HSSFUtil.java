package com.zzk.crm.commons.utils;

import com.zzk.crm.workbench.pojo.Activity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class HSSFUtil {
    /**
     * 从指定的HSSFCell对象中获取列的值
     */
    public static String getCellValueForStr(HSSFCell cell){
        CellType cellType = cell.getCellType();
        String ret="";
        if(cellType==CellType.STRING){
            ret=cell.getStringCellValue();
        }else if(cellType==CellType.NUMERIC){
            ret=cell.getNumericCellValue()+"";
        }else if(cellType==CellType.BOOLEAN){
            ret=cell.getBooleanCellValue()+"";
        }else if(cellType==CellType.FORMULA){
            ret=cell.getCellFormula();
        }else{
            ret="";
        }
        return ret;
    }

    /**
     * 批量导出市场活动
     */
    public static void createAndSendExcel(HttpServletResponse response, List<Activity> activityList) throws IOException {
        /**使用封装类HSSFWorkbook生成Excel文件*/
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("市场活动");
        //初始化首行，表示字段值
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell = row.createCell(1);
        cell.setCellValue("所有者");
        cell = row.createCell(2);
        cell.setCellValue("活动名称");
        cell = row.createCell(3);
        cell.setCellValue("开始日期");
        cell = row.createCell(4);
        cell.setCellValue("结束日期");
        cell = row.createCell(5);
        cell.setCellValue("成本");
        cell = row.createCell(6);
        cell.setCellValue("描述");
        cell = row.createCell(7);
        cell.setCellValue("创建日期");
        cell = row.createCell(8);
        cell.setCellValue("创建者");
        cell = row.createCell(9);
        cell.setCellValue("上次修改日期");
        cell = row.createCell(10);
        cell.setCellValue("修改者");

        //先判断查询结果是否为空，为空则不进入遍历环节，不为空则遍历插入生成的sheet表格中
        if (activityList!=null && activityList.size()>0){
            Activity activity = null;
            for (int i=0;i<activityList.size();i++){
                //要从activityList的第0个元素开始遍历
                activity = activityList.get(i);

                //要从第1行开始插入
                row = sheet.createRow(i+1);

                //插入数据
                cell = row.createCell(0);
                cell.setCellValue(activity.getId());
                cell = row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell = row.createCell(2);
                cell.setCellValue(activity.getName());
                cell = row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell = row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell = row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell = row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell = row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell = row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell = row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell = row.createCell(10);
                cell.setCellValue(activity.getEditBy());
            }
        }

        /**方式一：把生成的Excel下载到客户端(先下载到本地，再上传到服务器)，会访问两次磁盘，效率低不推荐*/
        /*//根据workbook对象生成Excel文件
        FileOutputStream fos = new FileOutputStream("C:/Users/asus/Desktop/outputs/activity_table.xls");
        *//**这个方法效率非常低，作用是将内存中的数据写入磁盘，而程序只要访问磁盘就十分吃效率（建立连接、寻找磁道）*//*
        workbook.write(fos);

        //关闭资源
        workbook.close();
        fos.flush();
        fos.close();

        //设置响应类型为'应用程序产生的 二进制文件'
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=市场活动表.xls");

        ServletOutputStream out = response.getOutputStream();
        InputStream in = new FileInputStream("C:/Users/asus/Desktop/outputs/activity_table.xls");
        byte[] buff = new byte[1024];
        int len=0;
        *//**这个循环效率非常低，作用是读取文件中的数据并输出到浏览器，也相当于访问磁盘*//*
        while ((len=in.read(buff))!=-1){
            out.write(buff,0,len);
        }

        //关闭资源
        out.flush();
        in.close();*/

        /**方式二：直接把内存中的workbook上传到服务器，没有下载再上传的过程，不会访问磁盘，效率很高，且少写了很多代码*/
        //设置响应信息，表明返回的是一个文件，且直接让浏览器通过下载器下载该文件
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=市场活动表.xls");

        //直接调用HSSFWorkbook.write(response.getOutputStream())，将内存中的HSSFWorkbook对象直接通过HttpServletResponse返回给Browser
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);

        //关闭资源
        out.flush();
        workbook.close();
    }
}
