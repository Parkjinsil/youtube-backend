package com.kh.youtube.controller;

import com.kh.youtube.domain.*;
import com.kh.youtube.service.VideoCommentService;
import com.kh.youtube.service.VideoService;
import lombok.extern.log4j.Log4j2;
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
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/*")
@Log4j2
@CrossOrigin(origins={"*"}, maxAge = 6000)
public class VideoController {

    @Value("${spring.servlet.multipart.location}") // application.properties에 있는 변수
    private String uploadPath;

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoCommentService comment;

    // 영상 전체 조회 : GET - http://localhost:8080/api/video
    @GetMapping("/video")
    public ResponseEntity<List<Video>> videoList(){
        return ResponseEntity.status(HttpStatus.OK).body(videoService.showAll());
    }

    // 영상 추가 : POST - http://localhost:8080/api/video
    @PostMapping("/video")
    public ResponseEntity<Video> createVideo(MultipartFile video, MultipartFile image, String title, String desc, String categoryCode) {
        // 가져와야 하는 값 == video_title, video_desc, video_url, video_photo, category_code
        log.info("video : " + video );
        log.info("image : " + image );
        log.info("title : " + title );
        log.info("desc : " + desc );
        log.info("categoryCode : " + categoryCode );

        // 업로드 처리
        // 비디오의 실제 파일 이름
        String originalVideo = video.getOriginalFilename();
        String realVideo = originalVideo.substring(originalVideo.lastIndexOf("\\")+1);
        log.info("realVideo : " + realVideo);

        // 이미지의 실제 파일 이름
        String originalImage = image.getOriginalFilename();
        String realImage = originalImage.substring(originalImage.lastIndexOf("\\")+1);

        // UUID
        String uuid = UUID.randomUUID().toString();

        // 실제로 저장할 파일명 (위치 포함)
        String saveVideo = uploadPath + File.separator + uuid + "_" + realVideo;
        Path pathVideo = Paths.get(saveVideo);

        String saveImage = uploadPath + File.separator + uuid + "_" + realImage;
        Path pathImage = Paths.get(saveImage);
        try {
            video.transferTo(pathVideo);
            image.transferTo(pathImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Video vo = new Video();
        vo.setVideoTitle(title);
        vo.setVideoDesc(desc);
        vo.setVideoUrl(saveVideo);
        vo.setVideoPhoto(saveImage);
        Category category = new Category();
        category.setCategoryCode(Integer.parseInt(categoryCode));
        vo.setCategory(category);
        Channel channel = new Channel();
        channel.setChannelCode(21);
        vo.setChannel(channel);
        Member member = new Member();
        member.setId("user1");
        vo.setMember(member);

//        return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.status(HttpStatus.OK).body(videoService.create(vo));
    }


    // 영상 수정 : PUT - http://localhost:8080/api/video
    @PutMapping("/video")
    public ResponseEntity<Video> updateVideo(@RequestBody Video vo) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.update(vo));
    }

    // 영상 삭제 : DELETE - http://localhost:8080/api/video/1
    @DeleteMapping("/video/{id}")
    public ResponseEntity<Video> deleteVideo(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.delete(id));
    }

    // 영상 1개 조회 : GET - http://localhost:8080/api/video/1
    @GetMapping("/video/{id}")
    public ResponseEntity<Video> showVideo(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(videoService.show(id));
    }

    // 영상 1개에 따른 댓글 조회 : GET - http://localhost:8080/api/video/1/comment
    @GetMapping("/video/{id}/comment")
    public ResponseEntity<List<VideoComment>> videoCommentList(@PathVariable int id){
        return ResponseEntity.status(HttpStatus.OK).body(comment.findByVideoCode(id));
    }

    // 좋아요 추가 : POST - http://localhost:8080/api/video/like
//    @PostMapping("/video/like")
//    public ResponseEntity<VideoLike>

    // 좋아요 취소 : DELETE - http://localhost:8080/api/video/like/1

    // 댓글 추가 : POST - http://localhost:8080/api/video/comment

    // 댓글 수정 : PUT - http://localhost:8080/api/video/comment

    // 댓글 삭제 : DELETE - http://localhost:8080/api/video/comment/1

    // 댓글 좋아요 추가 : POST - http://localhost:8080/api/video/comment/like

    // 댓글 좋아요 취소 : DELETE - http://localhost:8080/api/video/comment/like/1

}
