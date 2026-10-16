package br.com.sis_dispo_docente.dto;

import br.com.sis_dispo_docente.domain.DiaSemana;
import br.com.sis_dispo_docente.domain.Perfil;
import br.com.sis_dispo_docente.domain.Turno;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Set;

/** Corpos (JSON) recebidos pela API. */
public final class Requests {

    private Requests() {
    }

    public record ProReitoriaReq(@NotBlank String nome, String nomeado) {
    }

    public record IesReq(
            @NotBlank @Size(max = 20) String sigla,
            @NotBlank String nome,
            @Email String emailCentral,
            String telefoneCentral,
            @NotBlank String reitor,
            List<@Valid ProReitoriaReq> proReitorias) {
    }

    public record CentroReq(
            @NotBlank @Size(max = 20) String sigla,
            @NotBlank String nome,
            @Email String emailCentral,
            String telefoneCentral,
            @NotBlank String coordenador,
            @NotNull Long iesId) {
    }

    public record CursoReq(
            @NotBlank @Size(max = 20) String sigla,
            @NotBlank String descricao,
            @NotNull Long centroId,
            @NotEmpty Set<Turno> turnos,
            @NotBlank String coordenador) {
    }

    public record DocenteReq(
            @NotBlank String matricula,
            @NotBlank String nomeCompleto,
            @Email String email,
            String telefone,
            @NotNull Long centroId,
            @NotEmpty Set<Perfil> perfis,
            String senhaInicial) {
    }

    public record JustificativaReq(@NotBlank(message = "a justificativa é obrigatória") String justificativa) {
    }

    public record CelulaHorarioReq(@NotNull DiaSemana dia, @NotNull Long faixaId, boolean ativo) {
    }

    public record SemestreReq(
            @NotBlank @Pattern(regexp = "\\d{4}\\.[12]", message = "use o formato ANO.1 ou ANO.2 (ex.: 2027.1)")
            String codigo) {
    }

    public record GradeHorariosReq(@NotNull Long centroId, @NotNull Long semestreId,
                                   @NotNull List<@Valid CelulaHorarioReq> celulas) {
    }

    public record DisciplinaReq(
            @NotBlank @Size(max = 20) String sigla,
            @NotBlank String descricao,
            @NotNull Long cursoId,
            @NotNull @Min(1) Integer cargaHoraria) {
    }

    public record DisponibilidadeReq(@NotNull Long semestreId, @NotNull List<Long> horarioIds) {
    }

    public record InteresseItemReq(
            @NotNull Long disciplinaId,
            @NotNull @Min(value = 1, message = "a prioridade mínima é 1")
            @Max(value = 5, message = "a prioridade máxima é 5") Integer prioridade) {
    }

    public record InteressesReq(@NotNull Long semestreId, @NotNull List<@Valid InteresseItemReq> itens) {
    }

    public record MapaItemReq(@NotNull Long docenteId, @NotNull Long disciplinaId, @NotNull Long horarioId) {
    }
}
