package com.exp.fluent.entity;

import cn.org.atool.fluent.mybatis.annotation.FluentMybatis;
import cn.org.atool.fluent.mybatis.annotation.LogicDelete;
import cn.org.atool.fluent.mybatis.annotation.TableField;
import cn.org.atool.fluent.mybatis.annotation.TableId;
import cn.org.atool.fluent.mybatis.base.RichEntity;
import com.exp.fluent.constant.TokenType;
import com.exp.fluent.defaults.IEntityDefault;
import com.exp.fluent.defaults.IEntityDefaultSetter;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.EnumTypeHandler;

/**
 * User: 数据映射实体定义
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
    table = "t_user",
    schema = "exprass",
    defaults = IEntityDefaultSetter.class,
    suffix = ""
)
public class User extends RichEntity implements IEntityDefault {
  private static final long serialVersionUID = 1L;

  /**
   */
  @TableId("id")
  private Integer id;

  /**
   * 是否逻辑删除：1-是，0-否
   */
  @TableField(
      value = "logic_deleted",
      insert = "0"
  )
  @LogicDelete
  private Boolean logicDeleted;

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
   * 账号（登录名）
   */
  @TableField("username")
  private String username;

  /**
   * 密码md5
   */
  @TableField("passwd")
  private String passwd;

  /**
   * 第三方登录凭据，token
   */
  @TableField("token")
  private String token;

  /**
   * 第三方登录渠道，如微信
   */
  @TableField(
      value = "token_type",
      typeHandler = EnumTypeHandler.class
  )
  private TokenType tokenType;

  /**
   * 用户名（显示名称）
   */
  @TableField("name")
  private String name;

  /**
   * 是否被锁定
   */
  @TableField("locked")
  private Boolean locked;

  @Override
  public final Class entityClass() {
    return User.class;
  }
}
