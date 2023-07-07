SELECT * FROM JOGADOR
SELECT * FROM POCAO
SELECT * FROM NPC
SELECT * FROM CONDICOES
SELECT * FROM INGREDIENTE

SELECT id, descricao, quantidade FROM POCAO, JOGADOR_POSSUI_POCAO
WHERE POCAO.id = JOGADOR_POSSUI_POCAO.POCAO_id
AND JOGADOR_id = 1

SELECT id, valor, nome, tempo_necessario, quantidade FROM INGREDIENTE, JOGADOR_POSSUI_INGREDIENTE
WHERE INGREDIENTE.id = JOGADOR_POSSUI_INGREDIENTE.INGREDIENTE_id
AND JOGADOR_id = 1

SELECT * FROM INGREDIENTE
LEFT JOIN JOGADOR_POSSUI_INGREDIENTE ON INGREDIENTE.id = JOGADOR_POSSUI_INGREDIENTE.INGREDIENTE_id
AND JOGADOR_id = 1

SELECT * FROM JOGADOR_POSSUI_POCAO
SELECT * FROM JOGADOR_POSSUI_INGREDIENTE
SELECT * FROM INGREDIENTE_COMPOE_POCAO, INGREDIENTE WHERE POCAO_id = 1
SELECT * FROM NPC_ACOMETIDO_POR_CONDICOES
SELECT * FROM NPC_ALERGICO_A_INGREDIENTE
SELECT * FROM INGREDIENTE_TRATA_CONDICOES

SELECT  id, valor, nome, tempo_necessario FROM INGREDIENTE_TRATA_CONDICOES, INGREDIENTE
WHERE INGREDIENTE_TRATA_CONDICOES.INGREDIENTE_id = INGREDIENTE.id
AND CONDICOES_id = 1

SELECT id, nome, descricao, intensidade FROM NPC_ACOMETIDO_POR_CONDICOES, CONDICOES
WHERE NPC_ACOMETIDO_POR_CONDICOES.CONDICOES_id = CONDICOES.id
AND NPC_id = 3

SELECT id, valor, nome, tempo_necessario FROM NPC_ALERGICO_A_INGREDIENTE, INGREDIENTE
WHERE NPC_ALERGICO_A_INGREDIENTE.INGREDIENTE_id = INGREDIENTE.id
AND NPC_id = 3

(SELECT id, valor, nome, tempo_necessario, quantidade FROM JOGADOR_POSSUI_INGREDIENTE, INGREDIENTE, INGREDIENTE_COMPOE_POCAO
WHERE JOGADOR_POSSUI_INGREDIENTE.INGREDIENTE_id = INGREDIENTE.id
AND INGREDIENTE_COMPOE_POCAO.INGREDIENTE_id = INGREDIENTE.id AND POCAO_id = 2 AND JOGADOR_id = 1)
UNION (SELECT id, valor, nome, tempo_necessario, 0 FROM INGREDIENTE, INGREDIENTE_COMPOE_POCAO 
WHERE INGREDIENTE_COMPOE_POCAO.INGREDIENTE_id = INGREDIENTE.id AND POCAO_id = 2 AND id NOT IN
(SELECT id FROM JOGADOR_POSSUI_INGREDIENTE, INGREDIENTE, INGREDIENTE_COMPOE_POCAO
WHERE JOGADOR_POSSUI_INGREDIENTE.INGREDIENTE_id = INGREDIENTE.id
AND INGREDIENTE_COMPOE_POCAO.INGREDIENTE_id = INGREDIENTE.id AND JOGADOR_id = 1))

(SELECT id, valor, nome, tempo_necessario, quantidade FROM JOGADOR_POSSUI_INGREDIENTE, INGREDIENTE
WHERE JOGADOR_POSSUI_INGREDIENTE.INGREDIENTE_id = INGREDIENTE.id AND JOGADOR_id = 2)
UNION (SELECT id, valor, nome, tempo_necessario, 0 FROM INGREDIENTE WHERE id NOT IN
(SELECT id FROM JOGADOR_POSSUI_INGREDIENTE, INGREDIENTE
WHERE JOGADOR_POSSUI_INGREDIENTE.INGREDIENTE_id = INGREDIENTE.id AND JOGADOR_id = 2))

(SELECT id, descricao, quantidade FROM JOGADOR_POSSUI_POCAO, POCAO
WHERE JOGADOR_POSSUI_POCAO.POCAO_id = POCAO.id AND JOGADOR_id = 1)
UNION (SELECT id, descricao, 0 FROM POCAO WHERE id NOT IN
(SELECT id FROM JOGADOR_POSSUI_POCAO, POCAO
WHERE JOGADOR_POSSUI_POCAO.POCAO_id = POCAO.id AND JOGADOR_id = 1))

INSERT INTO JOGADOR VALUES (5, 'Oktamolfa', 100.0)

INSERT INTO POCAO VALUES (2, 'Pilula da vida eterna')

INSERT INTO INGREDIENTE VALUES (2, 2, 'Beterraba', 1)

INSERT INTO NPC VALUES (1, 'Marcel Oliveira', 2, 4, 25.6, 2, 2)
INSERT INTO NPC VALUES (2, 'Nelio Cacho', 3, 5, 26.8, 3, 3)
INSERT INTO NPC VALUES (3, 'Girao', 1, 2, 21.8, 1, 1)

INSERT INTO JOGADOR_POSSUI_POCAO VALUES (1, 1, 1)
INSERT INTO JOGADOR_POSSUI_INGREDIENTE VALUES (1, 2, 2)
INSERT INTO INGREDIENTE_COMPOE_POCAO VALUES (1, 1);
INSERT INTO INGREDIENTE_COMPOE_POCAO VALUES (2, 1);
INSERT INTO INGREDIENTE_COMPOE_POCAO VALUES (2, 2);
INSERT INTO NPC_ACOMETIDO_POR_CONDICOES VALUES (1, 3);
INSERT INTO NPC_ALERGICO_A_INGREDIENTE VALUES (1, 3);
INSERT INTO INGREDIENTE_TRATA_CONDICOES VALUES (1, 1);
INSERT INTO INGREDIENTE_TRATA_CONDICOES VALUES (2, 1);

DELETE FROM JOGADOR WHERE TRUE
DELETE FROM NPC WHERE TRUE