package com.example.web_2.util;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.UUID;

@Service
public class S3Service {
    private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
    private static final String KEY_ID = System.getenv("KEY_ID");
    private static final String KEY_SECRET = System.getenv("KEY_SECRET");

    public String load(MultipartFile picture, String userId) {
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                        new com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration(
                                "https://storage.yandexcloud.net", "ru-central1"
                        ))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(KEY_ID, KEY_SECRET)))
                .build();
        try {
            s3Client.deleteObject(BUCKET_NAME, userId);
            byte[] bytes = picture.getBytes();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bytes.length);
            s3Client.putObject(BUCKET_NAME, userId, new ByteArrayInputStream(bytes), metadata);
            return s3Client.getUrl(BUCKET_NAME, userId).toExternalForm();
        } catch (Exception e) {
            throw new AmazonS3Exception(e.getMessage());
        }
    }
}
