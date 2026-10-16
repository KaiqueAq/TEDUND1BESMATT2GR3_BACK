package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Requests.*;
import br.com.sis_dispo_docente.dto.Responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import br.com.sis_dispo_docente.service.DocenteService;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
public class DocenteController {

    private final DocenteService service;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<DocenteDto> listar() {
        return service.listar().stream().map(DocenteDto::de).toList();
    }

    @GetMapping("/{id}/historico")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<HistoricoDto> historico(@PathVariable Long id) {
        return service.historico(id).stream().map(HistoricoDto::de).toList();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public DocenteDto criar(@RequestBody @Valid DocenteReq req) {
        return DocenteDto.de(service.criar(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public DocenteDto atualizar(@PathVariable Long id, @RequestBody @Valid DocenteReq req) {
        return DocenteDto.de(service.atualizar(id, req));
    }

    /** Inativacao exige justificativa (vai para o historico do docente). */
    @PostMapping("/{id}/inativar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public DocenteDto inativar(@PathVariable Long id, @RequestBody @Valid JustificativaReq req) {
        return DocenteDto.de(service.inativar(id, req.justificativa()));
    }
}
