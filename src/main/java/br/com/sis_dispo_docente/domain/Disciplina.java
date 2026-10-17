package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "disciplina",
       uniqueConstraints = @UniqueConstraint(columnNames = {"sigla", "curso_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Disciplina {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String sigla;

    @Column(nullable = false, length = 200)
    private String descricao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(nullable = false)
    private int cargaHoraria;

    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ATIVO;
}
