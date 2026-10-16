package br.com.sis_dispo_docente.repository;

import br.com.sis_dispo_docente.domain.MapaItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MapaItemRepository extends JpaRepository<MapaItem, Long> {
    boolean existsByDisciplinaId(Long disciplinaId);
    List<MapaItem> findByHorarioCentroIdAndHorarioSemestreId(Long centroId, Long semestreId);
}
