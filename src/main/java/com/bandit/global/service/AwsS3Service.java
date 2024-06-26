package com.bandit.global.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.bandit.presentation.payload.code.ErrorStatus;
import com.bandit.presentation.payload.exception.ImageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AwsS3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<String> uploadFiles(List<MultipartFile> multipartFiles) {
        return multipartFiles.stream()
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    public String uploadFile(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다.");
        }

        return fileName;
    }

    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    public String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며, 파일 타입과 상관없이 업로드할 수 있게 하기위해, "."의 존재 유무만 판단하였습니다.
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일" + fileName + ") 입니다.");
        }
    }


    public void deleteFile(String fileName) {
        try {
            if (amazonS3.doesObjectExist(bucket, fileName)) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
            }
        } catch (SdkClientException e) {
            throw new ImageHandler(ErrorStatus.IMAGE_REQUEST_IS_EMPTY);
        }
    }

    public List<String> getImgFileList() {
        ListObjectsV2Result result = amazonS3.listObjectsV2(bucket);
        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }
}
