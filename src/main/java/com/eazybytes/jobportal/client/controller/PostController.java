package com.eazybytes.jobportal.client.controller;


import com.eazybytes.jobportal.client.service.PostService;
import com.eazybytes.jobportal.client.service.RestClientTodoService;
import com.eazybytes.jobportal.client.service.TodoService;
import com.eazybytes.jobportal.dto.PostDto;
import com.eazybytes.jobportal.dto.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class PostController {

    private final RestClientTodoService restClientTodoService;
    private final PostService postService;

    @GetMapping
    ResponseEntity<List<PostDto>> findAll() {
        // return ResponseEntity.ok(restClientTodoService.findAll());
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<PostDto> findById(@PathVariable Long id) {
        // return ResponseEntity.ok(restClientTodoService.findById(id));
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping
    ResponseEntity<PostDto> create(@RequestBody PostDto postDto) {
        // return ResponseEntity.status(HttpStatus.CREATED).body(restClientTodoService.create(toDoDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(postDto));
    }

    @PutMapping("/{id}")
    ResponseEntity<PostDto> update(@PathVariable Long id, @RequestBody PostDto postDto) {
        // return ResponseEntity.status(HttpStatus.OK).body(restClientTodoService.update(id, toDoDto));
        return ResponseEntity.status(HttpStatus.OK).body(postService.update(id, postDto));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        // restClientTodoService.delete(id);
        postService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("ToDo deleted successfully");
    }
}