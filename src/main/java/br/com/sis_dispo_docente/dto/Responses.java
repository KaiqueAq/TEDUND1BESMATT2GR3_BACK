package br.com.sis_dispo_docente.dto;

import br.com.sis_dispo_docente.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/** Objetos devolvidos pela API (nunca devolvemos a entidade direto, para nao expor senha etc.). */
public final class Responses {

    private Responses() {
    }

    public record ProReitoriaDto(String nome, String nomeado) {
    }

    public record IesDto(Long id, String sigla, String nome, String emailCentral, String telefoneCentral,
                         String reitor, List<ProReitoriaDto> proReitorias, LocalDate dataCadastro, Status status) {
        public static IesDto de(Ies i) {
            return new IesDto(i.getId(), i.getSigla(), i.getNome(), i.getEmailCentral(), i.getTelefoneCentral(),
                    i.getReitor(),
                    i.getProReitorias().stream().map(p -> new ProReitoriaDto(p.getNome(), p.getNomeado())).toList(),
                    i.getDataCadastro(), i.getStatus());
        }
    }

    public record CentroDto(Long id, String sigla, String nome, String emailCentral, String telefoneCentral,
                            String coordenador, Long iesId, String iesSigla, LocalDate dataCadastro, Status status) {
        public static CentroDto de(CentroAcademico c) {
            return new CentroDto(c.getId(), c.getSigla(), c.getNome(), c.getEmailCentral(), c.getTelefoneCentral(),
                    c.getCoordenador(), c.getIes().getId(), c.getIes().getSigla(), c.getDataCadastro(), c.getStatus());
        }
    }

    public record CursoDto(Long id, String sigla, String descricao, Long centroId, String centroSigla,
                           Set<Turno> turnos, String coordenador, LocalDate dataCadastro, Status status) {
        public static CursoDto de(Curso c) {
            return new CursoDto(c.getId(), c.getSigla(), c.getDescricao(), c.getCentro().getId(),
                    c.getCentro().getSigla(), Set.copyOf(c.getTurnos()), c.getCoordenador(), c.getDataCadastro(),
                    c.getStatus());
        }
    }

    public record DocenteDto(Long id, String matricula, String nomeCompleto, String email, String telefone,
                             Long centroId, String centroSigla, String login, Set<Perfil> perfis,
                             LocalDate dataCadastro, Status status) {
        public static DocenteDto de(Docente d) {
            return new DocenteDto(d.getId(), d.getMatricula(), d.getNomeCompleto(), d.getEmail(), d.getTelefone(),
                    d.getCentro().getId(), d.getCentro().getSigla(), d.getUsuario().getLogin(),
                    Set.copyOf(d.getUsuario().getPerfis()), d.getDataCadastro(), d.getStatus());
        }
    }

    public record HistoricoDto(LocalDateTime dataHora, String acao, String justificativa, String responsavel) {
        public static HistoricoDto de(HistoricoDocente h) {
            return new HistoricoDto(h.getDataHora(), h.getAcao(), h.getJustificativa(), h.getResponsavel());
        }
    }

    public record FaixaDto(Long id, String inicio, String fim, int ordem, String descricao) {
        public static FaixaDto de(FaixaHorario f) {
            return new FaixaDto(f.getId(), f.getInicio().toString(), f.getFim().toString(), f.getOrdem(),
                    f.getDescricao());
        }
    }

    public record SemestreDto(Long id, String codigo, LocalDate dataCadastro) {
        public static SemestreDto de(Semestre s) {
            return new SemestreDto(s.getId(), s.getCodigo(), s.getDataCadastro());
        }
    }

    public record HorarioDto(Long id, Long centroId, Long semestreId, String semestre, DiaSemana dia, Long faixaId,
                             String faixa, int ordem, Status status) {
        public static HorarioDto de(HorarioDisponivel h) {
            return new HorarioDto(h.getId(), h.getCentro().getId(), h.getSemestre().getId(),
                    h.getSemestre().getCodigo(), h.getDia(), h.getFaixa().getId(),
                    h.getFaixa().getDescricao(), h.getFaixa().getOrdem(), h.getStatus());
        }

        public static final Comparator<HorarioDto> ORDEM =
                Comparator.comparing(HorarioDto::dia).thenComparingInt(HorarioDto::ordem);
    }

    public record DisciplinaDto(Long id, String sigla, String descricao, Long cursoId, String cursoSigla,
                                Long centroId, int cargaHoraria, LocalDate dataCadastro, Status status) {
        public static DisciplinaDto de(Disciplina d) {
            return new DisciplinaDto(d.getId(), d.getSigla(), d.getDescricao(), d.getCurso().getId(),
                    d.getCurso().getSigla(), d.getCurso().getCentro().getId(), d.getCargaHoraria(),
                    d.getDataCadastro(), d.getStatus());
        }
    }

    public record InteresseDto(Long disciplinaId, String sigla, String descricao, String cursoSigla, int prioridade) {
        public static InteresseDto de(Interesse i) {
            Disciplina d = i.getDisciplina();
            return new InteresseDto(d.getId(), d.getSigla(), d.getDescricao(), d.getCurso().getSigla(),
                    i.getPrioridade());
        }
    }

    public record MapaItemDto(Long id, String semestre, Long docenteId, String docenteNome, Long disciplinaId,
                              String disciplina, Long horarioId, DiaSemana dia, String faixa, int ordem, Status status) {
        public static MapaItemDto de(MapaItem m) {
            return new MapaItemDto(m.getId(), m.getHorario().getSemestre().getCodigo(),
                    m.getDocente().getId(), m.getDocente().getNomeCompleto(),
                    m.getDisciplina().getId(), m.getDisciplina().getSigla() + " - " + m.getDisciplina().getDescricao(),
                    m.getHorario().getId(), m.getHorario().getDia(), m.getHorario().getFaixa().getDescricao(),
                    m.getHorario().getFaixa().getOrdem(), m.getStatus());
        }
    }

    public record LogDto(Long id, LocalDateTime dataHora, String usuario, String acao, String entidade,
                         Long entidadeId, String detalhe) {
        public static LogDto de(LogAuditoria l) {
            return new LogDto(l.getId(), l.getDataHora(), l.getUsuario(), l.getAcao(), l.getEntidade(),
                    l.getEntidadeId(), l.getDetalhe());
        }
    }

    public record UsuarioLogadoDto(String login, String nome, Set<Perfil> perfis, Long docenteId, Long centroId,
                                   String centroSigla) {
    }

    /** Cabecalho comum dos relatorios: quem emitiu e quando. */
    public record Emissao(String emitidoPor, String login, LocalDateTime emitidoEm) {
    }

    public record Relatorio<T>(String titulo, Emissao emissao, T dados) {
    }
}
