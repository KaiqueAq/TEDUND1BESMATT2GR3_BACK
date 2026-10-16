package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.LogAuditoria;
import br.com.sis_dispo_docente.repository.LogAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/** Grava o log de auditoria (data, hora, usuario, acao e registro afetado). */
@Service
@RequiredArgsConstructor
public class AuditoriaService {

    private final LogAuditoriaRepository repository;

    public static String usuarioAtual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth == null || auth.getName() == null) ? "sistema" : auth.getName();
    }

    public void registrar(String acao, String entidade, Long entidadeId, String detalhe) {
        LogAuditoria log = new LogAuditoria();
        log.setUsuario(usuarioAtual());
        log.setAcao(acao);
        log.setEntidade(entidade);
        log.setEntidadeId(entidadeId);
        log.setDetalhe(detalhe == null ? null : detalhe.length() > 1000 ? detalhe.substring(0, 1000) : detalhe);
        repository.save(log);
    }
}
