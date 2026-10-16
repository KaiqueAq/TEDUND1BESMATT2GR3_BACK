package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.Interesse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InteresseRepository extends JpaRepository<Interesse, Long> {
    boolean existsByDisciplinaId(Long disciplinaId);
    List<Interesse> findByDocenteIdAndSemestreIdOrderByPrioridadeAsc(Long docenteId, Long semestreId);
    void deleteByDocenteIdAndSemestreId(Long docenteId, Long semestreId);
}
