package com.kh.youtube.service;

import com.kh.youtube.domain.Channel;
import com.kh.youtube.domain.Member;
import com.kh.youtube.repo.ChannelDAO;
import com.kh.youtube.repo.MemberDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MemberService {

    @Autowired
    private MemberDAO memberDAO;

    @Autowired
    private ChannelDAO channelDAO;

    public List<Member> showAll() {
        return memberDAO.findAll(); // SELECT * FROM MEMBER
    }

    // SELECT * FROM MEMBER WHERE ID=?
    public Member show(String id) {
        return memberDAO.findById(id).orElse(null);
    }

    // INSERT INTO MEMBER(ID, PASSWORD, NAME, AUTHORITY)
    // VALUES(?, ?, ?, 'ROLE_USER')
    public Member create(Member member) {
        log.info("member: "+member); // System.out.println 대신에 이걸로 출력해서 보면됨 (@Slf4j 애노테이션해주면 사용가능)
        return memberDAO.save(member);
    }

    // UPDATE MEMBER SET ID=?, PASSWORD=?, NAME=?, AUTHORITY=?
    // WHERE ID=?
    public Member update(Member member){
        Member target = memberDAO.findById(member.getId()).orElse(null);
        if(target!=null){
            return memberDAO.save(member);
        }
        return null;
    }

    // DELETE FROM MEMBER WHERE ID=?
    public Member delete(String id) {
        Member target = memberDAO.findById(id).orElse(null);
        memberDAO.delete(target);
        return target;
    }

}





























