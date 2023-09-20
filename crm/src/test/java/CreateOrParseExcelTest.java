import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 使用apache-poi生成Excel文件
 */
public class CreateOrParseExcelTest {
    @Test
    public void createExcel() throws IOException {
        //创建HSSFWorkbook对象，对应一个excel文件(当前是一个空文件)
        HSSFWorkbook workbook = new HSSFWorkbook();

        //使用workbook创建HSSFSheet对象，对应workbook文件中的一页
        HSSFSheet sheet = workbook.createSheet("学生列表");

        /**初始化属性行*/
        //使用sheet创建HSSFRow对象，对应sheet中的行
        HSSFRow row = sheet.createRow(0);   //行号（从0开始）
        //使用row创建HSSFCell对象，对应row中的列
        HSSFCell cell = row.createCell(0); //列好（从0开始）

        //设置第一列第一行的信息
        cell.setCellValue("学号");
        //设置第二列第一行的信息
        cell = row.createCell(1);
        cell.setCellValue("姓名（居中对齐）");
        //设置第三列第一行的信息
        cell = row.createCell(2);
        cell.setCellValue("年龄");

        //生成HSSFCellStyle对象,用于修饰单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER); //设置对齐方式为居中对齐

        /**添加数据
         * 使用sheet创建10个HSSFRow对象，对应sheet中的10行,向每行中添加3条列数据*/
        for (int i = 1; i <= 10; i++) {
            row = sheet.createRow(i);

            cell = row.createCell(0);
            cell.setCellValue(2020028200 + i);
            cell = row.createCell(1);
            cell.setCellStyle(cellStyle);   //只让'姓名'一栏居中对齐(在循环中设置该列的对齐方式为刚刚封装的HSSFCellStyle对象)
            cell.setCellValue("test" + i);
            cell = row.createCell(2);
            cell.setCellValue(20 + i);
        }

        //调用工具函数生成excel文件
        FileOutputStream out = new FileOutputStream("D:\\Workspace\\CRM-Project\\crm\\src\\test\\java\\file\\studentList.xls");
        workbook.write(out);

        //关闭资源
        out.flush();
        out.close();
        workbook.close();

        System.out.println("===========create ok==========");
    }


    /**
     * 使用apache-poi解析Excel文件
     */
    @Test
    public void parseExcel() throws IOException {
        /**与创建不同的是: new HSSFWorkbook时要传入输入流，表示根据文件生成HSSFWorkbook对象*/
        FileInputStream fis = new FileInputStream("D:\\Workspace\\CRM-Project\\crm\\src\\test\\java\\file\\activityListImport.xls");
        HSSFWorkbook workbook = new HSSFWorkbook(fis);

        /**与创建不同的是: 之前是workbook.createSheet()，现在是workbook.getSheetAt(0)，表示获取第一个表*/
        HSSFSheet sheet = workbook.getSheetAt(0);   //根据workbook获取sheet

        /**获取表中的数据并打印,外层循环遍历row，内层循环遍历每个row的cell*/
        HSSFRow row = null;
        HSSFCell cell = null;
        /**行下标从0开始，会将第一行的字段/属性名列一并遍历
         * 后期与数据库结合使用是一般不会获取第一行，而是从第二行的数据行开始遍历，行下标应从1开始*/
        for (int i=0;i<=sheet.getLastRowNum();i++){  //根据sheet获取总行数,getLastRowNum()表示最后一行的下标，相当于数据行的总行数-1
            row = sheet.getRow(i);   //根据sheet获取row

            for (int j=0;j<row.getLastCellNum();j++){  //根据sheet获取总行数,getLastRowNum()表示最后一列的下标+1
                cell = row.getCell(j);   //根据row获取cell

                CellType cellType = cell.getCellType();   //根据cell获取cellType
                /**通过对cellType的类型进行判断执行不同的getCellValue()
                 * 实际使用时一般将以下这段代码封装*/
                if (cellType==CellType.STRING){   //字符串类型
                    System.out.print(cell.getStringCellValue()+" ");
                }else if (cellType==CellType.NUMERIC){  //数值型类型
                    System.out.print(cell.getNumericCellValue()+" ");
                }else if (cellType==CellType.BOOLEAN) {   //布尔类型
                    System.out.print(cell.getBooleanCellValue()+" ");
                }else{
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
    }
}
