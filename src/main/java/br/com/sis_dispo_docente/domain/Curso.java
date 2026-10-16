package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/** Curso. Pertence obrigatoriamente a um unico Centro Academico. */
@Entity
@Table(name = "curso")
@Getter
@Setter
@NoArgsConstructor
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String sigla;

    @Column(nullable = false, length = 150)
    private String descricao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "centro_id", nullable = false)
    private CentroAcademico centro;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "curso_turno", joinColumns = @JoinColumn(name = "curso_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "turno", length = 15)
    private Set<Turno> turnos = new HashSet<>();

    @Column(nullable = false, length = 150)
    private String coordenador;

    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ATIVO;
}
