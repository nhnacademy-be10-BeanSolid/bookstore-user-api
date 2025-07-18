package com.nhnacademy.bookstoreuserapi.review.service;


import com.nhnacademy.bookstoreuserapi.review.service.impl.MinioServiceImpl;
import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.messages.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MinioServiceTest {
    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioServiceImpl minioService;

    private final String bucketName = "test-bucket";
    private final String minioEndpoint = "http://storage.example.com";


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(minioService, "bucketName", bucketName);
        ReflectionTestUtils.setField(minioService, "minioEndpoint", minioEndpoint);
    }

    @Test
    @DisplayName("minio 이미지 삭제 - 성공")
    public void deleteImage() throws Exception {
        String imageUrl = "http://storage.example.com/test-bucket/images/my%20image.jpg";
        String expectedObjectName = "images/my image.jpg";

        // when
        minioService.deleteImage(imageUrl);

        // then
        ArgumentCaptor<RemoveObjectArgs> captor = ArgumentCaptor.forClass(RemoveObjectArgs.class);
        verify(minioClient, times(1)).removeObject(captor.capture());

        RemoveObjectArgs args = captor.getValue();
        assertThat(args.bucket()).isEqualTo(bucketName);
        assertThat(args.object()).isEqualTo(expectedObjectName);
    }

    @Test
    @DisplayName("minio 이미지 삭제 - 실패")
    public void deleteImageFailure() throws Exception {
        String imageUrl = "http://storage.example.com/test-bucket/images/error.jpg";

        doThrow(new RuntimeException("삭제 실패"))
                .when(minioClient).removeObject(any(RemoveObjectArgs.class));

        assertThatCode(() -> minioService.deleteImage(imageUrl))
                .doesNotThrowAnyException();

        verify(minioClient, times(1)).removeObject(any(RemoveObjectArgs.class));
    }

    @Test
    @DisplayName("minio 이미지 URL 목록 가져오기 - 성공")
    public void getAllImageUrls() throws Exception {
        Item item1 = mock(Item.class);
        Item item2 = mock(Item.class);
        when(item1.objectName()).thenReturn("images/image1.jpg");
        when(item2.objectName()).thenReturn("images/image2.png");

        Result<Item> result1 = mock(Result.class);
        Result<Item> result2 = mock(Result.class);
        when(result1.get()).thenReturn(item1);
        when(result2.get()).thenReturn(item2);

        Iterable<Result<Item>> results = List.of(result1, result2);
        when(minioClient.listObjects(any(ListObjectsArgs.class))).thenReturn(results);

        // when
        List<String> urls = minioService.getAllImageUrls();

        // then
        assertThat(urls).containsExactly(
                "http://storage.example.com/test-bucket/images/image1.jpg",
                "http://storage.example.com/test-bucket/images/image2.png"
        );
        verify(minioClient).listObjects(any(ListObjectsArgs.class));
    }

    @Test
    @DisplayName("MinIO 이미지 URL 조회 실패 시 빈 리스트 반환")
    void testGetAllImageUrls_Failure(){
        // given
        when(minioClient.listObjects(any(ListObjectsArgs.class)))
                .thenThrow(new RuntimeException("MinIO 연결 실패"));

        // when
        List<String> urls = minioService.getAllImageUrls();

        // then
        assertThat(urls).isEmpty();
        verify(minioClient).listObjects(any(ListObjectsArgs.class));
    }
}
