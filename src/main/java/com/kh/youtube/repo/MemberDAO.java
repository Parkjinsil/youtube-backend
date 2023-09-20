package com.kh.youtube.repo;

import com.kh.youtube.domain.Channel;
import com.kh.youtube.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberDAO extends JpaRepository<Member, String> {
}