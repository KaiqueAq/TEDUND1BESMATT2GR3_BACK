package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.CentroAcademico;
import br.com.sis_dispo_docente.domain.Ies;
import br.com.sis_dispo_docente.domain.Status;
import br.com.sis_dispo_docente.dto.Requests.CentroReq;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CentroAcademicoService {

    private final CentroAcademicoRepository repository;
    private final IesService iesService;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final HorarioDisponivelRepository horarioRepository;
    private final AuditoriaService auditoria;

    public List<CentroAcademico> listar() {
        return repository.findAllByOrderBySiglaAsc();
    }

    public CentroAcademico buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Centro acadêmico não encontrado."));
    }

    @Transactional
    public CentroAcademico criar(CentroReq req) {
        if (repository.existsBySiglaIgnoreCaseAndIesId(req.sigla(), req.iesId())) {
            throw new RegraNegocioException("Já existe um centro com a sigla " + req.sigla() + " nessa IES.");
        }
        CentroAcademico c = new CentroAcademico();
        preencher(c, req);
        repository.save(c);
        auditoria.registrar("CADASTRO", "CENTRO_ACADEMICO", c.getId(), c.getSigla() + " (" + c.getIes().getSigla() + ")");
        return c;
    }

    @Transactional
    public CentroAcademico atualizar(Long id, CentroReq req) {
        CentroAcademico c = buscar(id);
        if (repository.existsBySiglaIgnoreCaseAndIesIdAndIdNot(req.sigla(), req.iesId(), id)) {
            throw new RegraNegocioException("Já existe outro centro com a sigla " + req.sigla() + " nessa IES.");
        }
        preencher(c, req);
        auditoria.registrar("EDICAO", "CENTRO_ACADEMICO", id, c.getSigla());
        return c;
    }

    @Transactional
    public CentroAcademico inativar(Long id) {
        CentroAcademico c = buscar(id);
        c.setStatus(Status.INATIVO);
        auditoria.registrar("INATIVACAO", "CENTRO_ACADEMICO", id, c.getSigla());
        return c;
    }

    @Transactional
    public void excluir(Long id) {
        CentroAcademico c = buscar(id);
        if (cursoRepository.existsByCentroId(id) || docenteRepository.existsByCentroId(id)
                || horarioRepository.existsByCentroId(id)) {
            throw new RegraNegocioException("O centro " + c.getSigla()
                    + " possui cursos, docentes ou horários vinculados (histórico). Ele não pode ser excluído, apenas inativado.");
        }
        repository.delete(c);
        auditoria.registrar("EXCLUSAO", "CENTRO_ACADEMICO", id, c.getSigla());
    }

    private void preencher(CentroAcademico c, CentroReq req) {
        Ies ies = iesService.buscar(req.iesId()); // todo CA precisa estar atrelado a uma unica IES
        if (ies.getStatus() == Status.INATIVO && (c.getIes() == null || !c.getIes().getId().equals(ies.getId()))) {
            throw new RegraNegocioException("Não é possível vincular um centro a uma IES inativa.");
        }
        c.setSigla(req.sigla().trim().toUpperCase());
        c.setNome(req.nome().trim());
        c.setEmailCentral(req.emailCentral());
        c.setTelefoneCentral(req.telefoneCentral());
        c.setCoordenador(req.coordenador().trim()); // campo unico: 1 coordenador por centro
        c.setIes(ies);
    }
}
