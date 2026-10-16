package br.com.sis_dispo_docente.controller;

import br.com.sis_dispo_docente.domain.Docente;
import br.com.sis_dispo_docente.domain.Usuario;
import br.com.sis_dispo_docente.dto.Responses.UsuarioLogadoDto;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.repository.DocenteRepository;
import br.com.sis_dispo_docente.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Login: a tela chama /api/auth/me com o login/senha; se der 200, o usuario esta autenticado. */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;

    @GetMapping("/me")
    public UsuarioLogadoDto me(Authentication auth) {
        Usuario u = usuarioRepository.findByLogin(auth.getName())
                .orElseThrow(() -> new NaoEncontradoException("Usuário não encontrado."));
        Docente d = docenteRepository.findByUsuarioLogin(u.getLogin()).orElse(null);
        return new UsuarioLogadoDto(u.getLogin(), u.getNome(), u.getPerfis(),
                d == null ? null : d.getId(),
                d == null ? null : d.getCentro().getId(),
                d == null ? null : d.getCentro().getSigla());
    }
}
