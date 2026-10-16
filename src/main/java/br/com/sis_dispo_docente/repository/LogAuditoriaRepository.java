package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {
    List<LogAuditoria> findAllByOrderByDataHoraDesc();
}
