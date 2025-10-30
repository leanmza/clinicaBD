CREATE SCHEMA clinica;
DROP SCHEMA clinica;

INSERT INTO `especialidad` VALUES 
(7,'Cirugía Oral'),
(3,'Endodoncia'),
(8,'Estética Dental'),
(6,'Implantología'),
(1,'Odontología General'),
(2,'Ortodoncia'),
(5,'Pediatría Dental'),
(4,'Periodoncia'),
(9,'Prostodoncia'),
(10,'Radiología Dental');

INSERT INTO Horarios (hora_inicio, hora_fin) VALUES
('08:00:00','12:00:00'),
('09:00:00','13:00:00'),
('15:00:00','19:00:00'),
('16:00:00','20:00:00');

INSERT INTO `paciente` VALUES 
(1,'Rivas','2616112233','ana.rivas@gmail.com','Ana'),
(2,'López','2616223344','jose.lopez@gmail.com','José'),
(3,'Fernández','2616334455','carla.fernandez@gmail.com','Carla'),
(4,'García','2616445566','martin.garcia@gmail.com','Martín'),
(5,'Pérez','2616556677','valentina.perez@gmail.com','Valentina'),
(6,'Gómez','2616667788','tomas.gomez@gmail.com','Tomás'),
(7,'Rodríguez','2616778899','luciana.rodriguez@gmail.com','Luciana'),
(8,'Sánchez','2616889900','diego.sanchez@gmail.com','Diego'),
(9,'Martínez','2616990011','paula.martinez@gmail.com','Paula'),
(10,'Torres','2616001122','santiago.torres@gmail.com','Santiago');

INSERT INTO `doctor` (doctor_id, apellido, celular, email, nombre, especialidad_id, horarios_id) VALUES
(1,'Pérez','2611111111','juan.perez@gmail.com','Juan',1,1),
(2,'Gómez','2612222222','ana.gomez@gmail.com','Ana',2,1),
(3,'Martínez','2613333333','luis.martinez@gmail.com','Luis',3,2),
(4,'Rodríguez','2614444444','marta.rodriguez@gmail.com','Marta',4,2),
(5,'Sánchez','2615555555','carlos.sanchez@gmail.com','Carlos',5,3),
(6,'Fernández','2616666666','laura.fernandez@gmail.com','Laura',6,3),
(7,'Ramírez','2617777777','diego.ramirez@gmail.com','Diego',7,4),
(8,'Torres','2618888888','sofia.torres@gmail.com','Sofía',8,4),
(9,'Vega','2619999999','mario.vega@gmail.com','Mario',9,1),
(10,'Morales','2610000000','lucia.morales@gmail.com','Lucía',10,2);

INSERT INTO Turno (fecha_hora, ocupado, paciente_id, doctor_id) VALUES
-- Doctor 1 (08:00)
('2025-11-03 08:00:00', true, 1, 1),
('2025-11-03 08:30:00', true, 2, 1),
('2025-11-03 09:00:00', true, 3, 1),
('2025-11-03 09:30:00', true, 4, 1),
('2025-11-03 10:00:00', true, 5, 1),

-- Doctor 2 (08:00)
('2025-11-03 08:00:00', true, 6, 2),
('2025-11-03 08:30:00', true, 7, 2),
('2025-11-03 09:00:00', true, 8, 2),
('2025-11-03 09:30:00', true, 9, 2),
('2025-11-03 10:00:00', true, 10, 2),

-- Doctor 3 (09:00)
('2025-11-03 09:00:00', true, 1, 3),
('2025-11-03 09:30:00', true, 2, 3),
('2025-11-03 10:00:00', true, 3, 3),
('2025-11-03 10:30:00', true, 4, 3),
('2025-11-03 11:00:00', true, 5, 3),

-- Doctor 4 (09:00)
('2025-11-03 09:00:00', true, 6, 4),
('2025-11-03 09:30:00', true, 7, 4),
('2025-11-03 10:00:00', true, 8, 4),
('2025-11-03 10:30:00', true, 9, 4),
('2025-11-03 11:00:00', true, 10, 4),

-- Doctor 5 (15:00)
('2025-11-03 15:00:00', true, 1, 5),
('2025-11-03 15:30:00', true, 2, 5),
('2025-11-03 16:00:00', true, 3, 5),
('2025-11-03 16:30:00', true, 4, 5),
('2025-11-03 17:00:00', true, 5, 5),

-- Doctor 6 (15:00)
('2025-11-03 15:00:00', true, 6, 6),
('2025-11-03 15:30:00', true, 7, 6),
('2025-11-03 16:00:00', true, 8, 6),
('2025-11-03 16:30:00', true, 9, 6),
('2025-11-03 17:00:00', true, 10, 6),

-- Doctor 7 (16:00)
('2025-11-03 16:00:00', true, 1, 7),
('2025-11-03 16:30:00', true, 2, 7),
('2025-11-03 17:00:00', true, 3, 7),
('2025-11-03 17:30:00', true, 4, 7),
('2025-11-03 18:00:00', true, 5, 7),

-- Doctor 8 (16:00)
('2025-11-03 16:00:00', true, 6, 8),
('2025-11-03 16:30:00', true, 7, 8),
('2025-11-03 17:00:00', true, 8, 8),
('2025-11-03 17:30:00', true, 9, 8),
('2025-11-03 18:00:00', true, 10, 8),

-- Doctor 9 (08:00)
('2025-11-03 08:00:00', true, 1, 9),
('2025-11-03 08:30:00', true, 2, 9),
('2025-11-03 09:00:00', true, 3, 9),
('2025-11-03 09:30:00', true, 4, 9),
('2025-11-03 10:00:00', true, 5, 9),

-- Doctor 10 (09:00)
('2025-11-03 09:00:00', true, 6, 10),
('2025-11-03 09:30:00', true, 7, 10),
('2025-11-03 10:00:00', true, 8, 10),
('2025-11-03 10:30:00', true, 9, 10),
('2025-11-03 11:00:00', true, 10, 10);

