INSERT INTO cliente (nombre, correo, direccion, telefono) VALUES
    ('Carlos Pérez',   'carlos@correo.com',  'Av. Las Torres 123, Santiago', '+56911111111'),
    ('María González', 'maria@correo.com',   'Calle Falsa 456, Valparaíso',  '+56922222222'),
    ('Luis Rodríguez', 'luis@correo.com',    'Los Aromos 789, Concepción',   '+56933333333'),
    ('Ana Martínez',   'ana@correo.com',     'Pasaje Sol 101, Temuco',       '+56944444444'),
    ('Pedro Soto',     'pedro@correo.com',   'Av. Principal 202, Antofagasta','+56955555555');

INSERT INTO juego (titulo, desarrollador, plataforma, anio_lanzamiento, genero) VALUES
    ('The Last of Us Part II', 'Naughty Dog',       'PS4', '2020', 'Acción-Aventura'),
    ('Elden Ring',             'FromSoftware',       'PC',  '2022', 'RPG'),
    ('God of War Ragnarök',    'Santa Monica Studio','PS5', '2022', 'Acción'),
    ('Hollow Knight',          'Team Cherry',        'PC',  '2017', 'Metroidvania'),
    ('Red Dead Redemption 2',  'Rockstar Games',     'PS4', '2018', 'Aventura');

INSERT INTO venta (fecha, id_cliente, id_juego, cantidad, total) VALUES
    ('2024-01-15', 1, 1, 1, 59990),
    ('2024-02-20', 2, 2, 1, 54990),
    ('2024-03-05', 3, 3, 2, 119980),
    ('2024-04-10', 1, 4, 1, 24990),
    ('2024-05-22', 4, 5, 1, 49990),
    ('2024-06-01', 5, 2, 1, 54990),
    ('2024-06-15', 2, 3, 1, 59990);
