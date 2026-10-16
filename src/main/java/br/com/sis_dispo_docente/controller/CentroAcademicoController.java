package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Requests.*;
import br.com.sis_dispo_docente.dto.Responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import br.com.sis_dispo_docente.service.CentroAcademicoService;

@RestController
@RequestMapping("/api/centros")
@RequiredArgsConstructor
public class CentroAcademicoController {

    private final CentroAcademicoService service;

    @GetMapping
    public List<CentroDto> listar() {
        return service.listar().stream().map(CentroDto::de).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CentroDto criar(@RequestBody @Valid CentroReq req) {
        return CentroDto.de(service.criar(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CentroDto atualizar(@PathVariable Long id, @RequestBody @Valid CentroReq req) {
        return CentroDto.de(service.atualizar(id, req));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public CentroDto inativar(@PathVariable Long id) {
        return CentroDto.de(service.inativar(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
