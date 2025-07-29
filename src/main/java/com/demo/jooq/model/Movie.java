package com.demo.jooq.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    private Integer id;
    // 对应数据库的 year_released 列
    private Integer year;
    private String title;
    private String director;
    private String genre;
    private Integer likes;

    // 注意：在jOOQ中，这个POJO不再需要JPA的注解，
    // 它只是一个普通的Java Bean，用于数据传输和jOOQ的映射。
    // jOOQ会根据字段名（或通过配置的映射策略）尝试将数据库列映射到这些字段。
    // 如果字段名与jOOQ生成的Record中的字段名不完全匹配，可能需要自定义转换器或别名。
    // 但对于简单的驼峰命名转换，jOOQ通常能自动处理。
    // 例如，数据库的 `year_released` 列，在POJO中通常命名为 `year`。
}