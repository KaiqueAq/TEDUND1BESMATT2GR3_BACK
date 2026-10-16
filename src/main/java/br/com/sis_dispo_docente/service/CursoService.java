package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.CentroAcademico;
import br.com.sis_dispo_docente.domain.Curso;
import br.com.sis_dispo_docente.domain.Status;
import br.com.sis_dispo_docente.dto.Requests.CursoReq;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.CursoRepository;
import br.com.sis_dispo_docente.repository.DisciplinaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CursoService {

    private final CursoRepository repository;
    private final CentroAcademicoService centroService;
    private final DisciplinaRepository disciplinaRepository;
    private final AuditoriaService auditoria;

    public List<Curso> listar(Long centroId) {
        return centroId == null ? repository.findAllByOrderBySiglaAsc() : repository.findByCentroIdOrderBySiglaAsc(centroId);
    }

    public Curso buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new NaoEncontradoException("Curso não encontrado."));
    }

    @Transactional
    public Curso criar(CursoReq req) {
        Curso c = new Curso();
        preencher(c, req);
        repository.save(c);
        auditoria.registrar("CADASTRO", "CURSO", c.getId(), c.getSigla() + " - " + c.getDescricao());
        return c;
    }

    @Transactional
    public Curso atualizar(Long id, CursoReq req) {
        Curso c = buscar(id);
        preencher(c, req);
        auditoria.registrar("EDICAO", "CURSO", id, c.getSigla());
        return c;
    }

    @Transactional
    public Curso inativar(Long id) {
        Curso c = buscar(id);
        c.setStatus(Status.INATIVO);
        auditoria.registrar("INATIVACAO", "CURSO", id, c.getSigla());
        return c;
    }

    @Transactional
    public void excluir(Long id) {
        Curso c = buscar(id);
        if (disciplinaRepository.existsByCursoId(id)) {
            throw new RegraNegocioException("O curso " + c.getSigla()
                    + " possui disciplinas vinculadas (histórico). Ele não pode ser excluído, apenas inativado.");
        }
        repository.delete(c);
        auditoria.registrar("EXCLUSAO", "CURSO", id, c.getSigla());
    }

    private void preencher(Curso c, CursoReq req) {
        CentroAcademico centro = centroService.buscar(req.centroId()); // curso atrelado a um unico CA
        if (centro.getStatus() == Status.INATIVO && (c.getCentro() == null || !c.getCentro().getId().equals(centro.getId()))) {
            throw new RegraNegocioException("Não é possível vincular um curso a um centro inativo.");
        }
        c.setSigla(req.sigla().trim().toUpperCase());
        c.setDescricao(req.descricao().trim());
        c.setCentro(centro);
        c.getTurnos().clear();
        c.getTurnos().addAll(req.turnos());
        c.setCoordenador(req.coordenador().trim());
    }
}
