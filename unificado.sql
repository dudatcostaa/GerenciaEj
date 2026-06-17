SET FOREIGN_KEY_CHECKS=0;

CREATE TABLE IF NOT EXISTS `empresa_junior` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cnpj` varchar(18) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('PENDENTE','ATIVA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDENTE',
  PRIMARY KEY (`id`),
  UNIQUE KEY `cnpj` (`cnpj`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `gasto_mensal` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `data` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `leads` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome_cliente` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status_lead` enum('PROSPECCAO','NEGOCIACAO','FECHADO','PERDIDO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PROSPECCAO',
  `data_criacao` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `data_ultima_modificacao` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `proposta` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome_cliente` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `valor` decimal(10,2) NOT NULL,
  `data` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `projeto` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` text COLLATE utf8mb4_unicode_ci,
  `data_inicio` date NOT NULL DEFAULT (curdate()),
  `status` enum('EM_PLANEJAMENTO','EM_EXECUCAO','FINALIZADO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'EM_PLANEJAMENTO',
  `valor` decimal(10,2) NOT NULL DEFAULT '0.00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `usuario` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(150) COLLATE utf8mb4_unicode_ci NOT NULL,
  `senha` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `cargo` enum('MEMBRO','DIRETOR') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MEMBRO',
  `empresa_junior_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `empresa_junior_id` (`empresa_junior_id`),
  CONSTRAINT `usuario_ibfk_1` FOREIGN KEY (`empresa_junior_id`) REFERENCES `empresa_junior` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `arquivo` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `autor_id` bigint NOT NULL,
  `data_upload` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `autor_id` (`autor_id`),
  CONSTRAINT `arquivo_ibfk_1` FOREIGN KEY (`autor_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `candidatura_ej` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint NOT NULL,
  `empresa_junior_id` bigint NOT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT 'PENDENTE',
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `empresa_junior_id` (`empresa_junior_id`),
  CONSTRAINT `candidatura_ej_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `candidatura_ej_ibfk_2` FOREIGN KEY (`empresa_junior_id`) REFERENCES `empresa_junior` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `evento` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descricao` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `data` datetime NOT NULL,
  `empresa_junior_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `empresa_junior_id` (`empresa_junior_id`),
  CONSTRAINT `evento_ibfk_1` FOREIGN KEY (`empresa_junior_id`) REFERENCES `empresa_junior` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `evento_convidado` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `evento_id` bigint NOT NULL,
  `usuario_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_evento_convidado` (`evento_id`,`usuario_id`),
  KEY `usuario_id` (`usuario_id`),
  CONSTRAINT `evento_convidado_ibfk_1` FOREIGN KEY (`evento_id`) REFERENCES `evento` (`id`) ON DELETE CASCADE,
  CONSTRAINT `evento_convidado_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `metrica` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `valor` double NOT NULL,
  `data` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `empresa_junior_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `empresa_junior_id` (`empresa_junior_id`),
  CONSTRAINT `metrica_ibfk_1` FOREIGN KEY (`empresa_junior_id`) REFERENCES `empresa_junior` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `quadro_kamban` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contador_tarefas` bigint NOT NULL DEFAULT '1',
  `projeto_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `projeto_id` (`projeto_id`),
  CONSTRAINT `quadro_kamban_ibfk_1` FOREIGN KEY (`projeto_id`) REFERENCES `projeto` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `solicitacao_ej` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` enum('PENDENTE','APROVADA','REPROVADA') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDENTE',
  `documento_url` varchar(500) COLLATE utf8mb4_unicode_ci NOT NULL,
  `data_solicitacao` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `usuario_id` bigint NOT NULL,
  `empresa_junior_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `empresa_junior_id` (`empresa_junior_id`),
  CONSTRAINT `solicitacao_ej_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE,
  CONSTRAINT `solicitacao_ej_ibfk_2` FOREIGN KEY (`empresa_junior_id`) REFERENCES `empresa_junior` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `tarefa` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `titulo` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('PENDENTE','EM_PROGRESSO','EM_REVISAO','PRONTO') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDENTE',
  `quadro_kamban_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `quadro_kamban_id` (`quadro_kamban_id`),
  CONSTRAINT `tarefa_ibfk_1` FOREIGN KEY (`quadro_kamban_id`) REFERENCES `quadro_kamban` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `usuario_funcionalidade` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `usuario_id` bigint NOT NULL,
  `funcionalidade` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuario_funcionalidade` (`usuario_id`,`funcionalidade`),
  CONSTRAINT `usuario_funcionalidade_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

SET FOREIGN_KEY_CHECKS=1;
