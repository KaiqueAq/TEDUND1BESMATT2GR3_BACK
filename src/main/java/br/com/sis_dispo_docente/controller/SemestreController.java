package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Requests.SemestreReq;
import br.com.sis_dispo_docente.dto.Responses.SemestreDto;
import br.com.sis_dispo_docente.service.SemestreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/semestres")
@RequiredArgsConstructor
public class SemestreController {

    private final SemestreService service;

    @GetMapping
    public List<SemestreDto> listar() {
        return service.listar().stream().map(SemestreDto::de).toList();
    }

    @GetMapping("/{id}")
    public SemestreDto buscar(@PathVariable Long id) {
        return SemestreDto.de(service.buscar(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public SemestreDto criar(@RequestBody @Valid SemestreReq req) {
        return SemestreDto.de(service.criar(req));
    }
}
