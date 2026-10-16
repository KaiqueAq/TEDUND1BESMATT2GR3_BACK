package br.com.sis_dispo_docente.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProReitoria {

    @Column(length = 120)
    private String nome;     // ex.: Pro-Reitoria de Graduacao

    @Column(length = 150)
    private String nomeado;  // quem foi nomeado para a pro-reitoria
}
