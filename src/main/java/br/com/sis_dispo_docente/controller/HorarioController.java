package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Requests.GradeHorariosReq;
import br.com.sis_dispo_docente.dto.Responses.FaixaDto;
import br.com.sis_dispo_docente.dto.Responses.HorarioDto;
import br.com.sis_dispo_docente.service.HorarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService service;

    @GetMapping("/faixas")
    public List<FaixaDto> listarFaixas() {
        return service.listarFaixas().stream()
                .map(FaixaDto::de)
                .toList();
    }

    @GetMapping
    public List<HorarioDto> listar(
            @RequestParam Long centroId,
            @RequestParam Long semestreId) {
        return service.listar(centroId, semestreId).stream()
                .map(HorarioDto::de)
                .toList();
    }

    @PutMapping("/grade")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<HorarioDto> salvarGrade(
            @RequestBody @Valid GradeHorariosReq req) {
        return service.salvarGrade(req).stream()
                .map(HorarioDto::de)
                .toList();
    }
}
