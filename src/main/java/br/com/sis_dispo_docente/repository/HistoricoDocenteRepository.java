package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.HistoricoDocente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoDocenteRepository extends JpaRepository<HistoricoDocente, Long> {
    List<HistoricoDocente> findByDocenteIdOrderByDataHoraDesc(Long docenteId);
}
