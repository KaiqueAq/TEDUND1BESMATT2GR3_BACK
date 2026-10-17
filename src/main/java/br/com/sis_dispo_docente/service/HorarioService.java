package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.*;
import br.com.sis_dispo_docente.dto.Requests.GradeHorariosReq;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.FaixaHorarioRepository;
import br.com.sis_dispo_docente.repository.HorarioDisponivelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioService {
    private final HorarioDisponivelRepository repository;
    private final FaixaHorarioRepository faixaRepository;
    private final CentroAcademicoService centroService;
    private final SemestreService semestreService;
    private final AuditoriaService auditoria;

    public List<HorarioDisponivel> listar(Long centroId, Long semestreId) {
        return repository.findByCentroIdAndSemestreId(centroId, semestreId).stream()
                .sorted(Comparator.comparing(HorarioDisponivel::getDia)
                        .thenComparingInt(h -> h.getFaixa().getOrdem()))
                .toList();
    }

    public List<FaixaHorario> listarFaixas() {
        return faixaRepository.findAllByOrderByOrdemAsc();
    }

    public HorarioDisponivel buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Horário não encontrado."));
    }

    @Transactional
    public List<HorarioDisponivel> salvarGrade(GradeHorariosReq req) {
        CentroAcademico centro = centroService.buscar(req.centroId());
        Semestre semestre = semestreService.buscar(req.semestreId());

        if (centro.getStatus() != Status.ATIVO) {
            throw new RegraNegocioException("Não é possível liberar horários para um centro inativo.");
        }

        for (var celula : req.celulas()) {
            FaixaHorario faixa = faixaRepository.findById(celula.faixaId())
                    .orElseThrow(() -> new NaoEncontradoException("Faixa de horário não encontrada."));

            HorarioDisponivel horario = repository
                    .findByCentroIdAndSemestreIdAndDiaAndFaixaId(
                            centro.getId(), semestre.getId(), celula.dia(), faixa.getId())
                    .orElseGet(HorarioDisponivel::new);

            horario.setCentro(centro);
            horario.setSemestre(semestre);
            horario.setDia(celula.dia());
            horario.setFaixa(faixa);
            horario.setStatus(celula.ativo() ? Status.ATIVO : Status.INATIVO);
            repository.save(horario);

            auditoria.registrar(celula.ativo() ? "LIBERACAO" : "INATIVACAO",
                    "HORARIO_DISPONIVEL", horario.getId(),
                    semestre.getCodigo() + " | " + centro.getSigla() + " | "
                            + celula.dia() + " | " + faixa.getDescricao());
        }

        return listar(centro.getId(), semestre.getId());
    }
}
