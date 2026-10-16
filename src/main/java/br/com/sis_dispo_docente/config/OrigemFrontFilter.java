package br.com.sis_dispo_docente.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Restricao arquitetural do cenario: o BACK-END so responde a requisicoes vindas do FRONT-END.
 * O navegador sempre envia o cabecalho Origin quando a tela Angular chama a API.
 * Chamadas diretas (Postman, curl, navegador digitando a URL, outro site) nao trazem a origem
 * da tela oficial e sao barradas e descartadas aqui, antes de chegar em qualquer controller.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class OrigemFrontFilter extends OncePerRequestFilter {

    private final Set<String> origensPermitidas;

    public OrigemFrontFilter(@Value("${app.front-url}") String frontUrl) {
        this.origensPermitidas = Arrays.stream(frontUrl.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toSet());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String origem = request.getHeader("Origin");
        if (origem == null || !origensPermitidas.contains(origem)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"mensagem\":\"Acesso negado: o servidor só aceita requisições da tela oficial do sistema.\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}
