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
 * @since 2024-06-18
 */
@TableName("t_site_visitor_count")
public class SiteVisitorCount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 站点名
     */
    private String siteName;

    /**
     * 站点访问数
     */
    private Integer siteVisitorCount;

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
    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    public Integer getSiteVisitorCount() {
        return siteVisitorCount;
    }

    public void setSiteVisitorCount(Integer siteVisitorCount) {
        this.siteVisitorCount = siteVisitorCount;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "SiteVisitorCount{" +
            "id=" + id +
            ", siteName=" + siteName +
            ", siteVisitorCount=" + siteVisitorCount +
            ", createTime=" + createTime +
        "}";
    }
}
