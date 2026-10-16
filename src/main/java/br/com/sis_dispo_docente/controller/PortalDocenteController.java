package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Requests.*;
import br.com.sis_dispo_docente.dto.Responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import br.com.sis_dispo_docente.service.PortalDocenteService;

/** Telas do proprio docente (perfil USUARIO). */
@RestController
@RequestMapping("/api/meu")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USUARIO')")
public class PortalDocenteController {

    private final PortalDocenteService service;

    @GetMapping("/disponibilidade")
    public List<HorarioDto> disponibilidade(@RequestParam Long semestreId) {
        return service.minhaDisponibilidade(semestreId).stream().map(d -> HorarioDto.de(d.getHorario()))
                .sorted(HorarioDto.ORDEM).toList();
    }

    @PutMapping("/disponibilidade")
    public List<HorarioDto> salvarDisponibilidade(@RequestBody @Valid DisponibilidadeReq req) {
        return service.salvarDisponibilidade(req.semestreId(), req.horarioIds()).stream().map(d -> HorarioDto.de(d.getHorario()))
                .sorted(HorarioDto.ORDEM).toList();
    }

    @GetMapping("/interesses")
    public List<InteresseDto> interesses(@RequestParam Long semestreId) {
        return service.meusInteresses(semestreId).stream().map(InteresseDto::de).toList();
    }

    @PutMapping("/interesses")
    public List<InteresseDto> salvarInteresses(@RequestBody @Valid InteressesReq req) {
        return service.salvarInteresses(req.semestreId(), req.itens()).stream().map(InteresseDto::de).toList();
    }
}
