package br.com.sis_dispo_docente.service;

import br.com.sis_dispo_docente.domain.*;
import br.com.sis_dispo_docente.dto.Responses.*;
import br.com.sis_dispo_docente.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/** Relatorios por perfil. Todos trazem a identificacao de quem emitiu e quando. */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelatorioService {

    private final IesRepository iesRepository;
    private final CentroAcademicoRepository centroRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final HorarioDisponivelRepository horarioRepository;
    private final DisponibilidadeRepository disponibilidadeRepository;
    private final InteresseRepository interesseRepository;
    private final UsuarioRepository usuarioRepository;
    private final PortalDocenteService portalDocenteService;
    private final SemestreService semestreService;

    public record CentroComCursos(CentroDto centro, List<CursoDto> cursos) {
    }

    public record IesEstrutura(IesDto ies, List<CentroComCursos> centros) {
    }

    public record CentroComDocentes(CentroDto centro, List<DocenteDto> docentes) {
    }

    public record DocenteDisponibilidade(DocenteDto docente, List<HorarioDto> horarios, List<InteresseDto> interesses) {
    }

    private Emissao emissao() {
        String login = AuditoriaService.usuarioAtual();
        String nome = usuarioRepository.findByLogin(login).map(Usuario::getNome).orElse(login);
        return new Emissao(nome, login, LocalDateTime.now());
    }

    /** Administrador: IES, centros academicos e cursos com as vinculacoes. */
    public Relatorio<List<IesEstrutura>> estrutura() {
        List<IesEstrutura> dados = iesRepository.findAllByOrderBySiglaAsc().stream()
                .map(ies -> new IesEstrutura(IesDto.de(ies),
                        centroRepository.findByIesIdOrderBySiglaAsc(ies.getId()).stream()
                                .map(c -> new CentroComCursos(CentroDto.de(c),
                                        cursoRepository.findByCentroIdOrderBySiglaAsc(c.getId()).stream()
                                                .map(CursoDto::de).toList()))
                                .toList()))
                .toList();
        return new Relatorio<>("Estrutura cadastral (IES, Centros Acadêmicos e Cursos)", emissao(), dados);
    }

    /** Administrador: docentes por centro academico. */
    public Relatorio<List<CentroComDocentes>> docentesPorCentro() {
        List<CentroComDocentes> dados = centroRepository.findAllByOrderBySiglaAsc().stream()
                .map(c -> new CentroComDocentes(CentroDto.de(c),
                        docenteRepository.findByCentroIdOrderByNomeCompletoAsc(c.getId()).stream()
                                .map(DocenteDto::de).toList()))
                .toList();
        return new Relatorio<>("Cadastro docente por Centro Acadêmico", emissao(), dados);
    }

    /** Colaborador: dias e horarios cadastrados para o centro no semestre. */
    public Relatorio<List<HorarioDto>> horarios(Long centroId, Long semestreId) {
        String sem = semestreService.buscar(semestreId).getCodigo();
        List<HorarioDto> dados = horarioRepository.findByCentroIdAndSemestreId(centroId, semestreId).stream()
                .map(HorarioDto::de).sorted(HorarioDto.ORDEM).toList();
        return new Relatorio<>("Dias e horários cadastrados — semestre " + sem,
                emissao(), dados);
    }

    /** Colaborador: mapa de disponibilidade com base no que os docentes lancaram no semestre. */
    public Relatorio<List<DocenteDisponibilidade>> mapaDisponibilidade(Long centroId, Long semestreId) {
        String sem = semestreService.buscar(semestreId).getCodigo();
        return new Relatorio<>("Mapa de disponibilidade docente — semestre " + sem,
                emissao(), escolhasDocentes(centroId, semestreId));
    }

    /** O que cada docente ativo do centro lancou no semestre (usado na tela do mapa final, sem gerar log). */
    public List<DocenteDisponibilidade> escolhasDocentes(Long centroId, Long semestreId) {
        return docenteRepository.findByCentroIdOrderByNomeCompletoAsc(centroId).stream()
                .filter(d -> d.getStatus() == Status.ATIVO)
                .map(d -> disponibilidadeDe(d, semestreId))
                .toList();
    }

    /** Usuario (docente): os proprios dias, horarios e disciplinas registrados no semestre. */
    public Relatorio<DocenteDisponibilidade> meu(Long semestreId) {
        String sem = semestreService.buscar(semestreId).getCodigo();
        Docente d = portalDocenteService.docenteLogado();
        return new Relatorio<>("Minha disponibilidade — semestre " + sem, emissao(),
                disponibilidadeDe(d, semestreId));
    }

    private DocenteDisponibilidade disponibilidadeDe(Docente d, Long semestreId) {
        return new DocenteDisponibilidade(DocenteDto.de(d),
                disponibilidadeRepository.findByDocenteIdAndHorarioSemestreId(d.getId(), semestreId).stream()
                        .map(x -> HorarioDto.de(x.getHorario())).sorted(HorarioDto.ORDEM).toList(),
                interesseRepository.findByDocenteIdAndSemestreIdOrderByPrioridadeAsc(d.getId(), semestreId).stream()
                        .map(InteresseDto::de).toList());
    }
}
