-- Active: 1717729096588@@pg-38f1bace-henrique.f.aivencloud.com@13284@defaultdb@public
CREATE TABLE tb_atividade (
    cod_atividade INT PRIMARY KEY,
    descricao VARCHAR(200) CHECK (descricao IN ('comer', 'dormir', 'caçar')),
    data_de_ocorrencia TIMESTAMP
);