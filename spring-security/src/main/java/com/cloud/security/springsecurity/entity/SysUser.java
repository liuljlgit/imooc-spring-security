package com.cloud.security.springsecurity.entity;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;
import com.cloud.ftl.ftlbasic.constant.PatternConst;
import com.cloud.ftl.ftlbasic.webEntity.BaseQuery;
import com.cloud.ftl.ftlbasic.enums.Opt;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.cloud.ftl.ftlbasic.annotation.PrimaryKey;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("SysUser")
public class SysUser extends BaseQuery {

	@ApiModelProperty("主键")
    @PrimaryKey
    private Long uId;

	@ApiModelProperty("真实名称")
    private String realName;

	@ApiModelProperty("用户名")
    private String userName;

	@ApiModelProperty("密码（bcrypt加密）")
    private String password;

	@ApiModelProperty("生日,格式：yyyy-MM-dd")
    @JsonFormat(pattern = PatternConst.NORM_DATETIME,timezone = PatternConst.TIMEZONE)
    private Date birthday;

	@ApiModelProperty("性别，1：男 2：女")
    private Byte sex;

	@ApiModelProperty("可用性 :true:1 false:0")
    private Byte enabled;

	@ApiModelProperty("过期性 :true:1 false:0")
    private Byte accountNonExpired;

	@ApiModelProperty("有效性 :true:1 false:0")
    private Byte credentialsNonExpired;

	@ApiModelProperty("锁定性 :true:1 false:0")
    private Byte accountNonLocked;

	@ApiModelProperty("创建时间")
    @JsonFormat(pattern = PatternConst.NORM_DATETIME,timezone = PatternConst.TIMEZONE)
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String U_ID = "u_id";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String REAL_NAME = "real_name";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String USER_NAME = "user_name";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String PASSWORD = "password";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String BIRTHDAY = "birthday";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String SEX = "sex";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String ENABLED = "enabled";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String ACCOUNT_NON_EXPIRED = "account_non_expired";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String CREDENTIALS_NON_EXPIRED = "credentials_non_expired";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String ACCOUNT_NON_LOCKED = "account_non_locked";

    @JsonIgnore
    @ApiModelProperty(hidden = true)
    public static final transient String CREATE_TIME = "create_time";


    public void andUId(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(U_ID,opt);
        } else if(values.length == 1){
            addConditGroup(U_ID,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(U_ID,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ U_ID + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andRealName(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(REAL_NAME,opt);
        } else if(values.length == 1){
            addConditGroup(REAL_NAME,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(REAL_NAME,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ REAL_NAME + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andUserName(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(USER_NAME,opt);
        } else if(values.length == 1){
            addConditGroup(USER_NAME,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(USER_NAME,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ USER_NAME + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andPassword(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(PASSWORD,opt);
        } else if(values.length == 1){
            addConditGroup(PASSWORD,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(PASSWORD,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ PASSWORD + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andBirthday(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(BIRTHDAY,opt);
        } else if(values.length == 1){
            addConditGroup(BIRTHDAY,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(BIRTHDAY,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ BIRTHDAY + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andSex(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(SEX,opt);
        } else if(values.length == 1){
            addConditGroup(SEX,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(SEX,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ SEX + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andEnabled(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(ENABLED,opt);
        } else if(values.length == 1){
            addConditGroup(ENABLED,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(ENABLED,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ ENABLED + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andAccountNonExpired(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(ACCOUNT_NON_EXPIRED,opt);
        } else if(values.length == 1){
            addConditGroup(ACCOUNT_NON_EXPIRED,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(ACCOUNT_NON_EXPIRED,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ ACCOUNT_NON_EXPIRED + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andCredentialsNonExpired(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(CREDENTIALS_NON_EXPIRED,opt);
        } else if(values.length == 1){
            addConditGroup(CREDENTIALS_NON_EXPIRED,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(CREDENTIALS_NON_EXPIRED,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ CREDENTIALS_NON_EXPIRED + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andAccountNonLocked(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(ACCOUNT_NON_LOCKED,opt);
        } else if(values.length == 1){
            addConditGroup(ACCOUNT_NON_LOCKED,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(ACCOUNT_NON_LOCKED,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ ACCOUNT_NON_LOCKED + "’ 的SQL入参个数不正确 ");
        }
    }

    public void andCreateTime(Opt opt,Object... values) {
        if(values.length == 0){
            addConditGroup(CREATE_TIME,opt);
        } else if(values.length == 1){
            addConditGroup(CREATE_TIME,opt,values[0]);
        } else if(values.length == 2){
            addConditGroup(CREATE_TIME,opt,values[0],values[1]);
        } else {
            throw new RuntimeException("‘"+ CREATE_TIME + "’ 的SQL入参个数不正确 ");
        }
    }

}