package com.sugaram.controller;

import com.sugaram.entity.Attachment;
import com.sugaram.entity.Post;
import com.sugaram.repository.AttachmentRepository;
import com.sugaram.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UploadController {
    private final AttachmentRepository attachmentRepository;
    private final PostRepository postRepository;
    String mainPath = "/home/";
    @SneakyThrows
    @PostMapping("/upload")
    public ResponseEntity<?> uploading(Long postId, MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String id = UUID.randomUUID().toString();

        String fileName = mainPath + id + originalFilename.substring(originalFilename.lastIndexOf("."));
        Path path = Path.of(fileName);
        Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        Attachment attachment =
                Attachment
                        .builder()
                        .id(id)
                        .fullyName(fileName)
                        .originalName(originalFilename)
                        .size(multipartFile.getSize())
                        .build();
        attachmentRepository.save(attachment);

        Post post = postRepository.findByIdAndDisabled(postId, false).orElseThrow();
        post.setImageUrl(path.toString());
postRepository.save(post);

        System.out.println("uploaded");
        return ResponseEntity.ok("uploaded");
    }
}

