package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.Semestre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemestreRepository extends JpaRepository<Semestre, Long> {
    boolean existsByCodigo(String codigo);
    Optional<Semestre> findByCodigo(String codigo);
    List<Semestre> findAllByOrderByCodigoDesc();
}
