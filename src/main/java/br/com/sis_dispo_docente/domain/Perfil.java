package br.com.sis_dispo_docente.domain;

/** Perfis de acesso do cenario. Um usuario pode acumular mais de um. */
public enum Perfil {
    ADMINISTRADOR, // gerencia IES, centros, cursos, docentes, logs e relatorios gerais
    COLABORADOR,   // coordenador de CA: dias/horarios, disciplinas e mapa final
    USUARIO        // docente: informa disponibilidade e disciplinas de interesse
}
