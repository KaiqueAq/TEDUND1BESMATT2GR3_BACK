package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
    boolean existsByCursoId(Long cursoId);
    boolean existsBySiglaIgnoreCaseAndCursoId(String sigla, Long cursoId);
    boolean existsBySiglaIgnoreCaseAndCursoIdAndIdNot(String sigla, Long cursoId, Long id);
    List<Disciplina> findAllByOrderBySiglaAsc();
    List<Disciplina> findByCursoIdOrderBySiglaAsc(Long cursoId);
}
