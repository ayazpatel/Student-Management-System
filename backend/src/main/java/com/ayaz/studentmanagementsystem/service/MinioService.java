package com.ayaz.studentmanagementsystem.service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
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

    public String uploadAndGetPresignedUrl(MultipartFile file, String objectName) {
        try {
            // 1. Upload the object
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            // 2. Get the presigned URL
            String url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry(24*1000, TimeUnit.HOURS) // 1000 Days Expiry
                            .build()
            );

            return url; // Success!
        } catch (ErrorResponseException e) {
            logger.error("MinIO ErrorResponseException: {}", e.getMessage());
            return "Error: A MinIO error occurred (e.g., bucket not found, access denied). " + e.errorResponse().code();
        } catch (InsufficientDataException e) {
            logger.error("MinIO InsufficientDataException: {}", e.getMessage());
            return "Error: The file upload was incomplete.";
        } catch (InternalException e) {
            logger.error("MinIO InternalException: {}", e.getMessage());
            return "Error: An internal error occurred with the MinIO client.";
        } catch (InvalidKeyException e) {
            logger.error("MinIO InvalidKeyException: {}", e.getMessage());
            return "Error: The provided access/secret key is invalid.";
        } catch (IOException e) {
            logger.error("IOException during file upload: {}", e.getMessage());
            return "Error: A network or file I/O error occurred.";
        } catch (NoSuchAlgorithmException e) {
            logger.error("MinIO NoSuchAlgorithmException: {}", e.getMessage());
            return "Error: A required cryptographic algorithm is missing.";
        } catch (MinioException e) {
            // A general catch-all for other MinIO-specific exceptions
            logger.error("A MinIO exception occurred: {}", e.getMessage());
            return "Error: An unspecified MinIO error occurred.";
        }
    }

    /**
     * Deletes a file by parsing its full URL.
     *
     * @param objectUrl The full URL of the object to delete.
     * @throws MinioException if an error occurs on the MinIO server.
     * @throws IOException for network or parsing errors.
     * @throws NoSuchAlgorithmException if a required crypto algorithm is missing.
     * @throws InvalidKeyException if the access/secret key is invalid.
     */
    public void deleteFileByUrl(String objectUrl) throws MinioException, IOException, NoSuchAlgorithmException, InvalidKeyException {
        // 1. Parse the URL to get the path
        URL url = new URL(objectUrl);
        String path = url.getPath(); // This will be "/my-springboot-bucket/path/to/my-document.pdf"

        // 2. Extract bucket and object names from the path
        // The path starts with a '/', so we remove it.
        String fullObjectPath = path.substring(1);

        // The first part is the bucket name
        String bucketName = fullObjectPath.substring(0, fullObjectPath.indexOf('/'));

        // The rest is the object name
        String objectName = fullObjectPath.substring(fullObjectPath.indexOf('/') + 1);

        System.out.println("Attempting to delete from bucket: " + bucketName + ", object: " + objectName);

        // 3. Call the removeObject method with the parsed details
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }
}
