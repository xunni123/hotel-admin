package com.xunni.hotel.web.controller;

import com.xunni.hotel.common.result.Result;
import com.xunni.hotel.entity.Member;
import com.xunni.hotel.web.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/list")
    public Result getMemberList() {
        List<Member> members = memberService.list();
        return Result.success(members);
    }

    @PostMapping("/add")
    public Result addMember(@RequestBody Member member) {
        boolean success = memberService.save(member);
        return success ? Result.success(member) : Result.error("添加会员失败");
    }

    @PutMapping("/update/{memberId}")
    public Result updateMember(@PathVariable Integer memberId, @RequestBody Member member) {
        member.setMemberId(memberId);
        boolean success = memberService.updateById(member);
        return success ? Result.success(member) : Result.error("更新会员失败");
    }
}