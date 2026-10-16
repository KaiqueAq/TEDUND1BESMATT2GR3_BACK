package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Historico do docente: guarda a justificativa de cada inativacao/reativacao. */
@Entity
@Table(name = "historico_docente")
@Getter
@Setter
@NoArgsConstructor
public class HistoricoDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    @Column(nullable = false, length = 30)
    private String acao; // CADASTRO ou INATIVACAO

    @Column(length = 500)
    private String justificativa;

    @Column(length = 60)
    private String responsavel;
}
