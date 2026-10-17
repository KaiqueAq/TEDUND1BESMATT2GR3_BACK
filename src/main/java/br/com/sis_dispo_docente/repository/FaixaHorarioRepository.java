package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.FaixaHorario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FaixaHorarioRepository extends JpaRepository<FaixaHorario, Long> {
    List<FaixaHorario> findAllByOrderByOrdemAsc();
}
