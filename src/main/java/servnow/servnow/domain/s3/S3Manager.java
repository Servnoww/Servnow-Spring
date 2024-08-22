package servnow.servnow.domain.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import servnow.servnow.domain.config.S3Config;
import servnow.servnow.domain.s3.uuid.model.Uuid;
import servnow.servnow.domain.s3.uuid.repository.UuidRepository;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Manager {

    private final AmazonS3 amazonS3;

    private final S3Config s3Config;

    private final UuidRepository uuidRepository;

    public String uploadFile(String keyName, MultipartFile file){
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());

        // 파일의 Content-Type 설정
        metadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(new PutObjectRequest(s3Config.getBucket(), keyName, file.getInputStream(), metadata));
        }catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(s3Config.getBucket(), keyName).toString();
    }

    public String generateImage(Uuid savedUuid) {
        return s3Config.getPath() + '/' + savedUuid.getUuid();
    }
}