package com.nhnacademy.bookstoreuserapi.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "book-api", contextId = "bookAdapter")
public interface BookAdapter {

    @PostMapping("books/{bookId}/document")
    ResponseEntity<Void> updateBookDocument(
            @PathVariable Long bookId,
            @RequestParam Long reviewCount,
            @RequestParam Double reviewAverage);

}
