package ru.yandex.practicum.core_api.interfaces;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.model.compilation.CompilationCreateDto;
import ru.yandex.practicum.core_api.model.compilation.CompilationRequestDto;
import ru.yandex.practicum.core_api.model.compilation.CompilationUpdateDto;

import java.util.List;

public interface CompilationInterface {

    // Admin endpoints
    @PostMapping("/admin/compilations")
    ResponseEntity<CompilationRequestDto> create(@RequestBody @Valid CompilationCreateDto compilationCreateDto);

    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long compId);

    @PatchMapping("/admin/compilations/{compId}")
    ResponseEntity<CompilationRequestDto> update(@RequestBody @Valid CompilationUpdateDto compilationUpdateDto,
                                                 @PathVariable("compId") Long compId);

    // Public endpoints
    @GetMapping("/compilations")
    ResponseEntity<List<CompilationRequestDto>> get(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size);

    @GetMapping("/compilations/{compId}")
    ResponseEntity<CompilationRequestDto> getById(@PathVariable("compId") Long compId);
}

