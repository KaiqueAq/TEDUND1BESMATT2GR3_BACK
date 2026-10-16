package br.com.sis_dispo_docente.config;

import br.com.sis_dispo_docente.domain.FaixaHorario;
import br.com.sis_dispo_docente.domain.Perfil;
import br.com.sis_dispo_docente.domain.Usuario;
import br.com.sis_dispo_docente.repository.FaixaHorarioRepository;
import br.com.sis_dispo_docente.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;

/**
 * Executa ao subir o back:
 * - cria as 8 faixas de horario fixas do cenario;
 * - cria o usuario administrador (admin / admin123) se nao existir nenhum usuario.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final FaixaHorarioRepository faixaRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (faixaRepository.count() == 0) {
            int[][] faixas = {{7, 0, 8, 15}, {8, 25, 9, 40}, {9, 50, 11, 5}, {11, 15, 12, 20},
                    {13, 30, 14, 45}, {14, 55, 16, 10}, {17, 0, 18, 15}, {19, 0, 21, 30}};
            for (int i = 0; i < faixas.length; i++) {
                int[] f = faixas[i];
                faixaRepository.save(new FaixaHorario(LocalTime.of(f[0], f[1]), LocalTime.of(f[2], f[3]), i + 1));
            }
        }
        if (usuarioRepository.count() == 0) {
            Usuario admin = new Usuario();
            admin.setLogin("admin");
            admin.setNome("Administrador do Sistema");
            admin.setSenha(passwordEncoder.encode("admin123"));
            admin.getPerfis().add(Perfil.ADMINISTRADOR);
            usuarioRepository.save(admin);
        }
    }
}
