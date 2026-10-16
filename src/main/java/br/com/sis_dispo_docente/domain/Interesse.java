package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Disciplina que o docente quer lecionar, com prioridade de 1 (maior interesse) a 5 (menor). */
@Entity
@Table(name = "interesse",
        uniqueConstraints = @UniqueConstraint(columnNames = {"docente_id", "semestre_id", "disciplina_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Interesse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "semestre_id", nullable = false)
    private Semestre semestre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "disciplina_id", nullable = false)
    private Disciplina disciplina;

    @Column(nullable = false)
    private int prioridade;
}
