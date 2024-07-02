package com.example.movieboxoffice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-07-01
 */
@Data
@TableName("t_actor")
public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 别名
     */
    private String alias;

    /**
     * 性别（0-女  1-男 2-未知）
     */
    private Integer gender;

    /**
     * 出生日期
     */
    private String birthday;

    /**
     * 出生地
     */
    private String birthpalce;

    /**
     * 职业
     */
    private String career;


    /**
     * 简介
     */
    private String introduction;

    /**
     * 海报url
     */
    private String poster;

    /**
     * 豆瓣url
     */
    private String dbUrl;

    /**
     * 创建时间
     */
    private Date createTime;


}
