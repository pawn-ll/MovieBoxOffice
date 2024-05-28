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
 * @since 2024-05-13
 */
@TableName("t_second_do")
@Data
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
     * 手动填写的详情url
     */
    private String detailUrl;

    /**
     * 创建时间
     */
    private Date createTime;


}
