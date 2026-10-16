package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Instituicao de Ensino Superior. O campo unico "reitor" garante 1 reitor por IES. */
@Entity
@Table(name = "ies")
@Getter
@Setter
@NoArgsConstructor
public class Ies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String sigla;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 120)
    private String emailCentral;

    @Column(length = 30)
    private String telefoneCentral;

    @Column(nullable = false, length = 150)
    private String reitor;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ies_pro_reitoria", joinColumns = @JoinColumn(name = "ies_id"))
    @OrderColumn(name = "ordem")
    private List<ProReitoria> proReitorias = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Status status = Status.ATIVO;
}
