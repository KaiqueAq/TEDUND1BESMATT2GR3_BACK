package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "semestre", uniqueConstraints = @UniqueConstraint(columnNames = "codigo"))
@Getter
@Setter
@NoArgsConstructor
public class Semestre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String codigo;

    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();
}
