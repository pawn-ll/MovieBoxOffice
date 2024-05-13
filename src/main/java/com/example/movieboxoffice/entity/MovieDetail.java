package com.example.movieboxoffice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

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

    public MovieDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Long getMovieCode() {
        return movieCode;
    }

    public void setMovieCode(Long movieCode) {
        this.movieCode = movieCode;
    }
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }
    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getScripter() {
        return scripter;
    }

    public void setScripter(String scripter) {
        this.scripter = scripter;
    }

    @Override
    public String toString() {
        return "MovieDetail{" +
            "id=" + id +
            ", movieCode=" + movieCode +
            ", movieName=" + movieName +
            ", director=" + director +
            ", actor=" + actor +
            ", type=" + type +
            ", length=" + length +
            ", area=" + area +
            ", introduction=" + introduction +
            ", poster=" + poster +
            ", createTime=" + createTime +
        "}";
    }
}
