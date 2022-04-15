package com.exp.fluent.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.RefMethod;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import com.exp.fluent.defaults.IEntityDefault;
import com.exp.fluent.defaults.IEntityDefaultSetter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * File: 数据映射实体定义
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
    table = "t_file",
    schema = "exprass",
    defaults = IEntityDefaultSetter.class,
    suffix = ""
)
public class File extends RichEntity implements IEntityDefault {
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
   * 原始文件名
   */
  @TableField("original_name")
  private String originalName;

  /**
   * 文件扩展名，文件类型
   */
  @TableField("file_ext")
  private String fileExt;

  /**
   * 文件大小，单位byte
   */
  @TableField("file_size")
  private Long fileSize;

  /**
   * 文件MD5值
   */
  @TableField("md5")
  private String md5;

  /**
   * 文件存储名
   */
  @TableField("storage_name")
  private String storageName;

  /**
   * 文件存储路径
   */
  @TableField("storage_path")
  private String storagePath;

  @Override
  public final Class entityClass() {
    return File.class;
  }

  /**
   * 实现 {@link com.exp.fluent.IEntityRelation#findThumbnailOfFile(File)}
   */
  @RefMethod("logicalDeleted = logicalDeleted && fileId = id")
  public Thumbnail findThumbnail() {
    return super.invoke("findThumbnail", true);
  }
}
