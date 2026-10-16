package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.CentroAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CentroAcademicoRepository extends JpaRepository<CentroAcademico, Long> {
    boolean existsByIesId(Long iesId);
    List<CentroAcademico> findAllByOrderBySiglaAsc();
    List<CentroAcademico> findByIesIdOrderBySiglaAsc(Long iesId);
    boolean existsBySiglaIgnoreCaseAndIesId(String sigla, Long iesId);
    boolean existsBySiglaIgnoreCaseAndIesIdAndIdNot(String sigla, Long iesId, Long id);
}
