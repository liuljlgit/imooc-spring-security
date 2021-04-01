package com.cloud.security.springsecurity.dao;

import org.springframework.stereotype.Repository;
import java.util.List;
import com.cloud.ftl.ftlbasic.mapper.IBaseMapper;
import org.apache.ibatis.annotations.Param;
import com.cloud.security.springsecurity.entity.SysUser;

/**
  * 接口类 ISysUserDao
  * @author lijun
  */
@Repository
public interface ISysUserDao extends IBaseMapper<SysUser>{

    //------------------------ custom code begin ------------------------//
        
    //======================== custom code end ========================//

}