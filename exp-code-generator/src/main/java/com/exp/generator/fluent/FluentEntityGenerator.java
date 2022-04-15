package com.exp.generator.fluent;

import cn.org.atool.generator.FileGenerator;
import cn.org.atool.generator.annotation.*;
import com.exp.fluent.constant.TokenType;
import com.exp.fluent.defaults.IEntityDefault;
import com.exp.fluent.defaults.IEntityDefaultSetter;
import org.apache.ibatis.type.EnumTypeHandler;

public class FluentEntityGenerator {
    
    private static final String URL = "jdbc:mysql://localhost:3306/exprass" +
            "?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&allowMultiQueries=true";
    
    public static void main(String[] args) {
        FileGenerator.build(Empty.class);
    }
    
    @Tables(
            // 设置数据库连接信息
            url = URL, username = "exp", password = "exprass",
            // 设置entity类生成src目录, 相对于 user.dir
            srcDir = "exp-fluent/src/main/java",
            // 设置entity类的package值
            basePack = "com.exp.fluent",
            // 设置dao接口和实现的src目录, 相对于 user.dir
            daoDir = "exp-fluent/src/main/java",
            // 生成entity时，忽略表前缀
            tablePrefix = "t_",
            // 实体类名后缀，默认为Entity，现取消后缀
            entitySuffix = "",
            // 不按字母顺序定义entity字段，而是按数据库字段定义顺序
            alphabetOrder = false,
            // 逻辑删除字段，创建时间、更新时间字段
            logicDeleted = "logic_deleted", gmtCreated = "created_on", gmtModified = "last_updated_on",
            // 设置哪些表要生成Entity文件
            tables = {
                    @Table(value = "t_user",
                            entity = IEntityDefault.class, defaults = IEntityDefaultSetter.class,
                            columns = @Column(
                                    value = "token_type",
                                    javaType = TokenType.class,
                                    typeHandler = EnumTypeHandler.class)),
                    @Table(value = {"t_file", "t_album", "t_thumbnail", "t_album_file"},
                            entity = IEntityDefault.class, defaults = IEntityDefaultSetter.class)
            },
            relations = {
                    @Relation(source = "t_file", target = "t_thumbnail",
                            where = "id=file_id&logical_deleted=logical_deleted", type = RelationType.OneWay_0_1),
                    @Relation(source = "t_album", target = "t_file",
                           type = RelationType.OneWay_0_N)
            }
    )
    static class Empty {
    }
}