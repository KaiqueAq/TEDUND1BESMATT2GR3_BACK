package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.Semestre;
import br.com.sis_dispo_docente.dto.Requests.SemestreReq;
import br.com.sis_dispo_docente.exception.NaoEncontradoException;
import br.com.sis_dispo_docente.exception.RegraNegocioException;
import br.com.sis_dispo_docente.repository.SemestreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SemestreService {
    private final SemestreRepository repository;
    private final AuditoriaService auditoria;

    public List<Semestre> listar() {
        return repository.findAllByOrderByCodigoDesc();
    }

    public Semestre buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NaoEncontradoException("Semestre não encontrado."));
    }

    @Transactional
    public Semestre criar(SemestreReq req) {
        String codigo = req.codigo().trim();
        if (repository.existsByCodigo(codigo)) {
            throw new RegraNegocioException("Esse semestre já está cadastrado.");
        }

        Semestre semestre = new Semestre();
        semestre.setCodigo(codigo);
        semestre.setDataCadastro(LocalDate.now());
        repository.save(semestre);
        auditoria.registrar("CADASTRO", "SEMESTRE", semestre.getId(), codigo);
        return semestre;
    }
}
