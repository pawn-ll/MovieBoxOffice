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
 * @since 2024-06-04
 */
@TableName("t_ip")
@Data
public class Ip implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 0-黑名单 1-白名单
     */
    private Integer isWhite;

    /**
     * 创建时间
     */
    private Date createTime;


}
