package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "horario_disponivel",
       uniqueConstraints = @UniqueConstraint(
           columnNames = {"centro_id", "semestre_id", "dia", "faixa_id"}))
@Getter
@Setter
@NoArgsConstructor
public class HorarioDisponivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "centro_id", nullable = false)
    private CentroAcademico centro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "semestre_id", nullable = false)
    private Semestre semestre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DiaSemana dia;

    @ManyToOne(optional = false)
    @JoinColumn(name = "faixa_id", nullable = false)
    private FaixaHorario faixa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ATIVO;
}
