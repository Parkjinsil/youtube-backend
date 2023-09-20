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
public class ChannelService {

    @Autowired
    private ChannelDAO channelDAO;

    @Autowired
    private MemberDAO memberDAO;

    public List<Channel> showAll() {
        return channelDAO.findAll();
    }

    public Channel show(int id){
        Channel channel = channelDAO.findById(id).orElse(null);
        Member member = memberDAO.findById(channel.getMember().getId()).orElse(null);
        channel.setMember(member); // Channel에는 Member의 프라이머리키 값만 가지고있으므로 나머지 값도 다 담아준다
        return channel;
    }

    public Channel create(Channel channel) {
        return channelDAO.save(channel);
    }

    public Channel update(Channel channel) {
        Channel target = channelDAO.findById(channel.getChannelCode()).orElse(null);
        if(target!=null) {
            return channelDAO.save(channel);
        }
        return null;
    }

    public Channel delete(int id){
        Channel target = channelDAO.findById(id).orElse(null);
        channelDAO.delete(target);
        return target;
    }

    // 특정 멤버의 모든 채널 조회
    public List<Channel> showMember(String id) {
        return channelDAO.findByMemberId(id);
    }

}



























