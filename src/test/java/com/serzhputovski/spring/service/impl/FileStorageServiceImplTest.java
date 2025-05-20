package com.serzhputovski.spring.service.impl;

import com.serzhputovski.spring.config.StorageConfig;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.*;
import java.util.Comparator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileStorageServiceImplTest {

    private FileStorageServiceImpl storage;
    private Path tempDir;

    @BeforeEach
    void init() throws Exception {
        tempDir = Files.createTempDirectory("upload-test");
        StorageConfig cfg = mock(StorageConfig.class);
        when(cfg.getUploadDir()).thenReturn(tempDir.toString());
        storage = new FileStorageServiceImpl(cfg);
    }

    @AfterEach
    void cleanup() throws Exception {
        Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .forEach(path -> path.toFile().delete());
    }

    @ParameterizedTest
    @MethodSource("provideFilesToStore")
    void store_ShouldSaveAndReturnFilename(MockMultipartFile file) throws Exception {
        String generated = storage.store(file);

        assertNotNull(generated);
        assertTrue(generated.endsWith("_" + file.getOriginalFilename()));
        Resource resource = storage.loadAsResource(generated);
        byte[] data = resource.getInputStream().readAllBytes();
        assertArrayEquals(file.getBytes(), data);
    }

    @ParameterizedTest
    @ValueSource(strings = {"no-such.txt", "absent-file.png"})
    void loadAsResource_NonExisting_Throws(String filename) {
        assertThrows(RuntimeException.class,
                () -> storage.loadAsResource(filename));
    }

    static Stream<MockMultipartFile> provideFilesToStore() {
        return Stream.of(
                new MockMultipartFile("f", "a.txt", "text/plain", "Hello".getBytes()),
                new MockMultipartFile("f", "b.png", "image/png", new byte[]{1,2,3,4})
        );
    }
}