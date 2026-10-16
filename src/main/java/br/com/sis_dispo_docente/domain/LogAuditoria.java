package br.com.sis_dispo_docente.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/** Log de auditoria: toda inclusao, edicao, inativacao e exclusao gera um registro. */
@Entity
@Table(name = "log_auditoria")
@Getter
@Setter
@NoArgsConstructor
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dataHora = LocalDateTime.now();

    @Column(nullable = false, length = 60)
    private String usuario;

    @Column(nullable = false, length = 30)
    private String acao;

    @Column(nullable = false, length = 40)
    private String entidade;

    private Long entidadeId;

    @Column(length = 1000)
    private String detalhe;
}
