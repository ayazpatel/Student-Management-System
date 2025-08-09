package com.ayaz.studentmanagementsystem.service;

import com.ayaz.studentmanagementsystem.exception.FileUploadException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

// MinIO Specific Exceptions
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.MinioException;
import io.minio.errors.XmlParserException; // Often caught by the general MinioException but good to know
import io.minio.errors.InvalidResponseException; // Same as above

// Standard Java I/O Exception
import java.io.IOException;

// Standard Java Security Exceptions
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class MinioService {

    private static final Logger logger = LoggerFactory.getLogger(MinioService.class);

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private  String bucketName;

    public void uploadFile(MultipartFile file, String objectName) throws FileUploadException {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            logger.info("File '{}' uploaded successfully to bucket '{}'.", objectName, bucketName);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            logger.error("Error while uploading file '{}' to Minio: {}", objectName, e.getMessage());
            throw new FileUploadException("Failed to upload file to storage.", e);
        }
    }

//    public String getPresignedUrl(String objectName) {
//        try {
//            return  minioClient.getPresignedObjectUrl(
//                    GetPresignedObjectUrlArgs.builder()
//                            .method(Method.GET)
//                            .bucket(bucketName)
//                            .object(objectName)
//                            .expiry(7, TimeUnit.DAYS)
//                            .build()
//            );
//        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
//            logger.error("Error generating presigned URL for object '{}':'{}'", objectName, e.getMessage());
//            return null;
//        }
//    }

    public String getPresignedUrl(String objectName) {
        try {
            // Add a response-content-type parameter to the presigned URL.
            // This forces the browser to render the content instead of downloading it.
            Map<String, String> queryParams = new HashMap<>();
            String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;

            if (objectName != null) {
                if (objectName.toLowerCase().endsWith(".jpg") || objectName.toLowerCase().endsWith(".jpeg")) {
                    contentType = MediaType.IMAGE_JPEG_VALUE;
                } else if (objectName.toLowerCase().endsWith(".png")) {
                    contentType = MediaType.IMAGE_PNG_VALUE;
                }
            }
            queryParams.put("response-content-type", contentType);

            return  minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(24, TimeUnit.HOURS) // 24 hours is a more common and secure expiry
                            .extraQueryParams(queryParams)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            logger.error("Error generating presigned URL for object '{}': {}", objectName, e.getMessage());
            return null;
        }
    }

    public void deleteFile(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            logger.error("Error deleting object '{}' from bucket '{}': {}", objectName, bucketName, e.getMessage());
        }
    }


//    public String uploadAndGetPresignedUrl(MultipartFile file, String objectName) throws FileUploadException {
//        try {
//            // 1. Upload the object
//            minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object(objectName)
//                            .stream(file.getInputStream(), file.getSize(), -1)
//                            .contentType(file.getContentType())
//                            .build()
//            );
//            // 2. Get the presigned URL
//            String url = minioClient.getPresignedObjectUrl(
//                    GetPresignedObjectUrlArgs.builder()
//                            .method(Method.GET)
//                            .bucket(bucketName)
//                            .object(objectName)
////                            .expiry(24*1000, TimeUnit.HOURS) // 1000 Days Expiry
////                            .expiry(365*100, TimeUnit.DAYS)
//                            .expiry(7, TimeUnit.DAYS)
//                            .build()
//            );
//
//            return url; // Success!
//        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
//            logger.error("Error while uploading file to Minio", e);
//            throw new FileUploadException("Failed to upload file to storage.", e);
//        }
//    }

//    /**
//     * Deletes a file by parsing its full URL.
//     *
//     * @param objectUrl The full URL of the object to delete.
//     * @throws MinioException if an error occurs on the MinIO server.
//     * @throws IOException for network or parsing errors.
//     * @throws NoSuchAlgorithmException if a required crypto algorithm is missing.
//     * @throws InvalidKeyException if the access/secret key is invalid.
//     */
//    public void deleteFileByUrl(String objectUrl) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
//
//
//        try {
//            // 1. Parse the URL to get the path
//            URL url = new URL(objectUrl);
//            String path = url.getPath(); // This will be "/my-springboot-bucket/path/to/gfghkjdg-image.png"
//
//            // 2. Extract bucket and object names from the path
//            // The path starts with a '/', so we remove it.
//            String fullObjectPath = path.substring(1);
//
//            // The first part is the bucket name
//            String bucketName = fullObjectPath.substring(0, fullObjectPath.indexOf('/'));
//
//            // The rest is the object name
//            String objectName = fullObjectPath.substring(fullObjectPath.indexOf('/') + 1);
//
//            logger.info("Attempting to delete from bucket: {}, object: {}", this.bucketName, objectName);
//
//            // 3. Call the removeObject method with the parsed details
//            minioClient.removeObject(
//                    RemoveObjectArgs.builder()
//                            .bucket(this.bucketName)
//                            .object(objectName)
//                            .build()
//            );
//        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
//            logger.error("MinIO Exception occurred: {}", e.getMessage());
//            throw e;
//        }
//    }
}
