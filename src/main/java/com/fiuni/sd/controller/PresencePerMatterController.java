package com.fiuni.sd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fiuni.sd.dto.presencePerMatter.PresencePerMatterDto;
import com.fiuni.sd.dto.presencePerMatter.PresencePerMatterListDto;
import com.fiuni.sd.service.presencePerMatter.IPresencePerMatterService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/presences-per-matter") // Asegúrate de que la ruta sea consistente con la entidad
public class PresencePerMatterController {
    @Autowired
    private IPresencePerMatterService presencePerMatterService;

    @Value("${pagination.size:10}")
    private Integer pageSize;

    @GetMapping("/page/{page}")
    public PresencePerMatterListDto get(@PathVariable Integer page) {
        return presencePerMatterService.get(PageRequest.of(page, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresencePerMatterDto> getById(@PathVariable(value = "id") final Integer id) {
        try {
            PresencePerMatterDto presencePerMatter = presencePerMatterService.getById(id);
            return ResponseEntity.ok(presencePerMatter);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public PresencePerMatterDto create(@RequestBody PresencePerMatterDto presencePerMatterDto) {
        return presencePerMatterService.create(presencePerMatterDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PresencePerMatterDto> update(@PathVariable(value = "id") final Integer id,
            @RequestBody @Valid final PresencePerMatterDto presencePerMatterDto) {
        return ResponseEntity.ok(presencePerMatterService.update(id, presencePerMatterDto));
    }

    @DeleteMapping("/{id}")
    public PresencePerMatterDto delete(@PathVariable Integer id) {
        return presencePerMatterService.delete(id);
    }
}
