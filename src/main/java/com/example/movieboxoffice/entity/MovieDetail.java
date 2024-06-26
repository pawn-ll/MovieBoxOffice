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
 * @since 2024-05-10
 */
@TableName("t_movie_detail")
@Data
public class MovieDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电影id
     */
    private Long movieCode;

    /**
     * 电影名称
     */
    private String movieName;

    /**
     * 导演
     */
    private String director;

    /**
     * 编剧
     */
    private String scripter;

    /**
     * 主演
     */
    private String actor;


    /**
     * 上映日期
     */
    private String releaseDate;

    /**
     * 影片类型
     */
    private String type;

    /**
     * 影片时长
     */
    private String length;

    /**
     * 制片地区
     */
    private String area;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 海报
     */
    private String poster;


    /**
     * 创建时间
     */
    private Date createTime;


}
