package com.cloud.security.springsecurity.security.modular.authen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Date;

@ApiModel("用户自定义登录用户信息实体")
public class SecurityUserDetails extends User {

    @ApiModelProperty("uid")
    private Long uid;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("生日")
    private Date birthday;

    @ApiModelProperty("性别。1：男，2：女")
    private String sex;

    @ApiModelProperty("创建时间")
    private Date createTime;


    public SecurityUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public SecurityUserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
                               boolean credentialsNonExpired, boolean accountNonLocked,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);
    }

    public SecurityUserDetails(Long uid, String realName, Date birthday, String sex, Date createTime,
                               String username, String password, boolean enabled, boolean accountNonExpired,
                               boolean credentialsNonExpired, boolean accountNonLocked,
                               Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);
        this.uid = uid;
        this.realName = realName;
        this.birthday = birthday;
        this.sex = sex;
        this.createTime = createTime;
    }


    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
