package com.adekunle.controller;

import com.adekunle.services.BookAuthorClientService;
import com.google.protobuf.Descriptors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BookAuthorController {
    private final BookAuthorClientService bookAuthorClientService;

    @GetMapping("get-author/{authorId}")
    public Map<Descriptors.FieldDescriptor, Object> getAuthor(@PathVariable("authorId") String authorId) {

        return bookAuthorClientService.getAuthor(Integer.parseInt(authorId)); // cast the string value into integer since the author id is int value

    }
    @GetMapping("book/{authorId}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getBookByAuthor(@PathVariable("authorId") String authorId) throws InterruptedException {

        return bookAuthorClientService.getAllBooksByAuthorId(Integer.parseInt(authorId)); // cast the string value into integer since the author id is int value
    }
    @GetMapping("book")
    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getMostExpensiveBook() throws InterruptedException {
        return bookAuthorClientService.getMostExpensiveBook();
    }
    @GetMapping("author/{gender}")
    public List<Map<Descriptors.FieldDescriptor, Object>> getBooksByAuthorGenders(@PathVariable String gender) throws InterruptedException {
        return bookAuthorClientService.getBooksByAuthorGenders(gender);
    }
}
