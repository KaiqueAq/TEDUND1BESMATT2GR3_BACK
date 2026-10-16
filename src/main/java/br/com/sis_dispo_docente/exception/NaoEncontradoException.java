package br.com.sis_dispo_docente.exception;

/** Registro inexistente. Vira HTTP 404. */
public class NaoEncontradoException extends RuntimeException {
    public NaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
