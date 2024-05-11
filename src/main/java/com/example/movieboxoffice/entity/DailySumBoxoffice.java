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
 * @since 2024-05-07
 */
@TableName("t_daily_sum_boxoffice")
public class DailySumBoxoffice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日期
     */
    private String date;

    /**
     * 当日总票房
     */
    private String sumBoxoffice;

    /**
     * 当日总分账票房
     */
    private String sumSplitBoxoffice;

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
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DailySumBoxoffice{" +
            "id=" + id +
            ", date=" + date +
            ", sumBoxoffice=" + sumBoxoffice +
            ", sumSplitBoxoffice=" + sumSplitBoxoffice +
            ", createTime=" + createTime +
        "}";
    }
}
