insert into tb_cliente_endereco
(id, rua, numero, cep, cidade)
values
    ('f0ef7e69-207b-4456-a130-a130d0f329d5', 'rua a', '123', '12345678', 'São Paulo/SP/Brasil'),
    ('1e05357b-f3ef-41da-a69c-4d8323b0e100', 'rua b', '321', '65423123', 'São Paulo/SP/Brasil'),
    ('37f2bc8a-b6da-4203-8bd3-bc7c6179636b', 'rua c', '213', '12341234', 'São Paulo/SP/Brasil');

insert into tb_cliente
(id, nome, cpf, id_endereco)
values
    ('56833f9a-7fda-49d5-a760-8e1ba41f35a8', 'Anderson Wagner', '80346534038', 'f0ef7e69-207b-4456-a130-a130d0f329d5'),
    ('ab8fdcd5-c9b5-471e-8ad0-380a65d6cc86', 'Kaiby do Santos', '52816804046', '1e05357b-f3ef-41da-a69c-4d8323b0e100'),
    ('8855e7b2-77b6-448b-97f8-8a0b529f3976', 'Janaina Alvares', '61908619031', '37f2bc8a-b6da-4203-8bd3-bc7c6179636b');