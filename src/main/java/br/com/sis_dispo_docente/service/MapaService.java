package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.*;
import br.com.sis_dispo_docente.dto.Requests.MapaItemReq;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.DisponibilidadeRepository;
import br.com.sis_dispo_docente.repository.MapaItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Mapa Final: o coordenador define docente x disciplina x dia/horario, apos os docentes lancarem a disponibilidade. */
@Service
@RequiredArgsConstructor
public class MapaService {

    private final MapaItemRepository repository;
    private final DisponibilidadeRepository disponibilidadeRepository;
    private final DocenteService docenteService;
    private final DisciplinaService disciplinaService;
    private final HorarioService horarioService;
    private final AuditoriaService auditoria;

    public List<MapaItem> listar(Long centroId, Long semestreId) {
        return repository.findByHorarioCentroIdAndHorarioSemestreId(centroId, semestreId);
    }

    public MapaItem buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new NaoEncontradoException("Item do mapa não encontrado."));
    }

    @Transactional
    public MapaItem criar(MapaItemReq req) {
        MapaItem m = new MapaItem();
        preencher(m, req);
        repository.save(m);
        auditoria.registrar("CADASTRO", "MAPA_FINAL", m.getId(), descrever(m));
        return m;
    }

    @Transactional
    public MapaItem atualizar(Long id, MapaItemReq req) {
        MapaItem m = buscar(id);
        preencher(m, req);
        auditoria.registrar("EDICAO", "MAPA_FINAL", id, descrever(m));
        return m;
    }

    @Transactional
    public MapaItem inativar(Long id) {
        MapaItem m = buscar(id);
        m.setStatus(Status.INATIVO);
        auditoria.registrar("INATIVACAO", "MAPA_FINAL", id, descrever(m));
        return m;
    }

    private void preencher(MapaItem m, MapaItemReq req) {
        Docente docente = docenteService.buscar(req.docenteId());
        Disciplina disciplina = disciplinaService.buscar(req.disciplinaId());
        HorarioDisponivel horario = horarioService.buscar(req.horarioId());
        if (docente.getStatus() != Status.ATIVO) {
            throw new RegraNegocioException("O docente está inativo.");
        }
        if (disciplina.getStatus() != Status.ATIVO || horario.getStatus() != Status.ATIVO) {
            throw new RegraNegocioException("A disciplina e o horário precisam estar ativos.");
        }
        if (!disponibilidadeRepository.existsByDocenteIdAndHorarioId(docente.getId(), horario.getId())) {
            throw new RegraNegocioException(docente.getNomeCompleto() + " não marcou disponibilidade em "
                    + horario.getDia() + " " + horario.getFaixa().getDescricao() + ".");
        }
        m.setDocente(docente);
        m.setDisciplina(disciplina);
        m.setHorario(horario);
    }

    private String descrever(MapaItem m) {
        return m.getHorario().getSemestre().getCodigo() + " | " + m.getDocente().getNomeCompleto() + " | "
                + m.getDisciplina().getSigla() + " | " + m.getHorario().getDia() + " " + m.getHorario().getFaixa().getDescricao();
    }
}
