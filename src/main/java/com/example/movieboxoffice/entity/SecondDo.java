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
 * @since 2024-05-13
 */
@TableName("t_second_do")
public class SecondDo implements Serializable {

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
     * 是否已爬取详情（0-否 1-是）
     */
    private Integer isDo;

    /**
     * 创建时间
     */
    private Date createTime;

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
    public Integer getIsDo() {
        return isDo;
    }

    public void setIsDo(Integer isDo) {
        this.isDo = isDo;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SecondDo{" +
            "id=" + id +
            ", movieCode=" + movieCode +
            ", movieName=" + movieName +
            ", isDo=" + isDo +
            ", createTime=" + createTime +
        "}";
    }
}
