package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.Ies;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IesRepository extends JpaRepository<Ies, Long> {
    boolean existsBySiglaIgnoreCase(String sigla);
    boolean existsBySiglaIgnoreCaseAndIdNot(String sigla, Long id);
    List<Ies> findAllByOrderBySiglaAsc();
}
