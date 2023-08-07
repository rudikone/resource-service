package ru.rudikov.resourceservice.adapter.primary.rest;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;
import ru.rudikov.resourceservice.application.port.primary.ResourcePort;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class ResourceController {

    private final ResourcePort port;

    @PostMapping
    public ResponseEntity<Integer> createResourceObject(@RequestBody ResourceObject object) {
        val result = port.save(object);
        return ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceObject> getResourceObject(@PathVariable Integer id) {
        return ok(port.get(id));
    }

}
