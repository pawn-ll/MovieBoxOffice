package com.example.movieboxoffice.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class MovieDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 总票房
     */
    private String sumBoxOffice;



}
