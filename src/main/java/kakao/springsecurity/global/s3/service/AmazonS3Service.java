package kakao.springsecurity.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import kakao.springsecurity.domain.petTalk.entity.PetTalk;
import kakao.springsecurity.domain.petTalk.entity.PetTalkPhoto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    private static final String DATE_FORMAT = "yyyy/MM/dd/";
    private static final String PET_TALK_PHOTO_PREFIX = "pet_talk/";

    @Transactional
    public List<PetTalkPhoto> uploadPetTalkPhotos(MultipartFile[] files, PetTalk petTalk) {
        List<PetTalkPhoto> petTalkPhotos = new ArrayList<>();

        try {
            log.info("[펫톡] 이미지 업로드 진행");
            for (MultipartFile file : files) {
                String fileKey = PET_TALK_PHOTO_PREFIX + createDatePath() + generateRandomFileName();
                ObjectMetadata metadata = createObjectMetadataFromFile(file);
                amazonS3.putObject(bucket, fileKey, file.getInputStream(), metadata);

                petTalkPhotos.add(PetTalkPhoto.builder()
                        .name(file.getOriginalFilename())
                        .url(getUrlFromBucket(fileKey))
                        .petTalk(petTalk)
                        .build());
            }


        } catch (Exception e) {
            log.error("[펫톡] 이미지 업로드 중 오류 발생 : " + e.getMessage());
        }

        log.info("[펫톡] 이미지 업로드 완료");
        return petTalkPhotos;
    }

    private String generateRandomFileName() {
        return UUID.randomUUID().toString();
    }

    private String createDatePath() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return now.format(dateTimeFormatter);
    }

    private ObjectMetadata createObjectMetadataFromFile(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        return metadata;
    }

    private String getUrlFromBucket(String fileKey) {
        return amazonS3.getUrl(bucket, fileKey).toString();
    }

}
