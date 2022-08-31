package com.example.demo;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;
import java.nio.file.Paths;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

//    @Test
//    public void generateAsciiDocs() throws Exception {
//        //    输出Ascii格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .build();
//
//        Swagger2MarkupConverter.from(new URL("http://localhost:8081/v2/api-docs"))
//                .withConfig(config)
//                .build()
//                .toFile(Paths.get("src/docs/asciidoc/generated/all"));
//    }

}
