package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "faixa_horario")
@Getter
@Setter
@NoArgsConstructor
public class FaixaHorario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime inicio;

    @Column(nullable = false)
    private LocalTime fim;

    @Column(nullable = false)
    private int ordem;

    public FaixaHorario(LocalTime inicio, LocalTime fim, int ordem) {
        this.inicio = inicio;
        this.fim = fim;
        this.ordem = ordem;
    }

    public String getDescricao() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("HH:mm");
        return inicio.format(formato) + " - " + fim.format(formato);
    }
}
