package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Responses.HorarioDto;
import br.com.sis_dispo_docente.dto.Responses.Relatorio;
import br.com.sis_dispo_docente.service.RelatorioService;
import br.com.sis_dispo_docente.service.RelatorioService.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Relatorios de cada perfil. */
@RestController
@RequestMapping("/api/relatorios")
@RequiredArgsConstructor
public class RelatorioController {

    private final RelatorioService service;

    @GetMapping("/estrutura")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Relatorio<List<IesEstrutura>> estrutura() {
        return service.estrutura();
    }

    @GetMapping("/docentes")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public Relatorio<List<CentroComDocentes>> docentes() {
        return service.docentesPorCentro();
    }

    @GetMapping("/horarios")
    @PreAuthorize("hasAnyRole('COLABORADOR','ADMINISTRADOR')")
    public Relatorio<List<HorarioDto>> horarios(@RequestParam Long centroId, @RequestParam Long semestreId) {
        return service.horarios(centroId, semestreId);
    }

    @GetMapping("/mapa-disponibilidade")
    @PreAuthorize("hasAnyRole('COLABORADOR','ADMINISTRADOR')")
    public Relatorio<List<DocenteDisponibilidade>> mapaDisponibilidade(@RequestParam Long centroId,
                                                                      @RequestParam Long semestreId) {
        return service.mapaDisponibilidade(centroId, semestreId);
    }

    @GetMapping("/meu")
    @PreAuthorize("hasRole('USUARIO')")
    public Relatorio<DocenteDisponibilidade> meu(@RequestParam Long semestreId) {
        return service.meu(semestreId);
    }
}
