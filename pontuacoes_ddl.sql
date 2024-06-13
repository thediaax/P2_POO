-- Generated by the database client.
CREATE TABLE pontuacoes(
    id SERIAL NOT NULL,
    usuario_id integer NOT NULL,
    sessao_id integer NOT NULL,
    cacadas integer DEFAULT 0,
    sonos integer DEFAULT 0,
    pontos integer DEFAULT 0,
    PRIMARY KEY(id),
    CONSTRAINT pontuacoes_usuario_id_fkey FOREIGN key(usuario_id) REFERENCES usuarios(id)
);

ALTER TABLE pontuacoes ADD COLUMN data_jogo TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;