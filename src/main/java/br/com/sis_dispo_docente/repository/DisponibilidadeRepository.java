package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.Disponibilidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisponibilidadeRepository extends JpaRepository<Disponibilidade, Long> {
    boolean existsByDocenteIdAndHorarioId(Long docenteId, Long horarioId);
    List<Disponibilidade> findByDocenteIdAndHorarioSemestreId(Long docenteId, Long semestreId);
    void deleteByDocenteIdAndHorarioSemestreId(Long docenteId, Long semestreId);
}
