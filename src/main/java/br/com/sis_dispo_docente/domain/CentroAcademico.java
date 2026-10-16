package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/** Centro Academico (Escola). Pertence a uma unica IES e tem um unico coordenador. */
@Entity
@Table(name = "centro_academico")
@Getter
@Setter
@NoArgsConstructor
public class CentroAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String sigla;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 120)
    private String emailCentral;

    @Column(length = 30)
    private String telefoneCentral;

    @Column(nullable = false, length = 150)
    private String coordenador;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ies_id", nullable = false)
    private Ies ies;

    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ATIVO;
}
