package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Dia/horario que o docente marcou como disponivel. */
@Entity
@Table(name = "disponibilidade",
        uniqueConstraints = @UniqueConstraint(columnNames = {"docente_id", "horario_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Disponibilidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "horario_id", nullable = false)
    private HorarioDisponivel horario;
}
