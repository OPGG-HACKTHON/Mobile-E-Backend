package opgg.backend.gmakersserver.infra.aws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import opgg.backend.gmakersserver.error.exception.s3.NotImageFileException;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

	private final AmazonS3Client amazonS3Client;
	private static final String IMAGE = "image";
	private static final String PROPERTY_NAME = "user.home";

	@Value("${cloud.aws.s3.bucket}")
	public String bucket;

	// S3로 파일 업로드하기
	public String upload(String dirName, MultipartFile multipartFile) throws IOException {
		if (!multipartFile.getContentType().startsWith(IMAGE)) {
			throw new NotImageFileException();
		}
		String originalFilename = multipartFile.getOriginalFilename();
		String fileName = dirName + "/" + UUID.randomUUID() + "_" + originalFilename;
		return putS3(fileName, multipartFile);
	}

	// S3로 업로드
	private String putS3(String fileName, MultipartFile uploadFile) throws IOException {
		File file = convert(uploadFile);
		amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(CannedAccessControlList.PublicRead));
		removeNewFile(file);
		return amazonS3Client.getUrl(bucket, fileName).toString();
	}

	// 로컬에 저장된 이미지 지우기
	private void removeNewFile(File targetFile) {
		if (targetFile.delete()) {
			log.info("File delete success");
			return;
		}
		log.info("File delete fail");
	}

	private File convert(MultipartFile multipartFile) throws IOException {
		File convertFile = new File(System.getProperty(PROPERTY_NAME) + File.separator + System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename());
		if (convertFile.createNewFile()) {
			try (FileOutputStream fos = new FileOutputStream(convertFile)) {
				fos.write(multipartFile.getBytes());
			}
		}
		return convertFile;
	}
}

