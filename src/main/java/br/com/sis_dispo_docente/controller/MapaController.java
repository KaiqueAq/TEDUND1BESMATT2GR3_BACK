package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Requests.*;
import br.com.sis_dispo_docente.dto.Responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import br.com.sis_dispo_docente.service.MapaService;
import br.com.sis_dispo_docente.service.RelatorioService;
import br.com.sis_dispo_docente.service.RelatorioService.DocenteDisponibilidade;

@RestController
@RequestMapping("/api/mapa")
@RequiredArgsConstructor
public class MapaController {

    private final MapaService service;
    private final RelatorioService relatorioService;

    /** Disponibilidade e disciplinas de interesse lancadas pelos docentes do centro (apoio para montar o mapa). */
    @GetMapping("/escolhas")
    @PreAuthorize("hasAnyRole('COLABORADOR','ADMINISTRADOR')")
    public List<DocenteDisponibilidade> escolhas(@RequestParam Long centroId, @RequestParam Long semestreId) {
        return relatorioService.escolhasDocentes(centroId, semestreId);
    }

    @GetMapping
    public List<MapaItemDto> listar(@RequestParam Long centroId, @RequestParam Long semestreId) {
        return service.listar(centroId, semestreId).stream().map(MapaItemDto::de).toList();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('COLABORADOR','ADMINISTRADOR')")
    public MapaItemDto criar(@RequestBody @Valid MapaItemReq req) {
        return MapaItemDto.de(service.criar(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COLABORADOR','ADMINISTRADOR')")
    public MapaItemDto atualizar(@PathVariable Long id, @RequestBody @Valid MapaItemReq req) {
        return MapaItemDto.de(service.atualizar(id, req));
    }

    @PatchMapping("/{id}/inativar")
    @PreAuthorize("hasAnyRole('COLABORADOR','ADMINISTRADOR')")
    public MapaItemDto inativar(@PathVariable Long id) {
        return MapaItemDto.de(service.inativar(id));
    }
}
