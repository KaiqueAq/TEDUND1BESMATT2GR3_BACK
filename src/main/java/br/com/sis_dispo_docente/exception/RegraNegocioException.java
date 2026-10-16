package br.com.sis_dispo_docente.exception;

/** Violacao de regra de negocio (ex.: excluir registro com historico). Vira HTTP 409. */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
