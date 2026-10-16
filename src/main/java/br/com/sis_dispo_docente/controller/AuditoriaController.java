package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.dto.Responses.LogDto;
import br.com.sis_dispo_docente.repository.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** Monitorar logs de auditoria (somente Administrador). */
@RestController
@RequestMapping("/api/auditoria")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class AuditoriaController {

    private final LogAuditoriaRepository repository;

    @GetMapping
    public List<LogDto> listar() {
        return repository.findAllByOrderByDataHoraDesc().stream().map(LogDto::de).toList();
    }
}
