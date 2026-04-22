-- ElderGuard Nexus - Database Schema
-- PostgreSQL DDL for geriatric care system
-- Tables are auto-created by Hibernate (ddl-auto=update) but this is the reference schema

-- Users / Caretakers
CREATE TABLE IF NOT EXISTS users (
    id            BIGSERIAL PRIMARY KEY,
    username      VARCHAR(50)  NOT NULL UNIQUE,
    password      VARCHAR(255) NOT NULL,
    full_name     VARCHAR(255) NOT NULL,
    email         VARCHAR(255) UNIQUE,
    role          VARCHAR(50)  NOT NULL DEFAULT 'ROLE_CARETAKER',
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Patients (Elderly)
CREATE TABLE IF NOT EXISTS patients (
    id                       BIGSERIAL PRIMARY KEY,
    full_name                VARCHAR(255) NOT NULL,
    age                      INTEGER      NOT NULL,
    gender                   VARCHAR(20),
    date_of_birth            DATE,
    blood_group              VARCHAR(10),
    medical_conditions       VARCHAR(500),
    allergies                VARCHAR(500),
    emergency_contact        VARCHAR(255),
    emergency_contact_phone  VARCHAR(50),
    address                  VARCHAR(500),
    room_number              VARCHAR(50),
    caretaker_id             BIGINT REFERENCES users(id) ON DELETE SET NULL,
    registered_at            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    status                   VARCHAR(50)  NOT NULL DEFAULT 'Active'
);

-- Health Data (Vitals)
CREATE TABLE IF NOT EXISTS health_data (
    id            BIGSERIAL PRIMARY KEY,
    patient_id    BIGINT NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    heart_rate    DOUBLE PRECISION,
    spo2          DOUBLE PRECISION,
    temperature   DOUBLE PRECISION,
    systolic_bp   DOUBLE PRECISION,
    diastolic_bp  DOUBLE PRECISION,
    blood_sugar   DOUBLE PRECISION,
    weight        DOUBLE PRECISION,
    notes         VARCHAR(500),
    recorded_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    recorded_by   VARCHAR(100)
);

-- Orders (Food & Medicine)
CREATE TABLE IF NOT EXISTS orders (
    id                    BIGSERIAL PRIMARY KEY,
    patient_id            BIGINT       NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    order_type            VARCHAR(20)  NOT NULL CHECK (order_type IN ('FOOD', 'MEDICINE')),
    item_name             VARCHAR(255) NOT NULL,
    quantity              INTEGER,
    special_instructions  VARCHAR(500),
    status                VARCHAR(30)  NOT NULL DEFAULT 'PENDING'
                            CHECK (status IN ('PENDING','PROCESSING','DELIVERED','CANCELLED')),
    ordered_at            TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    ordered_by            VARCHAR(100),
    price                 DOUBLE PRECISION
);

-- Emergency Alerts
CREATE TABLE IF NOT EXISTS emergency_alerts (
    id               BIGSERIAL PRIMARY KEY,
    patient_id       BIGINT      NOT NULL REFERENCES patients(id) ON DELETE CASCADE,
    alert_type       VARCHAR(50) NOT NULL DEFAULT 'SOS',
    description      VARCHAR(1000),
    severity         VARCHAR(20) NOT NULL DEFAULT 'HIGH'
                       CHECK (severity IN ('LOW','MEDIUM','HIGH','CRITICAL')),
    status           VARCHAR(30) NOT NULL DEFAULT 'ACTIVE'
                       CHECK (status IN ('ACTIVE','ACKNOWLEDGED','RESOLVED')),
    triggered_at     TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    resolved_at      TIMESTAMP,
    triggered_by     VARCHAR(100),
    resolved_by      VARCHAR(100),
    location_details VARCHAR(255)
);

-- Indexes for performance
CREATE INDEX IF NOT EXISTS idx_health_data_patient ON health_data(patient_id);
CREATE INDEX IF NOT EXISTS idx_health_data_recorded ON health_data(recorded_at DESC);
CREATE INDEX IF NOT EXISTS idx_orders_patient ON orders(patient_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_alerts_patient ON emergency_alerts(patient_id);
CREATE INDEX IF NOT EXISTS idx_alerts_status ON emergency_alerts(status);
CREATE INDEX IF NOT EXISTS idx_patients_caretaker ON patients(caretaker_id);
