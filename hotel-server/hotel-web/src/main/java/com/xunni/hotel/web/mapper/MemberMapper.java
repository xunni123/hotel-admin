package com.xunni.hotel.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xunni.hotel.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {
}