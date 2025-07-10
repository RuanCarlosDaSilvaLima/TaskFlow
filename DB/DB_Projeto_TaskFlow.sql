CREATE DATABASE IF NOT EXISTS DB_TaskFlow;
USE DB_TaskFlow;

-- USUÁRIO
CREATE TABLE Usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    foto_perfil BLOB
);

-- PROJETO
CREATE TABLE Projeto (
    id_projeto INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    data_inicio DATE NOT NULL,
    prazo DATE,
    status_projeto ENUM('ativo', 'arquivado', 'concluido', 'atrasado') NOT NULL DEFAULT 'ativo'
);

-- EQUIPE DO PROJETO
CREATE TABLE EquipeProjeto (
    id_usuario INT NOT NULL,
    id_projeto INT NOT NULL,
    papel ENUM('lider', 'membro') NOT NULL DEFAULT 'membro',
    data_entrada DATE NOT NULL DEFAULT (CURRENT_DATE),
    PRIMARY KEY (id_usuario, id_projeto),
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (id_projeto) REFERENCES Projeto(id_projeto)
);

-- META (sem descricao)
CREATE TABLE Meta (
    id_meta INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prazo DATE,
    id_chefe INT NOT NULL,
    id_projeto INT NOT NULL,
    FOREIGN KEY (id_chefe) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (id_projeto) REFERENCES Projeto(id_projeto)
);

-- TAREFA
CREATE TABLE Tarefa (
    id_tarefa INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    status ENUM('pendente', 'em_andamento', 'concluida', 'cancelada') NOT NULL DEFAULT 'pendente',
    data_criacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prazo DATE,
    id_responsavel INT,
    id_meta INT NOT NULL,
    FOREIGN KEY (id_responsavel) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (id_meta) REFERENCES Meta(id_meta)
);

-- COMENTÁRIO
CREATE TABLE Comentario (
    id_comentario INT AUTO_INCREMENT PRIMARY KEY,
    texto TEXT NOT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL,
    id_tarefa INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (id_tarefa) REFERENCES Tarefa(id_tarefa)
);

-- LOG DE AÇÕES (ligado à meta)
CREATE TABLE Log (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    acao VARCHAR(255) NOT NULL,
    data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL,
    id_meta INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (id_meta) REFERENCES Meta(id_meta)
);

use db_taskflow;

select * from projeto;

delete from projeto where id_projeto=3;


