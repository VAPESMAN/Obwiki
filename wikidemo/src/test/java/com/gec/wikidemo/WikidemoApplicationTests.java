package com.gec.wikidemo;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import com.gec.wikidemo.entity.Ebook;
import com.gec.wikidemo.mapper.EbookMapper;
import org.apache.catalina.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

@SpringBootTest
class WikidemoApplicationTests {

    @Autowired
    private EbookMapper ebookMapper;
    @Test
    void contextLoads() {
        //UserMapper 中的 selectList() 方法的参数为 MP 内置的条件封装器 Wrapper
    //所以不填写就是无任何条件
        List<Ebook> ebooks = ebookMapper.selectList(null);
        ebooks.forEach(ebook -> System.out.println(ebook));
    }

    @Test
    void generateCode() {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/wiki?serverTimezone=UTC", "root", "474769ab")
                .globalConfig(builder -> {
                    builder.author("yq") // 设置作者
                            .outputDir(System.getProperty("user.dir") + "/src/main/java") // 指定输出目录
                            .disableOpenDir(); // 禁止打开输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.gec.wikidemo") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("content") // 设置需要生成的表名
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .controllerBuilder()
                            .enableRestStyle(); // 启用 REST 风格
                })
                .templateEngine(new VelocityTemplateEngine()) // 使用 Velocity 引擎模板
                .execute(); // 执行
    }




}