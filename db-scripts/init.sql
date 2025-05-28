CREATE TABLE tienda (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    codigo VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE seccion (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    horas_necesarias INTEGER NOT NULL CHECK (horas_necesarias > 0),
    tienda_id INTEGER NOT NULL,
    CONSTRAINT fk_tienda_seccion FOREIGN KEY (tienda_id) REFERENCES tienda(id) ON DELETE CASCADE
);

CREATE TABLE trabajador (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    horas_totales INTEGER NOT NULL CHECK (horas_totales >= 0),
    tienda_id INTEGER NOT NULL,
    CONSTRAINT fk_tienda_trabajador FOREIGN KEY (tienda_id) REFERENCES tienda(id) ON DELETE CASCADE
);

CREATE TABLE asignacion (
    id SERIAL PRIMARY KEY,
    horas_asignadas INTEGER NOT NULL CHECK (horas_asignadas > 0),
    trabajador_id INTEGER NOT NULL,
    seccion_id INTEGER NOT NULL,
    CONSTRAINT fk_trabajador FOREIGN KEY (trabajador_id) REFERENCES trabajador(id) ON DELETE CASCADE,
    CONSTRAINT fk_seccion FOREIGN KEY (seccion_id) REFERENCES seccion(id) ON DELETE CASCADE,
    CONSTRAINT unique_asignacion UNIQUE (trabajador_id, seccion_id)
);

INSERT INTO tienda (nombre,codigo) VALUES ('Supermercado Central', 'SC001'),
('Supermercado Local', 'SL002'),
('Supermercado Express', 'SE003');


INSERT INTO seccion (nombre, horas_necesarias, tienda_id) VALUES 
('Horno', 8, 1),
('Cajas', 16, 1),
('Pescadería', 16, 1),
('Verduras', 16, 1),
('Droguería', 16, 1),
('Horno', 8, 2),
('Cajas', 16, 2),
('Pescadería', 16, 2),
('Verduras', 16, 2),
('Droguería', 16, 2),
('Horno', 8, 3),
('Cajas', 16, 3),
('Pescadería', 16, 3),
('Verduras', 16, 3),
('Droguería', 16, 3);


INSERT INTO trabajador (nombre, apellidos, identificacion, horas_totales, tienda_id) VALUES
('Laura', 'Martínez Gómez', '12345678A', 8, 1),
('Carlos', 'Ruiz Fernández', '23456789B', 6, 1),
('Ana', 'López Torres', '34567890C', 4, 1),
('Pedro', 'García Pérez', '45678901D', 8, 2),
('Marta', 'Sánchez Jiménez', '56789012E', 6, 2),
('Luis', 'Hernández Díaz', '67890123F', 4, 2),
('Elena', 'Moreno Ruiz', '78901234G', 8, 3),
('Javier', 'Jiménez López', '89012345H', 6, 3),
('Sofía', 'Torres García', '90123456I', 4, 3);

INSERT INTO asignacion (trabajador_id, seccion_id, horas_asignadas) VALUES
(1, 2, 4),  -- Cajas
(1, 3, 4),  -- Pescadería
(2, 1, 2),  -- Horno
(2, 2, 4),  -- Cajas
(3, 4, 4),  -- Verduras
(3, 5, 4),  -- Droguería
(4, 6, 4),  -- Cajas
(4, 7, 4),  -- Pescadería
(5, 8, 2),  -- Horno
(5, 9, 4),  -- Cajas
(6, 10, 4),  -- Verduras
(6, 11, 4),  -- Droguería
(7, 12, 4),  -- Cajas
(7, 13, 4),  -- Pescadería
(8, 14, 2),  -- Horno
(8, 2, 4),  -- Cajas
(9, 4, 4),  -- Verduras
(9, 5, 4); -- Droguería