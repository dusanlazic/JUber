package com.nwt.juber.service;

import com.nwt.juber.config.AppProperties;
import com.nwt.juber.exception.FileNotFoundException;
import com.nwt.juber.exception.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path uploadsLocation;
    private final String servingEndpoint;
    private final Set<String> allowedContentTypes;
    private final Tika tika = new Tika();
    private final MimeTypes mimeTypes = TikaConfig.getDefaultConfig().getMimeRepository();

    @Autowired
    public FileStorageService(AppProperties appProperties) {
        uploadsLocation = Paths.get(appProperties.getUploads().getUploadsLocation());
        servingEndpoint = appProperties.getUploads().getServingEndpoint();
        allowedContentTypes = appProperties.getUploads().getAllowedContentTypes();
    }

    public void init() {
        try {
            FileSystemUtils.deleteRecursively(uploadsLocation.toFile());
            Files.createDirectories(uploadsLocation);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String store(MultipartFile file) {
        String contentType = detectContentType(file);
        String storedFilename = generateStoredFilename(contentType);

        Path storedFilePath = generateStoredFilePath(storedFilename);
        validatePath(storedFilePath);

        String storedFileURI = generateStoredFileURI(storedFilename);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, storedFilePath, StandardCopyOption.REPLACE_EXISTING);
            return storedFileURI;
        } catch (IOException e) {
            throw new FileUploadException("Failed to store the file.", e);
        }
    }

    public String store(String imageUrl, String contentType) {
        String storedFilename = generateStoredFilename(contentType);

        Path storedFilePath = generateStoredFilePath(storedFilename);
        validatePath(storedFilePath);

        String storedFileURI = generateStoredFileURI(storedFilename);

        try (InputStream is = new BufferedInputStream(new URL(imageUrl).openStream())) {
            Files.copy(is, storedFilePath, StandardCopyOption.REPLACE_EXISTING);
            return storedFileURI;
        } catch (IOException e) {
            throw new FileUploadException("Failed to store the file.", e);
        }
    }

    public ResponseEntity<Resource> serve(String filename) throws IOException {
        Path storedFilePath = uploadsLocation.resolve(filename);
        validatePath(storedFilePath);

        Resource resource = new UrlResource(storedFilePath.toUri());
        if (!resource.exists() || !resource.isReadable())
            throw new FileNotFoundException("File not found.");

        String extension = FilenameUtils.getExtension(filename); // dot not included (i.e. "png")

        return ResponseEntity.ok()
                .contentType(extensionToMediaType(extension))
                .body(resource);
    }

    private String detectContentType(MultipartFile file) {
        if (file.isEmpty())
            throw new FileUploadException("File is empty.");

        if (!allowedContentTypes.contains(file.getContentType()))
            throw new FileUploadException("Invalid content type.");

        try {
            String detectedType = tika.detect(file.getBytes());
            if (!allowedContentTypes.contains(detectedType))
                throw new FileUploadException("Invalid file type or the file is corrupted.");

            return detectedType;

        } catch (IOException e) {
            throw new FileUploadException("Invalid file type or the file is corrupted.", e);
        }
    }

    private String generateStoredFilename(String contentType) {
        try {
            String baseFilename = UUID.randomUUID().toString();
            String extension = mimeTypes.forName(contentType).getExtension(); // dot included (i.e. ".png")

            return baseFilename + extension;

        } catch (MimeTypeException e) {
            throw new FileUploadException("Invalid content type.", e);
        }
    }

    private Path generateStoredFilePath(String filename) {
        return uploadsLocation.resolve(filename).normalize().toAbsolutePath();
    }

    private String generateStoredFileURI(String filename) {
        String URI = "";

        if (!servingEndpoint.startsWith("/"))
            URI += "/";
        URI += servingEndpoint;

        if (!servingEndpoint.endsWith("/"))
            URI += "/";
        URI += filename;

        return URI;
    }

    private void validatePath(Path path) {
        if (!path.normalize().toAbsolutePath().getParent().equals(uploadsLocation.toAbsolutePath()))
            throw new FileUploadException("Invalid path.");
    }

    private MediaType extensionToMediaType(String extension) {
        return switch (extension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
