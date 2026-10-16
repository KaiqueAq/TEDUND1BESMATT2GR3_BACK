package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.Ies;
import br.com.sis_dispo_docente.domain.ProReitoria;
import br.com.sis_dispo_docente.domain.Status;
import br.com.sis_dispo_docente.dto.Requests.IesReq;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.CentroAcademicoRepository;
import br.com.sis_dispo_docente.repository.IesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IesService {

    private final IesRepository repository;
    private final CentroAcademicoRepository centroRepository;
    private final AuditoriaService auditoria;

    public List<Ies> listar() {
        return repository.findAllByOrderBySiglaAsc();
    }

    public Ies buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new NaoEncontradoException("IES não encontrada."));
    }

    @Transactional
    public Ies criar(IesReq req) {
        if (repository.existsBySiglaIgnoreCase(req.sigla())) {
            throw new RegraNegocioException("Já existe uma IES com a sigla " + req.sigla() + ".");
        }
        Ies ies = new Ies();
        preencher(ies, req);
        repository.save(ies);
        auditoria.registrar("CADASTRO", "IES", ies.getId(), ies.getSigla() + " - " + ies.getNome());
        return ies;
    }

    @Transactional
    public Ies atualizar(Long id, IesReq req) {
        Ies ies = buscar(id);
        if (repository.existsBySiglaIgnoreCaseAndIdNot(req.sigla(), id)) {
            throw new RegraNegocioException("Já existe outra IES com a sigla " + req.sigla() + ".");
        }
        preencher(ies, req);
        auditoria.registrar("EDICAO", "IES", id, ies.getSigla() + " - " + ies.getNome());
        return ies;
    }

    @Transactional
    public Ies inativar(Long id) {
        Ies ies = buscar(id);
        ies.setStatus(Status.INATIVO);
        auditoria.registrar("INATIVACAO", "IES", id, ies.getSigla());
        return ies;
    }

    /** Regra do cenario: a IES nao pode ser excluida se tiver historico vinculado; so inativada. */
    @Transactional
    public void excluir(Long id) {
        Ies ies = buscar(id);
        if (centroRepository.existsByIesId(id)) {
            throw new RegraNegocioException("A IES " + ies.getSigla()
                    + " possui centros acadêmicos vinculados (histórico). Ela não pode ser excluída, apenas inativada.");
        }
        repository.delete(ies);
        auditoria.registrar("EXCLUSAO", "IES", id, ies.getSigla());
    }

    private void preencher(Ies ies, IesReq req) {
        ies.setSigla(req.sigla().trim().toUpperCase());
        ies.setNome(req.nome().trim());
        ies.setEmailCentral(req.emailCentral());
        ies.setTelefoneCentral(req.telefoneCentral());
        ies.setReitor(req.reitor().trim()); // campo unico: so existe 1 reitor por IES
        ies.getProReitorias().clear();
        if (req.proReitorias() != null) {
            req.proReitorias().forEach(p -> ies.getProReitorias().add(new ProReitoria(p.nome(), p.nomeado())));
        }
    }
}
