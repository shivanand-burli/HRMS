-- ==========================
-- HRMS Schema V1 (Flyway)
-- ==========================

-- Company table
CREATE TABLE company (
    email VARCHAR(255) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    doc DATE NOT NULL,
    about VARCHAR(255),

    -- working hours (stored UTC)
    last_allowed_check_in_time TIME NOT NULL,
    begin_check_out_time TIME NOT NULL,

    -- company details (embedded)
    reg_number VARCHAR(100),
    pan VARCHAR(100),
    company_type VARCHAR(100),

    -- auth (embedded, define fields here if required)
    password_hash VARCHAR(255),

    role VARCHAR(50) NOT NULL DEFAULT 'COMPANY'
);

-- Employee table
CREATE TABLE employee (
    email VARCHAR(255) PRIMARY KEY,
    id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    mobile VARCHAR(20) NOT NULL,
    doj DATE NOT NULL,
    dob DATE NOT NULL,

    -- employee details (embedded)
    salary NUMERIC(15,2) NOT NULL,
    aadhar VARCHAR(255),
    pan VARCHAR(255),
    account_no BIGINT,
    ifsc VARCHAR(20),

    company_id VARCHAR(255) NOT NULL,
    -- auth (embedded, define fields here if required)
    password_hash VARCHAR(255),

    role VARCHAR(50) NOT NULL DEFAULT 'EMPLOYEE',
    CONSTRAINT fk_employee_company FOREIGN KEY (company_id) REFERENCES company (email) ON DELETE CASCADE
);

CREATE INDEX idx_employee_company ON employee (company_id);

-- Attendance table
CREATE TABLE attendance (
    employee_email VARCHAR(255) NOT NULL,
    attendance_date DATE NOT NULL,

    company_id VARCHAR(255) NOT NULL,

    check_in TIME,
    check_out TIME,

    attendance_status VARCHAR(100) NOT NULL,
    ot_hours DOUBLE PRECISION DEFAULT 0,

    PRIMARY KEY (employee_email, attendance_date, company_id),

    CONSTRAINT fk_attendance_employee FOREIGN KEY (employee_email) REFERENCES employee (email) ON DELETE CASCADE,
    CONSTRAINT fk_attendance_company FOREIGN KEY (company_id) REFERENCES company (email) ON DELETE CASCADE
);

CREATE INDEX idx_attendance_company_date ON attendance (company_id, attendance_date);
CREATE INDEX idx_attendance_company_date_status ON attendance (company_id, attendance_date, attendance_status);
