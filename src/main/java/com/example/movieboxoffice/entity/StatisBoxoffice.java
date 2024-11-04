package com.example.movieboxoffice.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-06-05
 */
@TableName("t_statis_boxoffice")
@Data
public class StatisBoxoffice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelIgnore
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电影编码
     */
    @ExcelIgnore
    private Long movieCode;

    /**
     * 电影名称
     */
    @ExcelProperty("电影名称")
    private String movieName;

    /**
     * 统计总票房
     */
    @ExcelProperty("统计总票房（万）")
    private BigDecimal statisSumBoxoffice;

    /**
     * 平均票房占比
     */
    @ExcelProperty("平均票房占比")
    private String avgBoxofficeRate;

    /**
     * 平均排片率
     */
    @ExcelProperty("平均排片率")
    private String avgArrangeRate;

    /**
     * 平均入座率
     */
    @ExcelProperty("平均入座率")
    private String avgSeatRate;

    /**
     * 1-月 2-年
     */
    @ExcelIgnore
    private Integer statisType;

    /**
     * 统计区间（2024-01 or 2024）
     */
    @ExcelProperty("统计区间")
    private String statisInterval;

    /**
     * 创建时间
     */
    @ExcelIgnore
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelIgnore
    private Date updateTime;


}
