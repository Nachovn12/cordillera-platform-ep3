# Guía de Pruebas API (Postman / cURL) - Cordillera Platform

Esta guía contiene los endpoints principales y payloads de prueba (Mock Data) para probar la API a través del **BFF Gateway (Puerto 8081)**. Al pasar por el BFF, estarás validando el ruteo hacia los microservicios backend (`report-service`, `kpi-service` y `data-service`).

## 1. Microservicio de Reportes (report-service)

### Crear Reporte (POST)
Crea un nuevo registro de reporte.
- **Endpoint:** `POST http://localhost:8081/api/v1/reportes`
- **Headers:** `Content-Type: application/json`

**Body (JSON):**
```json
{
  "titulo": "Reporte Mensual Ventas Julio",
  "tipo": "Mensual",
  "area": "Ventas",
  "valor": 450000.00
}
```

### Generar Reporte Ejecutivo (POST)
Genera el cálculo del reporte consolidado, aplicando las reglas de negocio (ej. unicidad).
- **Endpoint:** `POST http://localhost:8081/api/v1/reportes/generar`
- **Headers:** `Content-Type: application/json`

**Body (JSON):**
```json
{
  "titulo": "Reporte Semestral Operaciones",
  "tipo": "Semestral",
  "area": "Operaciones",
  "valor": 125000.00
}
```

### Listar Reportes (GET)
Obtiene todos los reportes generados.
- **Endpoint:** `GET http://localhost:8081/api/v1/reportes`

### Eliminar Reporte (DELETE)
Elimina un reporte existente por su ID. Reemplaza `1` por el ID a eliminar.
- **Endpoint:** `DELETE http://localhost:8081/api/v1/reportes/1`


## 2. Microservicio de KPIs (kpi-service)

### Crear KPI (POST)
- **Endpoint:** `POST http://localhost:8081/api/v1/kpis`
- **Headers:** `Content-Type: application/json`

**Body (JSON):**
```json
{
  "nombre": "Nivel de Satisfacción Q3",
  "valor": 95.50,
  "unidad": "%",
  "categoria": "Calidad",
  "estado": "Activo"
}
```

### Listar KPIs (GET)
Obtiene todos los KPIs.
- **Endpoint:** `GET http://localhost:8081/api/v1/kpis`

### Buscar KPI por Categoría (GET)
Obtiene los KPIs filtrados por categoría (ej. Ventas, Inventario).
- **Endpoint:** `GET http://localhost:8081/api/v1/kpis/categoria/Ventas`

### Actualizar KPI (PUT)
Actualiza un KPI existente por su ID.
- **Endpoint:** `PUT http://localhost:8081/api/v1/kpis/1`
- **Headers:** `Content-Type: application/json`

**Body (JSON):**
```json
{
  "nombre": "Nivel de Satisfacción Q3 (Actualizado)",
  "valor": 98.00,
  "unidad": "%",
  "categoria": "Calidad",
  "estado": "Activo"
}
```

### Eliminar KPI (DELETE)
Elimina un KPI existente por su ID.
- **Endpoint:** `DELETE http://localhost:8081/api/v1/kpis/1`


## 3. Microservicio de Datos (data-service)

### Crear Dato (POST)
- **Endpoint:** `POST http://localhost:8081/api/v1/datos`
- **Headers:** `Content-Type: application/json`

**Body (JSON):**
```json
{
  "origen": "ERP_SAP",
  "descripcion": "Ventas consolidadas región norte",
  "valor": 1500000.00,
  "fechaRegistro": "2026-07-01T10:00:00"
}
```

### Listar Datos (GET)
Obtiene todos los datos operacionales.
- **Endpoint:** `GET http://localhost:8081/api/v1/datos`

### Buscar Datos por Sistema Origen (GET)
Filtra los datos por sistema (ej. SAP, ERP).
- **Endpoint:** `GET http://localhost:8081/api/v1/datos/sistema/SAP`

### Buscar Datos por Sucursal (GET)
Filtra los datos por ID de sucursal.
- **Endpoint:** `GET http://localhost:8081/api/v1/datos/sucursal/1`

### Actualizar Dato (PUT)
Actualiza un dato existente por su ID.
- **Endpoint:** `PUT http://localhost:8081/api/v1/datos/1`
- **Headers:** `Content-Type: application/json`

**Body (JSON):**
```json
{
  "sistemaOrigen": "ERP_SAP_V2",
  "tipoDato": "Ventas",
  "valor": "1800000.00",
  "sucursalId": 1
}
```

### Eliminar Dato (DELETE)
Elimina un registro de datos existente por su ID.
- **Endpoint:** `DELETE http://localhost:8081/api/v1/datos/1`

---
> **Tip Evaluativo:** Apuntar las peticiones de Postman siempre al puerto **8081** (BFF Gateway) demuestra que la arquitectura centralizada está funcionando. El BFF se encargará de re-enrutar la petición internamente a los puertos 8083, 8084 o 8085 según corresponda.
