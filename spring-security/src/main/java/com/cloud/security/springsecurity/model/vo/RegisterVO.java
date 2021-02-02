package com.cloud.security.springsecurity.model.vo;

import com.cloud.ftl.ftlbasic.constant.PatternConst;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("注册对象")
public class RegisterVO {

    @ApiModelProperty("真实名称")
    private String realName;

    @ApiModelProperty("用户名")
    @NotEmpty(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty("密码，RSA加密")
    @NotEmpty(message = "密码不能为空")
    private String password;

    @ApiModelProperty("生日,格式：yyyy-MM-dd")
    @JsonFormat(pattern = PatternConst.NORM_DATE,timezone = PatternConst.TIMEZONE)
    private Date birthday;

    @ApiModelProperty("性别，1：男 2：女")
    @Pattern(regexp = "^[1|2]$",message = "请输入正确的参数")
    private Byte sex;

    @ApiModelProperty("可用性 :true:1 false:0")
    @Pattern(regexp = "^[0|1]$",message = "请输入正确的参数")
    private Byte enabled;

    @ApiModelProperty("过期性 :true:1 false:0")
    @Pattern(regexp = "^[0|1]$",message = "请输入正确的参数")
    private Byte accountNonExpired;

    @ApiModelProperty("有效性 :true:1 false:0")
    @Pattern(regexp = "^[0|1]$",message = "请输入正确的参数")
    private Byte credentialsNonExpired;

    @ApiModelProperty("锁定性 :true:1 false:0")
    @Pattern(regexp = "^[0|1]$",message = "请输入正确的参数")
    private Byte accountNonLocked;

}
