package com.example.movieboxoffice.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-06-26
 */
@TableName("t_movie_poster")
public class MoviePoster implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 电影码
     */
    private Long movieCode;

    /**
     * 电影名
     */
    private String movieName;

    /**
     * 海报base64
     */
    private String posterBase64;

    /**
     * 创建时间
     */
    private Date createTime;

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
    public String getPosterBase64() {
        return posterBase64;
    }

    public void setPosterBase64(String posterBase64) {
        this.posterBase64 = posterBase64;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "MoviePoster{" +
            "movieCode=" + movieCode +
            ", movieName=" + movieName +
            ", posterBase64=" + posterBase64 +
            ", createTime=" + createTime +
        "}";
    }
}
