package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.*;
import br.com.sis_dispo_docente.dto.Requests.InteresseItemReq;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Acoes do proprio docente logado, sempre em um semestre: disponibilidade de dias/horarios e disciplinas de interesse. */
@Service
@RequiredArgsConstructor
public class PortalDocenteService {

    private final DocenteRepository docenteRepository;
    private final DisponibilidadeRepository disponibilidadeRepository;
    private final InteresseRepository interesseRepository;
    private final HorarioService horarioService;
    private final DisciplinaService disciplinaService;
    private final SemestreService semestreService;
    private final AuditoriaService auditoria;

    public Docente docenteLogado() {
        return docenteRepository.findByUsuarioLogin(AuditoriaService.usuarioAtual())
                .orElseThrow(() -> new RegraNegocioException("O usuário logado não está cadastrado como docente."));
    }

    public List<Disponibilidade> minhaDisponibilidade(Long semestreId) {
        return disponibilidadeRepository.findByDocenteIdAndHorarioSemestreId(docenteLogado().getId(), semestreId);
    }

    /** Substitui a disponibilidade do docente. So aceita dias/horarios liberados (ativos) pelo coordenador do seu centro. */
    @Transactional
    public List<Disponibilidade> salvarDisponibilidade(Long semestreId, List<Long> horarioIds) {
        Docente d = docenteLogado();
        Semestre semestre = semestreService.buscar(semestreId);
        disponibilidadeRepository.deleteByDocenteIdAndHorarioSemestreId(d.getId(), semestreId);
        disponibilidadeRepository.flush();
        for (Long hid : new HashSet<>(horarioIds)) {
            HorarioDisponivel h = horarioService.buscar(hid);
            if (h.getStatus() != Status.ATIVO || !h.getCentro().getId().equals(d.getCentro().getId())
                    || !h.getSemestre().getId().equals(semestreId)) {
                throw new RegraNegocioException("O horário " + h.getDia() + " " + h.getFaixa().getDescricao()
                        + " não está liberado pelo coordenador do seu centro neste semestre.");
            }
            Disponibilidade disp = new Disponibilidade();
            disp.setDocente(d);
            disp.setHorario(h);
            disponibilidadeRepository.save(disp);
        }
        auditoria.registrar("EDICAO", "DISPONIBILIDADE", d.getId(),
                semestre.getCodigo() + ": " + horarioIds.size() + " horário(s) marcados");
        return disponibilidadeRepository.findByDocenteIdAndHorarioSemestreId(d.getId(), semestreId);
    }

    public List<Interesse> meusInteresses(Long semestreId) {
        return interesseRepository.findByDocenteIdAndSemestreIdOrderByPrioridadeAsc(docenteLogado().getId(), semestreId);
    }

    /** Substitui as disciplinas de interesse. Prioridade de 1 (maior interesse) a 5 (menor). */
    @Transactional
    public List<Interesse> salvarInteresses(Long semestreId, List<InteresseItemReq> itens) {
        Docente d = docenteLogado();
        Semestre semestre = semestreService.buscar(semestreId);
        Set<Long> vistos = new HashSet<>();
        for (InteresseItemReq item : itens) {
            if (item.prioridade() < 1 || item.prioridade() > 5) {
                throw new RegraNegocioException("A prioridade deve estar entre 1 e 5.");
            }
            if (!vistos.add(item.disciplinaId())) {
                throw new RegraNegocioException("A mesma disciplina foi informada mais de uma vez.");
            }
        }
        interesseRepository.deleteByDocenteIdAndSemestreId(d.getId(), semestreId);
        interesseRepository.flush();
        for (InteresseItemReq item : itens) {
            Disciplina disc = disciplinaService.buscar(item.disciplinaId());
            if (disc.getStatus() != Status.ATIVO) {
                throw new RegraNegocioException("A disciplina " + disc.getSigla() + " está inativa.");
            }
            Interesse i = new Interesse();
            i.setDocente(d);
            i.setSemestre(semestre);
            i.setDisciplina(disc);
            i.setPrioridade(item.prioridade());
            interesseRepository.save(i);
        }
        auditoria.registrar("EDICAO", "INTERESSES", d.getId(),
                semestre.getCodigo() + ": " + itens.size() + " disciplina(s) de interesse");
        return interesseRepository.findByDocenteIdAndSemestreIdOrderByPrioridadeAsc(d.getId(), semestreId);
    }
}
