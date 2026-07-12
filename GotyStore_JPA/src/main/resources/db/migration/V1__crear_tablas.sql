CREATE TABLE IF NOT EXISTS cliente (
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre    TEXT    NOT NULL,
    correo    TEXT    NOT NULL UNIQUE,
    direccion TEXT,
    telefono  TEXT
);

CREATE TABLE IF NOT EXISTS juego (
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo           TEXT NOT NULL,
    desarrollador    TEXT NOT NULL,
    plataforma       TEXT NOT NULL,
    anio_lanzamiento TEXT,
    genero           TEXT
);

CREATE TABLE IF NOT EXISTS venta (
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha      TEXT    NOT NULL,
    id_cliente INTEGER NOT NULL,
    id_juego   INTEGER NOT NULL,
    cantidad   INTEGER NOT NULL,
    total      REAL    NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id),
    FOREIGN KEY (id_juego)   REFERENCES juego(id)
);
