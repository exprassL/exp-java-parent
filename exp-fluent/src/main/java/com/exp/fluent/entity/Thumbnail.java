package com.exp.fluent.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import com.exp.fluent.defaults.IEntityDefault;
import com.exp.fluent.defaults.IEntityDefaultSetter;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Thumbnail: 数据映射实体定义
 *
 * @author Powered By Fluent Mybatis
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Data
@Accessors(
    chain = true
)
@EqualsAndHashCode(
    callSuper = false
)
@FluentMybatis(
    table = "t_thumbnail",
    schema = "exprass",
    defaults = IEntityDefaultSetter.class,
    suffix = ""
)
public class Thumbnail extends RichEntity implements IEntityDefault {
  private static final long serialVersionUID = 1L;

  /**
   */
  @TableId("id")
  private Integer id;

  /**
   * 是否逻辑删除：1-是，0-否
   */
  @TableField("logical_deleted")
  private Boolean logicalDeleted;

  /**
   * 创建人
   */
  @TableField("created_by")
  private Integer createdBy;

  /**
   * 创建时间
   */
  @TableField(
      value = "created_on",
      insert = "now()"
  )
  private Date createdOn;

  /**
   * 最后更新人，初始为创建人
   */
  @TableField("last_updated_by")
  private Integer lastUpdatedBy;

  /**
   * 最后更新时间，初始为创建时间
   */
  @TableField(
      value = "last_updated_on",
      insert = "now()",
      update = "now()"
  )
  private Date lastUpdatedOn;

  /**
   * 环境关键字
   */
  @TableField("env")
  private String env;

  /**
   * 租户
   */
  @TableField("tenant")
  private String tenant;

  /**
   * 原始图片文件id
   */
  @TableField("file_id")
  private Integer fileId;

  /**
   * 缩略图大小，单位byte
   */
  @TableField("thumb_size")
  private Integer thumbSize;

  /**
   * 文件存储路径
   */
  @TableField("storage_path")
  private String storagePath;

  @Override
  public final Class entityClass() {
    return Thumbnail.class;
  }
}
