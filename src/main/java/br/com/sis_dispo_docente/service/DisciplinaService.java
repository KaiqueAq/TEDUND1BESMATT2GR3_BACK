package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.Curso;
import br.com.sis_dispo_docente.domain.Disciplina;
import br.com.sis_dispo_docente.domain.Status;
import br.com.sis_dispo_docente.dto.Requests.DisciplinaReq;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.DisciplinaRepository;
import br.com.sis_dispo_docente.repository.InteresseRepository;
import br.com.sis_dispo_docente.repository.MapaItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplinaService {
    private final DisciplinaRepository repository;
    private final CursoService cursoService;
    private final InteresseRepository interesseRepository;
    private final MapaItemRepository mapaItemRepository;
    private final AuditoriaService auditoria;

    public List<Disciplina> listar(Long cursoId) {
        return cursoId == null ? repository.findAllByOrderBySiglaAsc()
                : repository.findByCursoIdOrderBySiglaAsc(cursoId);
    }

    public Disciplina buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Disciplina não encontrada."));
    }

    @Transactional
    public Disciplina criar(DisciplinaReq req) {
        Disciplina disciplina = new Disciplina();
        preencher(disciplina, req);
        disciplina.setDataCadastro(LocalDate.now());
        repository.save(disciplina);
        auditoria.registrar("CADASTRO", "DISCIPLINA", disciplina.getId(),
                disciplina.getSigla() + " - " + disciplina.getDescricao());
        return disciplina;
    }

    @Transactional
    public Disciplina atualizar(Long id, DisciplinaReq req) {
        Disciplina disciplina = buscar(id);
        preencher(disciplina, req);
        auditoria.registrar("EDICAO", "DISCIPLINA", id, disciplina.getSigla());
        return disciplina;
    }

    @Transactional
    public Disciplina inativar(Long id) {
        Disciplina disciplina = buscar(id);
        disciplina.setStatus(Status.INATIVO);
        auditoria.registrar("INATIVACAO", "DISCIPLINA", id, disciplina.getSigla());
        return disciplina;
    }

    @Transactional
    public void excluir(Long id) {
        Disciplina disciplina = buscar(id);
        if (interesseRepository.existsByDisciplinaId(id)
                || mapaItemRepository.existsByDisciplinaId(id)) {
            throw new RegraNegocioException(
                    "A disciplina possui vínculos ou histórico. Ela não pode ser excluída; apenas inativada.");
        }
        repository.delete(disciplina);
        auditoria.registrar("EXCLUSAO", "DISCIPLINA", id, disciplina.getSigla());
    }

    private void preencher(Disciplina disciplina, DisciplinaReq req) {
        String sigla = req.sigla().trim().toUpperCase();
        Curso curso = cursoService.buscar(req.cursoId());

        if (curso.getStatus() != Status.ATIVO) {
            throw new RegraNegocioException("Não é possível vincular uma disciplina a um curso inativo.");
        }

        boolean duplicada = disciplina.getId() == null
                ? repository.existsBySiglaIgnoreCaseAndCursoId(sigla, curso.getId())
                : repository.existsBySiglaIgnoreCaseAndCursoIdAndIdNot(
                        sigla, curso.getId(), disciplina.getId());

        if (duplicada) {
            throw new RegraNegocioException("Já existe uma disciplina com essa sigla nesse curso.");
        }

        disciplina.setSigla(sigla);
        disciplina.setDescricao(req.descricao().trim());
        disciplina.setCurso(curso);
        disciplina.setCargaHoraria(req.cargaHoraria());
    }
}
