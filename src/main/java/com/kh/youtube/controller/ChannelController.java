package com.kh.youtube.controller;

import com.kh.youtube.domain.Channel;
import com.kh.youtube.domain.Member;
import com.kh.youtube.domain.Subscribe;
import com.kh.youtube.domain.Video;
import com.kh.youtube.repo.ChannelDAO;
import com.kh.youtube.service.ChannelService;
import com.kh.youtube.service.SubscribeService;
import com.kh.youtube.service.VideoService;
import jakarta.persistence.Column;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/*")
@Log4j2
@CrossOrigin(origins={"*"}, maxAge = 6000)
public class ChannelController {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Autowired
    private ChannelService channel;

    @Autowired
    private VideoService video;

    @Autowired
    private SubscribeService subscribe;

    // 채널 조회 : GET -  http://localhost:8080/api/channel/1
    @GetMapping("/channel/{id}")
    public ResponseEntity<Channel> showChannel(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(channel.show(id));
    }
    
    // 채널에 있는 영상 조회 : GET - http://localhost:8080/api/channel/1/video
    @GetMapping("/channel/{code}/video")
    public ResponseEntity<List<Video>> channelVideoList(@PathVariable int code){
        return ResponseEntity.status(HttpStatus.OK).body(video.findByChannelCode(code));
    }

    // 채널 추가 : POST - http://localhost:8080/api/channel
    @PostMapping("/channel")
    public ResponseEntity<Channel> createChannel(String name, String desc, MultipartFile photo) {
        log.info("name : " + name );
        log.info("desc : " + desc );
        log.info("photo " + photo );

        // 업로드 처리
        // 이미지의 실제 파일 이름
        String originalPhoto = photo.getOriginalFilename();
        String filePhoto = originalPhoto.substring(originalPhoto.lastIndexOf("\\")+1);
        String uuid = UUID.randomUUID().toString();

        // 실제로 저장할 파일명(위치 포함)
        String savePhoto = uploadPath + File.separator + uuid + "_" + filePhoto;
        Path pathPhoto = Paths.get(savePhoto);

        try {
            photo.transferTo(pathPhoto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 파일 업로드가 끝났으니 경로(savePhoto), name, desc, memberId(id)
        Channel vo = new Channel();
        vo.setChannelPhoto(savePhoto);
        vo.setChannelName(name);
        vo.setChannelDesc(desc);

        Member member = new Member();
        member.setId("user1");
        vo.setMember(member);

//        return ResponseEntity.status(HttpStatus.OK).build();
          return ResponseEntity.status(HttpStatus.OK).body(channel.create(vo));
    }
    
    // 채널 수정 : PUT - http://localhost:8080/api/channel
    @PutMapping("/channel")
    public ResponseEntity<Channel> updateChannel(@RequestBody Channel vo) {
        return ResponseEntity.status(HttpStatus.OK).body(channel.update(vo));
    }
    
    // 채널 삭제 : DELETE - http://localhost:8080/api/channel/1
    @DeleteMapping("/channel/{id}")
    public ResponseEntity<Channel> deleteChannel(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(channel.delete(id));
    }
    
    // 내가 구독한 채널 조회 : GET - http://localhost:8080/api/subscribe/user1
    @GetMapping("/subscribe/{user}")
    public ResponseEntity<List<Subscribe>> subscribeList(@PathVariable String user) {
        return ResponseEntity.status(HttpStatus.OK).body(subscribe.findByMemberId(user));
    }

    // 채널 구독 : POST - http://localhost:8080/api/subscribe
    @PostMapping("/subscribe")
    public ResponseEntity<Subscribe> createSubscribe(@RequestBody Subscribe vo) {
        return ResponseEntity.status(HttpStatus.OK).body(subscribe.create(vo));
    }
    
    // 채널 구독 취소 : DELETE - http://localhost:8080/api/subscribe/1
    @DeleteMapping("/subscribe/{id}")
    public ResponseEntity<Subscribe> deleteSubscribe(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(subscribe.delete(id));
    }

}

























