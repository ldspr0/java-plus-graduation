package ru.yandex.practicum.event_service.controller.compilation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.core_api.interfaces.CompilationInterface;
import ru.yandex.practicum.core_api.model.compilation.CompilationCreateDto;
import ru.yandex.practicum.core_api.model.compilation.CompilationRequestDto;
import ru.yandex.practicum.core_api.model.compilation.CompilationUpdateDto;
import ru.yandex.practicum.event_service.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationController implements CompilationInterface {
    private final String className = this.getClass().getSimpleName();
    private final CompilationService compilationService;

    @Override
    @PostMapping("/admin/compilations")
    public ResponseEntity<CompilationRequestDto> create(@RequestBody @Valid CompilationCreateDto compilationCreateDto) {
        log.trace("{}: create() call with compilationCreateDto: {}", className, compilationCreateDto);
        CompilationRequestDto compilationRequestDto = compilationService.create(compilationCreateDto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(compilationRequestDto);
    }

    @Override
    @PatchMapping("/admin/compilations/{compId}")
    public ResponseEntity<CompilationRequestDto> update(@RequestBody @Valid CompilationUpdateDto compilationUpdateDto,
                                                        @PathVariable Long compId) {
        log.trace("{}: update() call with compilationUpdateDto: {}, compilationId: {}", className, compilationUpdateDto, compId);
        CompilationRequestDto compilationRequestDto = compilationService.update(compilationUpdateDto, compId);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(compilationRequestDto);
    }

    @Override
    @DeleteMapping("/admin/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long compId) {
        log.trace("{}: delete() call with compilationId: {}", className, compId);
        compilationService.delete(compId);
    }

    @Override
    @GetMapping("/compilations")
    public ResponseEntity<List<CompilationRequestDto>> get(
            @RequestParam(defaultValue = "false") Boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {
        log.trace("{}: get() call with pinned: {}, from: {}, size: {}", className, pinned, from, size);
        List<CompilationRequestDto> compilations = compilationService.get(pinned, from, size);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(compilations);
    }

    @Override
    @GetMapping("/compilations/{compId}")
    public ResponseEntity<CompilationRequestDto> getById(@PathVariable Long compId) {
        log.trace("{}: getById() call with compilationId {}", className, compId);
        CompilationRequestDto compilationRequestDto = compilationService.getById(compId);

        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(compilationRequestDto);
    }
}
