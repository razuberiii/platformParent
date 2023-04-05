package com.platform.dao;

import com.platform.dto.LoginDto;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LoginMapper {

    @Select("Select * from user_info where phone =#{phone}")
    LoginDto findUserByPhone(String phone);

    @Update("update user_info set ipAddress=#{ipAddress} , time=#{time} where uid=#{uid}")
    void updateIpAndTime(@Param("ipAddress") String ipAddress, @Param("time")String time, @Param("uid")Integer uid);

    @Update("update user_info set password=#{password},status=#{status} where uid=#{uid}")
    void updatePassword(@Param("password") String password, @Param("status") String status,@Param("uid")Integer uid);

    @Select("select * from user_info")
    LoginDto queryUserPasswordInfo();

    @Update("update user_info set count=#{count},${passwordName}=#{password} where uid=#{uid}")
    void updateCountAndLastPassword(@Param("count") Integer count,@Param("password") String password,@Param("passwordName") String passwordName,@Param("uid")Integer uid);
}
