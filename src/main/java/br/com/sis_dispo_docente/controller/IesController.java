package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Requests.*;
import br.com.sis_dispo_docente.dto.Responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import br.com.sis_dispo_docente.service.IesService;

@RestController
@RequestMapping("/api/ies")
@RequiredArgsConstructor
public class IesController {

    private final IesService service;

    @GetMapping
    public List<IesDto> listar() {
        return service.listar().stream().map(IesDto::de).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public IesDto criar(@RequestBody @Valid IesReq req) {
        return IesDto.de(service.criar(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public IesDto atualizar(@PathVariable Long id, @RequestBody @Valid IesReq req) {
        return IesDto.de(service.atualizar(id, req));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public IesDto inativar(@PathVariable Long id) {
        return IesDto.de(service.inativar(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
