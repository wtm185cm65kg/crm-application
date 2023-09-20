package com.zzk.crm.workbench.web.controller;

import com.zzk.crm.commons.pojo.ReturnJson;
import com.zzk.crm.commons.utils.DateUtil;
import com.zzk.crm.commons.utils.HSSFUtil;
import com.zzk.crm.commons.utils.UUIDUtil;
import com.zzk.crm.contants.Constants;
import com.zzk.crm.settings.pojo.User;
import com.zzk.crm.settings.service.UserService;
import com.zzk.crm.workbench.pojo.Activity;
import com.zzk.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Controller
public class ActivityController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        /**
         * 调用service层方法所查询所有用户
         * 并在页面请求转发跳转时将查询结果传入request域(不用session域：session太大，该数据仅仅是跳转时携带，没必要不在当前页面时也携带着)
         */
        List<User> users = userService.queryAllUsers();
        request.setAttribute(Constants.REQUEST_USERS,users);
        //请求转发到市场活动主页面
        return "workbench/activity/index";
    }

    /**
     * 点击添加/修改/删除，最终展示的页面仍是activity/index.jsp，因此这三个操作也应写在ActivityController中
     */
    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        /**由于前端传过来的只是部分数据
         * 并没有id、createTime、createBy属性，因此要手动添加
         * 使用UUID生成UUID并调用其toString()转为字符串，并将生成的所有'-'替换为空串，以获得独一无二的id*/
        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateUtil.formatDateTime19(new Date()));
        /**获取session的user，以确定创建者
         * 由于'活动'与'用户'是多对一关系（一个用户可以创建多个活动，一个活动只能由一个用户创建）
         * 因此需要传入的createBy是外键，应传入user的id，而不是其name*/
        User user = (User)session.getAttribute(Constants.SESSION_USER);
        //activity.setCreateBy(user.getName());
        activity.setCreateBy(user.getId());

        ReturnJson returnJson = new ReturnJson();
        /**
         * 与查询不同的是，CUD一般要进行try..catch捕捉异常
         */
        try {
            int ret = activityService.saveCreateActivity(activity);

            if (ret>0){//插入成功
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {//插入失败
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                /**
                 * 其实这里就是单纯的后台问题，但为了转移客户的情绪，防止认为我们做的软件不好
                 * 一般都会进行矛盾转移，把后台CUD失败的锅输出信息为'系统忙'
                 * 实际做过开发的人都知道这种输出是后端做的不好，但一般客户并不会怀疑到后端，只是认为系统忙
                 */
                returnJson.setMessage("系统忙，请稍后重试....");
            }
        }catch (Exception e){//插入报异常
            e.printStackTrace();
            /**这里如法炮制上面的做法*/
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("系统忙，请稍后重试....");
        }
        return returnJson;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        Map map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        /**beginNo的计算公式放在Controller层封装，不要放到Service层，否则还要取、计算、删除原pageNo，很麻烦*/
        int beginNo = (pageNo-1)*pageSize;
        map.put("beginNo",beginNo);
        map.put("pageSize",pageSize);

        List<Activity> activityList = activityService.queryActivityByConditionForPage(map);
        int totalRows=activityService.queryCountOfActivityByCondition(map);

        Map<String,Object> retMap=new HashMap<>();
        retMap.put(Constants.ACTIVITY_LIST,activityList);
        retMap.put(Constants.TOTAL_ROWS,totalRows);

        return retMap;
    }


    @RequestMapping("/workbench/activity/removeActivityByIds.do")
    @ResponseBody
    @Transactional
    /**为了与客户端传过来的属性名一致，这里的参数名不能使用ids，而要使用id.    当然也可以使用@RequestParam手动指定属性名*/
    public Object removeActivityByIds(String[] id){
        ReturnJson returnJson = new ReturnJson();

        /**对于CUD语句要使用try..catch捕捉*/
        try {
            int ret = activityService.removeActivityByIds(id);
            /**ret==-2也在判定成功的范畴：兼容Oracle数据库的批量删除(Oracle批量删除成功返回结果为-2，失败返回-3)*/
            if (ret>0||ret==-2){
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                /**经典做法：怨气转移*/
                returnJson.setMessage("系统忙，请联系系统管理员....");
            }
        }catch(Exception e){
            e.printStackTrace();
            /**如法炮制经典做法*/
            returnJson.setMessage("系统忙，请联系系统管理员....");
        }

        return returnJson;
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        Activity activity = activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/modifyActivity.do")
    @ResponseBody
    public Object modifyActivity(Activity activity,HttpSession session) {
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        /*封装参数：
            如果做查询条件/参数间不属于一个实体类对象/无实体类的主键id，封装成map
            如果做CUD/参数间属于一个实体类对象且有主键id，封装成实体类对象*/
        activity.setEditTime(DateUtil.formatDateTime19(new Date()));
        //name不唯一，可能会有重名的情况，况且数据库的edit_by字段要求的也是一个外键
        //activity.setEditBy(user.getName());
        activity.setEditBy(user.getId());

        ReturnJson returnJson=new ReturnJson();
        try {
            int ret = activityService.saveEditActivity(activity);
            if (ret == 1){
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
                returnJson.setMessage("系统忙，请联系系统管理员....");
            }
        }catch (Exception e){
            e.printStackTrace();
            returnJson.setMessage("系统忙，请联系系统管理员....");
        }
        return returnJson;
    }

    /**
     * 返回的如果是一个文件类型，则：
     *      1.必须是同步请求get/post
     *      2.异常处理直接throws向上抛出，而不是try..catch捕捉
     *      3.HttpServletResponse的应
     *          设置响应类型为二进制流文件
     *              setContentType("application/octet-stream;charset=UTF-8");
     *          设置响应头信息的Content-Disposition的值为attachment
     *              setHeader("Content-Disposition","attachment;filename=my_student_list.xls");
     */
    @RequestMapping("/workbench/activity/exportActivityExcel.do")
    public void exportActivityExcel(HttpServletResponse response) throws IOException {
        //查询所有市场活动
        List<Activity> activityList = activityService.queryAllActivity();

        //调用封装方法生成并向服务器发送Excel文件
        HSSFUtil.createAndSendExcel(response,activityList);
    }

    @RequestMapping("/workbench/activity/exportSelectedActivityExcel.do")
    public void exportSelectedActivityExcel(HttpServletResponse response,String[] id) throws IOException {
        //按条件查询市场活动
        List<Activity> activityList = activityService.queryActivityByCondition(id);

        //调用封装方法生成并向服务器发送Excel文件
        HSSFUtil.createAndSendExcel(response,activityList);
    }

    @RequestMapping("/workbench/activity/importActivityExcel.do")
    @ResponseBody
    public Object importActivityExcel(MultipartFile activityFile,HttpSession session){
        List<Activity> activityList = new ArrayList<>();
        ReturnJson returnJson = new ReturnJson();
        User user = (User)session.getAttribute(Constants.SESSION_USER);

        try {
            //把Excel文件写到磁盘中，且文件名及其后缀与上传时的文件保持一致
            /*File file = new File("C:\\Users\\asus\\Desktop\\",activityFile.getOriginalFilename());
            *//**将内存中的数据（activityFile）写入磁盘，效率很低*//*
            activityFile.transferTo(file);

            *//**解析Excel文件，并将其中的数据封装为List<Activity>*//*
            FileInputStream fis = new FileInputStream(file);
            *//**将磁盘中数据放入内存（workbook），效率很低*//*
            HSSFWorkbook workbook = new HSSFWorkbook(fis);*/

            /**将activityFile内存中的数据 直接写入 workbook内存中，没有连接磁盘，效率高*/
            InputStream in = activityFile.getInputStream();
            HSSFWorkbook workbook = new HSSFWorkbook(in);

            HSSFSheet sheet = workbook.getSheetAt(0);

            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            /**将数据插入时应从下标为1的位置开始遍历，
             * 如果从0开始则表示从第一行的字段/属性名开始遍历，并非是数据行
             * 而这里是向数据库中插入数据，因此要从第二行的数据行开始遍历*/
            for(int i=1;i<=sheet.getLastRowNum();i++){
                row = sheet.getRow(i);

                activity=new Activity();
                /**将动态字段先设置好*/
                activity.setId(UUIDUtil.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateBy(user.getId());
                activity.setCreateTime(DateUtil.formatDateTime19(new Date()));

                /**遍历并按序封装其余动态字段*/
                for (int j=0;j<row.getLastCellNum();j++){
                    //根据row获取HSSFCell对象，封装了一列的所有信息
                    cell = row.getCell(j);

                    //获取当前列（单元格）中的数据
                    String cellValue = HSSFUtil.getCellValueForStr(cell);

                    /**所有的文件上传，上传的格式一定是跟用户提前约定好的，否则这里的循环无法判断应该赋给Activity对象的哪个属性*/
                    if (j==0){
                        /**如果这里的id是用户自己写的，则可能格式并不规范，且有冲突的风险
                         * 因此一般不导入id，且与用户约定时也不添加id列，且导出时也不要导出id（否则回来用导出xls再导入会出错）*/
                        //activity.setId(cellValue);

                        /**插入owner时，应插入其id而不是其name
                         * 将name转为id的办法：
                         *     1.根据name去数据库里查id（不可取，反面案例）
                         *       可能有重名的情况，且会连接磁盘，效率很低
                         *     2.写一个附录，提供user表，用户手动将name换为对应id（效率最高但不现实，小数据量时可用）
                         *       用户体验不好，且数据量很大时要配置很久，只适合小数据量
                         *     3.设置一个公共账户，登陆公共账号的人手动分配外键（最整洁但也最麻烦，至少不需要用户操作）
                         *       不能实时插入外键，要等后期有人手动分配
                         *     4.谁导入的谁负责，就是直接获取session域中当前登录的user（最省事，效率也不低，但是外键会被写死而不是动态的）
                         *       这样会强行将owner设置为当前登录的用户
                         * 这里采用方案4，将所有者设置为当前用户，因此在与用户约定时不添加owner列*/
                        //activity.setOwner(cellValue);

                        /**创建时间设置为当前系统时间即可，因此在与用户约定时不添加create_time列*/
                        //activity.setCreateTime(cellValue);

                        /**创建者设置为当前用户即可，因此在与用户约定时不添加create_by列*/
                        //activity.setCreateBy(cellValue);

                        /**没有修改过，因此在与用户约定时也不添加edit_by和edit_time列*/

                        activity.setName(cellValue);
                    }else if (j==1){
                        activity.setStartDate(cellValue);
                    }else if (j==2){
                        activity.setEndDate(cellValue);
                    }else if (j==3){
                        activity.setCost(cellValue);
                    }else if (j==4){
                        activity.setDescription(cellValue);
                    }
                }
                activityList.add(activity);
            }
            //执行批量插入语句
            int ret = activityService.saveActivityByList(activityList);
            //只要不报异常就是成功了（这里不对ret进行判断，因为如果ret=0说明用户给了一个空表，并不能说明导入失败）
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_SUCCESS);
            //将批量插入结果一并封装到returnJson中
            returnJson.setRetData(ret);
        } catch (Exception e) {
            e.printStackTrace();
            //进入catch表明导入失败
            returnJson.setCode(Constants.RETURN_OBJECT_CODE_FAIL);
            returnJson.setMessage("请检查您的文件格式是否正确！");
        }

        return returnJson;
    }

    /**
     * 跳转到市场活动详细信息页面
     */
    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
        Activity activity = activityService.queryActivityForDetailById(id);
        request.setAttribute(Constants.REQUEST_ACTIVITY,activity);
        return "workbench/activity/detail";
    }
}