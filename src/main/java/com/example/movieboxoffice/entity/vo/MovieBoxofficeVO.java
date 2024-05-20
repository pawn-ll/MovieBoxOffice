package com.example.movieboxoffice.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author baomidou
 * @since 2024-05-20
 */
@Data
public class MovieBoxofficeVO implements Serializable {

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


}
