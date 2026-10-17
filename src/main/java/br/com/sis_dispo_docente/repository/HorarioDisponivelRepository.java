package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.HorarioDisponivel;
import br.com.sis_dispo_docente.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HorarioDisponivelRepository extends JpaRepository<HorarioDisponivel, Long> {
    boolean existsByCentroId(Long centroId);
    java.util.Optional<HorarioDisponivel> findByCentroIdAndSemestreIdAndDiaAndFaixaId(
            Long centroId, Long semestreId,
            br.com.sis_dispo_docente.domain.DiaSemana dia, Long faixaId);
    List<HorarioDisponivel> findByCentroIdAndSemestreId(Long centroId, Long semestreId);
    List<HorarioDisponivel> findByCentroIdAndSemestreIdAndStatus(
            Long centroId, Long semestreId, Status status);
}
