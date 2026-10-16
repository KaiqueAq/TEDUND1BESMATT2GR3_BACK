package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.Docente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocenteRepository extends JpaRepository<Docente, Long> {
    boolean existsByCentroId(Long centroId);
    boolean existsByMatricula(String matricula);
    Optional<Docente> findByUsuarioLogin(String login);
    List<Docente> findAllByOrderByNomeCompletoAsc();
    List<Docente> findByCentroIdOrderByNomeCompletoAsc(Long centroId);
}
