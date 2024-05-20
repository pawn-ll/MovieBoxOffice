package com.example.movieboxoffice.entity;

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
 * @since 2024-05-20
 */
@Data
@TableName("t_movie_boxoffice")
public class MovieBoxoffice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 电影码
     */
    private Long movieCode;

    /**
     * 电影名称
     */
    private String movieName;

    /**
     * 总票房（万）
     */
    private BigDecimal sumBoxoffice;

    /**
     * 总分账票房（万）
     */
    private BigDecimal sumSplitBoxoffice;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
