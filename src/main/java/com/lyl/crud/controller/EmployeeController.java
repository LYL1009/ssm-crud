package com.lyl.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lyl.crud.bean.Employee;
import com.lyl.crud.bean.Msg;
import com.lyl.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 单个批量员工删除二合一
     * 批量删除：1-2-3
     * 单个删除：1
     * @param ids
     * @return
     */
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmp(@PathVariable("id") String ids) {
        if (ids.contains("-")) {
            //批量删除
            List<Integer> del_ids = new ArrayList<>();
            String[] str_ids = ids.split("-");
            //组装id的集合
            for (String str:str_ids) {
                del_ids.add(Integer.parseInt(str));
            }
            employeeService.deleteBatch(del_ids);
        } else {
            //单个删除
            Integer id = Integer.parseInt(ids);
            employeeService.deleteEmp(id);
        }
        return Msg.success();
    }

    /**
     * 如果直接发送ajax=PUT形式的请求
     * 问题：
     * 请求体中的有数据
     * 但是Employee对象封装不上
     *
     * AJAX发送PUT请求引发的血案：
     * PUT请求，请求体中的数据，request.getParameter("empName")拿不到
     * Tomcat一看是PUT不会封禁请求体中的数据为map，只有POST形式的请求才封禁请求体为map
     *
     * 解决方案：
     * 我们要能支持直接发送PUT请求之类的请求还要封装请求体中的数据
     * 1.配置上HttpPutFormContentFilter；
     * 2.它的作用：将请求体中的数据解析包装成一个map
     * 3.request被重新包装，request.getParameter()被重写，就会从自己封装的map中取数据
     * 员工更新
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp/{empId}",method = RequestMethod.PUT)
    @ResponseBody
    public Msg updateEmp(Employee employee) {
        System.out.println("将要更新的员工数据"+employee);
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     * 查询单个员工信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getEmp(id);
        return Msg.success().add("emp",employee);
    }

    /**
     * 1.支持JSP303校验
     * 2.导入Hibernate-Validate
     * 检查用户名是否可用
     * @param empName
     * @return
     */
    @RequestMapping("/checkuser")
    @ResponseBody
    public Msg checkUser(@RequestParam("empName") String empName) {
        //先判断用户名是否是合法的表达式；
        String regx = "(^[A-Za-z0-9_-]{3,12}$)|(^[\\u4e00-\\u9fa5]{2,5})";
        if (!empName.matches(regx)) {
            return Msg.error().add("va_msg","用户名可以是2-5位中文或者3-12位英文和数字的组合！");
        }
        //数据库用户名重复校验
        boolean b = employeeService.checkUser(empName);
        if (b) {
            return Msg.success();
        } else {
            return Msg.error().add("va_msg","用户名不可用");
        }
    }

    /**
     * 员工保存
     */
    @RequestMapping(value = "/emp",method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            //校验失败，应该返回失败，在模态框中显示校验失败的信息
            Map<String,Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError : errors) {
                System.out.println("错误的字段名"+fieldError.getField());
                System.out.println("错误信息"+fieldError.getDefaultMessage());
                map.put(fieldError.getField(),fieldError.getDefaultMessage());
            }
            return Msg.error().add("errorFields",map);
        } else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

    /**
     * 所有员工查询
     * @param pn
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(@RequestParam(value = "pn",defaultValue = "1") Integer pn) {
        PageHelper.startPage(pn,5);
        List<Employee> emps = employeeService.getAllEmps();
        PageInfo page = new PageInfo(emps,5);
        return Msg.success().add("pageInfo",page);
    }

    //@RequestMapping(value = "/emps")
    public String getEmps(@RequestParam(value = "pn",defaultValue = "1") Integer pn, Model model) {
        PageHelper.startPage(pn,5);
        List<Employee> emps = employeeService.getAllEmps();
        PageInfo page = new PageInfo(emps,5);
        model.addAttribute("pageInfo",page);
        return "list";
    }

}
