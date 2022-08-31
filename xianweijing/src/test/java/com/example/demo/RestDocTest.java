package com.example.demo;

import com.example.demo.controller.TestController;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author zhangduhuang
 * @date 2021/7/18 23:50
 */

@RunWith(SpringRunner.class)
@WebMvcTest(TestController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class RestDocTest {

    @Resource
    private MockMvc mockMvc;

    @Test
    public void test01() throws Exception {
        this.mockMvc.perform(get("/test2")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello World")))
                .andDo(document("test2"));
    }
}
