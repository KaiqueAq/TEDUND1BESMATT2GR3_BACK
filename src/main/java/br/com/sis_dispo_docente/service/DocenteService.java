package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.*;
import br.com.sis_dispo_docente.dto.Requests.DocenteReq;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.DocenteRepository;
import br.com.sis_dispo_docente.repository.HistoricoDocenteRepository;
import br.com.sis_dispo_docente.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocenteService {

    private final DocenteRepository repository;
    private final HistoricoDocenteRepository historicoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CentroAcademicoService centroService;
    private final PasswordEncoder passwordEncoder;
    private final AuditoriaService auditoria;

    public List<Docente> listar() {
        return repository.findAllByOrderByNomeCompletoAsc();
    }

    public Docente buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new NaoEncontradoException("Docente não encontrado."));
    }

    public List<HistoricoDocente> historico(Long id) {
        return historicoRepository.findByDocenteIdOrderByDataHoraDesc(id);
    }

    /** Cadastra o docente e cria o usuario de acesso (login = matricula; senha inicial = matricula se nao informada). */
    @Transactional
    public Docente criar(DocenteReq req) {
        String matricula = req.matricula().trim();
        if (repository.existsByMatricula(matricula) || usuarioRepository.existsByLogin(matricula)) {
            throw new RegraNegocioException("Já existe um docente/usuário com a matrícula " + matricula + ".");
        }
        Usuario u = new Usuario();
        u.setLogin(matricula);
        String senha = (req.senhaInicial() == null || req.senhaInicial().isBlank()) ? matricula : req.senhaInicial();
        u.setSenha(passwordEncoder.encode(senha));
        Docente d = new Docente();
        d.setMatricula(matricula);
        d.setUsuario(u);
        preencher(d, req);
        repository.save(d);
        registrarHistorico(d, "CADASTRO", null);
        auditoria.registrar("CADASTRO", "DOCENTE", d.getId(), matricula + " - " + d.getNomeCompleto()
                + " perfis=" + u.getPerfis());
        return d;
    }

    @Transactional
    public Docente atualizar(Long id, DocenteReq req) {
        Docente d = buscar(id);
        if (!d.getMatricula().equals(req.matricula().trim())) {
            throw new RegraNegocioException("A matrícula não pode ser alterada (ela é o login do docente).");
        }
        preencher(d, req);
        if (req.senhaInicial() != null && !req.senhaInicial().isBlank()) {
            d.getUsuario().setSenha(passwordEncoder.encode(req.senhaInicial()));
        }
        auditoria.registrar("EDICAO", "DOCENTE", id, d.getMatricula() + " perfis=" + d.getUsuario().getPerfis());
        return d;
    }

    /** Regra do cenario: a inativacao exige justificativa, registrada no historico do docente. */
    @Transactional
    public Docente inativar(Long id, String justificativa) {
        if (justificativa == null || justificativa.isBlank()) {
            throw new RegraNegocioException("Informe a justificativa da inativação (afastamento, desligamento etc.).");
        }
        Docente d = buscar(id);
        if (d.getStatus() == Status.INATIVO) {
            throw new RegraNegocioException("O docente já está inativo.");
        }
        d.setStatus(Status.INATIVO);
        d.getUsuario().setAtivo(false); // docente inativo nao consegue mais entrar no sistema
        registrarHistorico(d, "INATIVACAO", justificativa.trim());
        auditoria.registrar("INATIVACAO", "DOCENTE", id, d.getMatricula() + " - motivo: " + justificativa.trim());
        return d;
    }

    private void preencher(Docente d, DocenteReq req) {
        CentroAcademico centro = centroService.buscar(req.centroId()); // docente vinculado a um unico CA
        d.setNomeCompleto(req.nomeCompleto().trim());
        d.setEmail(req.email());
        d.setTelefone(req.telefone());
        d.setCentro(centro);
        Usuario u = d.getUsuario();
        u.setNome(d.getNomeCompleto());
        u.getPerfis().clear();
        u.getPerfis().addAll(req.perfis()); // pode acumular mais de um perfil
    }

    private void registrarHistorico(Docente d, String acao, String justificativa) {
        HistoricoDocente h = new HistoricoDocente();
        h.setDocente(d);
        h.setAcao(acao);
        h.setJustificativa(justificativa);
        h.setResponsavel(AuditoriaService.usuarioAtual());
        historicoRepository.save(h);
    }
}
