package com.example.movieboxoffice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.movieboxoffice.entity.DailyBoxoffice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2024-05-07
 */
@Mapper
public interface DailyBoxofficeMapper extends BaseMapper<DailyBoxoffice> {

    Integer verifyCode(@Param("oldCode") Long oldCode , @Param("newCode") Long newCode);
}
