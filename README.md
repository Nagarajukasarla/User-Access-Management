# User Access Management System

A comprehensive role-based access control system that manages user permissions and tracks their actions within the application.

## 📋 Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation Steps](#installation-steps)
- [System Setup](#system-setup)
  - [Database Configuration](#database-configuration)
  - [Application Setup](#application-setup)
- [User Guide](#user-guide)
  - [Admin Portal](#admin-portal)
  - [Manager Portal](#manager-portal)
  - [Employee Portal](#employee-portal)
- [Support](#support)

## Overview

This system provides a secure way to manage software access requests through different user roles:
- **Admin**: Creates and manages software
- **Manager**: Approves or rejects access requests
- **Employee**: Requests access to software

## ✨ Features

- Role-based access control (Admin, Manager, Employee)
- Secure user authentication
- Software access management
- Request tracking system
- Access level authorization
- Audit logging

## Getting Started

### 🔧 Prerequisites

Download and install the following:

Java 17+ [Download](https://www.oracle.com/java/technologies/javase-downloads.html)

Apache Tomcat 9.0+ [Download](https://tomcat.apache.org/download-90.cgi)

IntelliJ IDEA Latest [Download](https://www.jetbrains.com/idea/download/)

PostgreSQL Latest [Download](https://www.postgresql.org/download/)

### 🚀 Installation Steps

1. **Clone Repository**
   ```bash
   git clone https://github.com/Nagarajukasarla/User-Access-Management.git
   cd User-Access-Management
   ```

2. **Project Setup in IntelliJ IDEA**
   - Open IntelliJ IDEA
   - Select `File > Open` → choose cloned directory
   - Configure Web Application:
     - Navigate to `File > Project Structure > Artifacts`
     - Add `Web Application: Exploded`
     - Select User Access Management module

3. **Tomcat Configuration**
   - Go to `Run > Edit Configurations`
   - Add `Tomcat Server > Local`
   - Configure:
     - Set Tomcat installation directory
     - Add `war exploded` artifact
     - Set context path to `/`

## System Setup

### 💾 Database Configuration

1. **Create Database**
   ```sql
   CREATE DATABASE uam;
   ```

2. **Initialize Tables**
   ```sql
   -- User Table
   CREATE TABLE _user (
       id SERIAL PRIMARY KEY,
       username VARCHAR,
       password VARCHAR,
       role VARCHAR
   );

   -- Software Table
   CREATE TABLE _software (
       id SERIAL PRIMARY KEY,
       name VARCHAR,
       description VARCHAR,
       access_level VARCHAR
   );

   -- Requests Table
   CREATE TABLE _requests (
       id SERIAL PRIMARY KEY,
       user_id INT REFERENCES _user(id),
       software_id INT REFERENCES _software(id),
       access_type VARCHAR,
       reason VARCHAR,
       status VARCHAR
   );

   -- Default Users
   INSERT INTO _user (username, password, role) 
   VALUES ('krishna', '123456', 'admin'),
          ('raghu', '123456', 'manager');
   ```

## User Guide

### 🔐 Admin Portal

1. **Login**
   - Username: `krishna`
   - Password: `123456`

2. **Software Management**
   - Click `+ Create New` to add software
   - Fill in:
     - Name
     - Description
     - Access Level
   - Submit to create

### 👨‍💼 Manager Portal

1. **Login**
   - Username: `raghu`
   - Password: `123456`

2. **Request Management**
   - View pending requests
   - Click `Approve` or `Reject` for each request

### 👤 Employee Portal

1. **Account Creation**
   - Click `Create New Account`
   - Enter username and password
   - Click `Sign Up`

2. **Request Access**
   - Login with credentials
   - Select software
   - Provide reason
   - Click `Request Access`

## Support

### ⚠️ Troubleshooting

Common issues and solutions:
1. Verify prerequisite installations
2. Check application logs
3. Confirm database connectivity
4. Validate Tomcat configuration

For support: [Create an Issue](https://github.com/Nagarajukasarla/User-Access-Management/issues)

### 📚 Resources

- [IntelliJ IDEA Guide](https://www.jetbrains.com/help/idea/getting-started.html)
- [Tomcat Documentation](https://tomcat.apache.org/tomcat-9.0-doc/index.html)
- [PostgreSQL Manual](https://www.postgresql.org/docs/)