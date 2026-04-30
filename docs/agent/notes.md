# 📝 Notas de Decisiones y Dudas - RA3 Projecte 2

## 🎯 Decisiones Arquitectónicas

### 1. Uso de BigDecimal para precio y rating
**Decisión:** Usar `BigDecimal` en lugar de `double`
**Justificación:** 
- Operaciones financieras requieren precisión decimal exacta
- `double` puede tener errores de redondeo
- `BigDecimal` es estándar en aplicaciones empresariales

### 2. Enum ProductCondition
**Decisión:** Usar `@Enumerated(EnumType.STRING)`
**Justificación:**
- Valores controlados: nou, bon_estat, acceptable, mal_estat
- STRING es más legible en BD que ORDINAL
- Evita problemas si se reordenan los valores

### 3. Borrado Lógico vs Físico
**Decisión:** Implementar ambos
- `DELETE /api/products/{id}` → Físico (elimina del BD)
- `PATCH /api/products/{id}/logical-delete` → Lógico (status = false)

**Justificación:**
- Borrado lógico: conserva auditoría y referencias
- Borrado físico: libera espacio en BD
- Queries por defecto filtran `status = true`

### 4. Auditoría con @PrePersist y @PreUpdate
**Decisión:** Automatizar timestamps en la entidad
**Justificación:**
- Garantiza consistencia en todos los registros
- No depende de triggers BD
- Portable entre BD

### 5. DTOs separados para request/response
**Decisión:** ProductRequestDTO ≠ ProductResponseDTO
**Justificación:**
- Request: no incluye id, dataCreated, dataUpdated
- Response: sí incluye toda la información
- Seguridad: evita que cliente manipule campos de auditoría

### 6. CSV con @Transactional
**Decisión:** Envolver carga de CSV en `@Transactional(rollbackFor = Exception.class)`
**Justificación:**
- Si falla un registro → rollback de todo
- Integridad de datos
- Cumple requisito de la práctica

### 7. Paginación con Pageable
**Decisión:** Usar Spring Data `Pageable`
**Justificación:**
- Manejo automático de page/size/sort
- Retorna `Page<>` con metadata
- Optimización: SELECT ... LIMIT x OFFSET y

---

## ❓ Dudas Resueltas

### ¿Cómo manejar errores en CSV?
**Solución:** 
- Validar cada línea antes de guardar
- Si falla una línea → capturar excepción y registrar
- Retornar JSON con successCount + lista de errores
- Hacer ROLLBACK de todo si hay errores críticos

### ¿Qué estatus HTTP usar?
**Solución:**
- 200 OK: GET exitoso, PATCH exitoso
- 201 Created: POST exitoso
- 204 No Content: DELETE exitoso
- 400 Bad Request: validación fallida
- 404 Not Found: recurso no existe
- 500 Internal Server Error: error no controlado (con ErrorDTO)

### ¿Query Derivation vs JPQL?
**Solución:**
- Query Derivation: `findBy...()` - para queries simples
- JPQL: `@Query` - para queries complejas con lógica custom
- En la práctica usar AMBAS donde sea apropiado

### ¿Cómo mapear ENUM en CSV?
**Solución:**
```java
ProductCondition condition = ProductCondition.valueOf(csvLine.getCondition().toUpperCase());
```

### ¿Cómo validar entrada en CSV?
**Solución:**
- Usar `Validator` de Jakarta Validation
- O validar manualmente línea por línea
- Registrar qué línea falló exactamente

---

## ⚠️ Riesgos Identificados

| Riesgo | Impacto | Mitigación |
|--------|---------|-----------|
| N+1 problem en queries | Lentitud | Usar JPQL con JOINs si es necesario |
| Memory leak en CSV | Aplicación lenta | Procesar en streaming, no cargar todo en memoria |
| FK constraint en productos relacionados | Error al borrar | Implementar cascada o restricción apropiada |
| Valores NULL en rating | Queries fallidas | Filtros explícitos: `AND rating IS NOT NULL` |
| Huso horario en auditoría | Inconsistencia | Usar `LocalDateTime` con zona UTC |

---

## 📋 Checklist Pre-Implementación

- [ ] ¿Spring Boot correctamente configurado?
- [ ] ¿Dependencies en pom.xml? (Spring Data JPA, MySQL driver, Validation, etc.)
- [ ] ¿application.properties configurado? (BD, JPA logging, profiles)
- [ ] ¿Estructura de carpetas creada?
- [ ] ¿Enum ProductCondition definido?
- [ ] ¿Entity Product con todas las anotaciones?
- [ ] ¿DTOs con validaciones?
- [ ] ¿ProductRepository extendiendo JpaRepository?
- [ ] ¿ProductService con lógica?
- [ ] ¿ProductController con endpoints?
- [ ] ¿GlobalExceptionHandler configurado?
- [ ] ¿ErrorDTO retornado en excepciones?
- [ ] ¿CSV upload con validaciones?
- [ ] ¿Paginación implementada?
- [ ] ¿Queries derivation + JPQL diferenciadas?
- [ ] ¿Tests unitarios pasando?
- [ ] ¿Comentarios en código?
- [ ] ¿README con instrucciones?

---

## 🚀 Pasos de Implementación (Orden Recomendado)

1. **Configuración inicial**
   - pom.xml con dependencies
   - application.properties
   - Estructura de carpetas

2. **Capa de modelo**
   - ProductCondition (ENUM)
   - Product (Entity con validaciones)

3. **DTOs y validaciones**
   - ProductRequestDTO
   - ProductResponseDTO
   - ErrorDTO

4. **Capa de repository**
   - ProductRepository (queries derivation + JPQL)

5. **Capa de servicio**
   - ProductService (CRUD logic + CSV + búsquedas)

6. **Capa de presentación**
   - ProductController (endpoints)
   - GlobalExceptionHandler

7. **Testing y validación**
   - Endpoints con cURL/Postman
   - CSV upload
   - Paginación

8. **Documentación**
   - Comentarios en código
   - README con video

---

## 📌 Requisitos del Curso

✅ Usar todas las funcionalidades explicadas
✅ Comentar el código
✅ Usar JpaRepository
✅ @Transactional en CSV
✅ Entornos-profile (dev/prod)
✅ ErrorDTO obligatorio
✅ Seguir patrón MVC
✅ No usar funcionalidades no explicadas

---

## 🔗 Referencias Internas

- `activitat.md` - Especificación completa
- `apuntes_spring_jpa.md` - Conceptos teóricos
- `database.md` - Diseño de BD
- `code.md` - Arquitectura de código

---
