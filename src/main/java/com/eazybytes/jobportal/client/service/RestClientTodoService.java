package com.eazybytes.jobportal.client.service;

import com.eazybytes.jobportal.dto.TodoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestClientTodoService {

    private final RestClient restClient;
    private static final String TODO_API = "/todos";

    public RestClientTodoService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://jsonplaceholder.typicode.com").defaultHeader("Accept", "application/json").
                build();
    }

    public List<TodoDto> getAllTodos(){
        return restClient.get().uri(TODO_API).retrieve().onStatus(HttpStatusCode::isError, (req,res) ->
                new IllegalArgumentException("Failed to fetch todos from API")).body(new ParameterizedTypeReference<List<TodoDto>>(){});
    }

    public TodoDto getTodoById(Long id){

        return restClient.get().uri(TODO_API+"{id}", id).retrieve().onStatus(HttpStatusCode::is4xxClientError,(req, res) ->
                new IllegalArgumentException("No Todo found with this id "+id)).body(new ParameterizedTypeReference<TodoDto>() {
        });
    }

    public TodoDto createTodo(TodoDto todoDto){
        return restClient.post().uri(TODO_API).body(todoDto).retrieve().body(TodoDto.class);
    }

    public TodoDto update(Long id, TodoDto todo) {
        return restClient.put()
                .uri(TODO_API + "/{id}", id)
                .body(todo)
                .retrieve()
                .body(TodoDto.class);
    }

    public void delete(Long id) {
        restClient.delete()
                .uri(TODO_API+ "/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
