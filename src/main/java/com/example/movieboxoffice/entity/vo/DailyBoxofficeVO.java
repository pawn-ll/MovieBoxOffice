package com.example.movieboxoffice.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
@TableName("t_daily_boxoffice")
public class DailyBoxofficeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电影编码
     */
    private Long movieCode;

    /**
     * 电影名称
     */
    private String movieName;

    /**
     * 电影总票房
     */
    private String sumBoxoffice;

    /**
     * 电影总分账票房
     */
    private String sumSplitBoxoffice;

    /**
     * 电影上映天数
     */
    private Integer releaseDays;

    /**
     * 今日票房
     */
    private BigDecimal dayBoxoffice;

    /**
     * 今日分账票房
     */
    private BigDecimal daySplitBoxoffice;

    /**
     * 今日票房占比
     */
    private String dayBoxofficeRate;

    /**
     * 今日分账票房占比
     */
    private String daySplitBoxofficeRate;

    /**
     * 当天排片率
     */
    private String dayArrangeRate;

    /**
     * 当天上座率
     */
    private String daySeatRate;

    /**
     * 数据日期
     */
    private String recordDate;


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
    public String getSumBoxoffice() {
        return sumBoxoffice;
    }

    public void setSumBoxoffice(String sumBoxoffice) {
        this.sumBoxoffice = sumBoxoffice;
    }
    public String getSumSplitBoxoffice() {
        return sumSplitBoxoffice;
    }

    public void setSumSplitBoxoffice(String sumSplitBoxoffice) {
        this.sumSplitBoxoffice = sumSplitBoxoffice;
    }
    public Integer getReleaseDays() {
        return releaseDays;
    }

    public void setReleaseDays(Integer releaseDays) {
        this.releaseDays = releaseDays;
    }
    public BigDecimal getDayBoxoffice() {
        return dayBoxoffice;
    }

    public void setDayBoxoffice(BigDecimal dayBoxoffice) {
        this.dayBoxoffice = dayBoxoffice;
    }
    public BigDecimal getDaySplitBoxoffice() {
        return daySplitBoxoffice;
    }

    public void setDaySplitBoxoffice(BigDecimal daySplitBoxoffice) {
        this.daySplitBoxoffice = daySplitBoxoffice;
    }
    public String getDayBoxofficeRate() {
        return dayBoxofficeRate;
    }

    public void setDayBoxofficeRate(String dayBoxofficeRate) {
        this.dayBoxofficeRate = dayBoxofficeRate;
    }
    public String getDaySplitBoxofficeRate() {
        return daySplitBoxofficeRate;
    }

    public void setDaySplitBoxofficeRate(String daySplitBoxofficeRate) {
        this.daySplitBoxofficeRate = daySplitBoxofficeRate;
    }
    public String getDayArrangeRate() {
        return dayArrangeRate;
    }

    public void setDayArrangeRate(String dayArrangeRate) {
        this.dayArrangeRate = dayArrangeRate;
    }
    public String getDaySeatRate() {
        return daySeatRate;
    }

    public void setDaySeatRate(String daySeatRate) {
        this.daySeatRate = daySeatRate;
    }
    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate;
    }

    @Override
    public String toString() {
        return "DailyBoxoffice{" +
            "id=" + id +
            ", movieCode=" + movieCode +
            ", movieName=" + movieName +
            ", sumBoxoffice=" + sumBoxoffice +
            ", sumSplitBoxoffice=" + sumSplitBoxoffice +
            ", releaseDays=" + releaseDays +
            ", dayBoxoffice=" + dayBoxoffice +
            ", daySplitBoxoffice=" + daySplitBoxoffice +
            ", dayBoxofficeRate=" + dayBoxofficeRate +
            ", daySplitBoxofficeRate=" + daySplitBoxofficeRate +
            ", dayArrangeRate=" + dayArrangeRate +
            ", daySeatRate=" + daySeatRate +
            ", recordDate=" + recordDate +
        "}";
    }
}
