package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Requests.*;
import br.com.sis_dispo_docente.dto.Responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import br.com.sis_dispo_docente.service.CursoService;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService service;

    @GetMapping
    public List<CursoDto> listar(@RequestParam(required = false) Long centroId) {
        return service.listar(centroId).stream().map(CursoDto::de).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CursoDto criar(@RequestBody @Valid CursoReq req) {
        return CursoDto.de(service.criar(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CursoDto atualizar(@PathVariable Long id, @RequestBody @Valid CursoReq req) {
        return CursoDto.de(service.atualizar(id, req));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CursoDto inativar(@PathVariable Long id) {
        return CursoDto.de(service.inativar(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
