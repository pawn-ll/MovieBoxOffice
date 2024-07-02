package com.example.movieboxoffice.utils;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class CodeGeneratorUtils {


    public static void main(String[] args) {
        CodeGeneratorUtils.fastGenerate();
    }

    public static void fastGenerate(){
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/movie-boxoffice?useUnicode=true&characterEncoding=utf8",
                         "root",
                         "root123")
                .globalConfig(builder -> {
                    builder.author("baomidou") // 设置作者
                            .outputDir("D:\\Projects\\MovieBoxOffice\\src\\main\\java"); // 指定输出目录
                }).packageConfig(builder -> {
                    builder.parent("com.example.movieboxoffice") // 设置父包名
                            .moduleName("") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "D:\\Projects\\MovieBoxOffice\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("t_actor") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_"); // 设置过滤表前缀
                })
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
