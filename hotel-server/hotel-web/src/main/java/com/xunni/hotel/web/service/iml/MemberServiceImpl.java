package com.xunni.hotel.web.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xunni.hotel.entity.Member;
import com.xunni.hotel.web.mapper.MemberMapper;
import com.xunni.hotel.web.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
}