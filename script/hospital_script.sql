-- ============================================================
-- Proyecto: Sistema de Gestión de Citas Médicas (HospitalCitas)
-- Motor: SQLite
-- Archivo de base de datos: hospital.db
-- ============================================================

-- Activa la verificación de llaves foráneas en SQLite
PRAGMA foreign_keys = ON;

-- ------------------------------------------------------------
-- Tabla: especialidades
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS especialidades (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE
);

-- ------------------------------------------------------------
-- Tabla: medicos
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS medicos (
    cedula TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    telefono TEXT,
    especialidad_id INTEGER,
    FOREIGN KEY (especialidad_id) REFERENCES especialidades(id)
);

-- ------------------------------------------------------------
-- Tabla: pacientes
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS pacientes (
    cedula TEXT PRIMARY KEY,
    nombre TEXT NOT NULL,
    telefono TEXT,
    direccion TEXT
);

-- ------------------------------------------------------------
-- Tabla: citas
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS citas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    paciente_cedula TEXT NOT NULL,
    medico_cedula TEXT NOT NULL,
    fecha TEXT NOT NULL,
    hora TEXT NOT NULL,
    motivo TEXT,
    estado TEXT DEFAULT 'Pendiente',
    FOREIGN KEY (paciente_cedula) REFERENCES pacientes(cedula),
    FOREIGN KEY (medico_cedula) REFERENCES medicos(cedula)
);

-- ------------------------------------------------------------
-- Tabla: usuarios
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    usuario TEXT NOT NULL UNIQUE,
    clave TEXT NOT NULL,
    rol TEXT NOT NULL
);
