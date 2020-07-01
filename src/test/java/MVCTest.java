import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "file:dispatcher-servlet.xml"})
public class MVCTest {

    @Autowired
    WebApplicationContext context;
    MockMvc mockMvc;

    @Before
    public void initMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void test(){
        System.out.println("hello");
    }

//    @Test
//    public void test() throws Exception {
//        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/emps").param("pn", "5")).andReturn();
//        MockHttpServletRequest request = result.getRequest();
//        PageInfo pageInfo = (PageInfo) request.getAttribute("pageInfo");
//        //获取页码
//        System.out.println("当前页码"+pageInfo.getPageNum());
//        System.out.println("总页码"+pageInfo.getPages());
//        System.out.println("总记录数"+pageInfo.getTotal());
//        int[] nums = pageInfo.getNavigatepageNums();
//        System.out.println("在页面需要连续显示的页码");
//        for (int i:nums) {
//            System.out.print(i+" ");
//        }
//        System.out.println();
//        //获取员工信息
//        List<Employee> list = pageInfo.getList();
//        for (Employee employee:list) {
//            System.out.println("ID:"+employee.getEmpId()+" Name:"+employee.getEmpName());
//        }
//    }

}
