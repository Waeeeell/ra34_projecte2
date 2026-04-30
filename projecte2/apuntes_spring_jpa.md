# ACCÉS A DADES – RA3 – 0486  
## Spring Data JPA & ORM

## OBJECT RELATIONAL MAPPING (ORM)

El ORM (Object Relational Mapping) es una técnica que actúa como intermediaria entre una aplicación y una base de datos relacional, permitiendo trabajar sin escribir SQL directamente.

En aplicaciones empresariales:
- Se conectan a bases de datos
- Se envían consultas
- Se procesan resultados

Con herramientas como JDBC, el código se vuelve repetitivo.

Con Spring + ORM (Hibernate):
- Los datos se transforman en objetos
- Se evita SQL directo
- Se automatizan operaciones

Interfaces clave:
- EntityManager
- CrudRepository
- JpaRepository

---

## JPA (Java Persistence API)

Spring Data JPA ofrece una abstracción para acceder a datos.

Permite:
- Usar interfaces en lugar de implementar lógica
- Automatizar operaciones

Principales interfaces:
- CrudRepository
- JpaRepository (más completo)

---

## MAPAJE ENTIDAD / OBJETO

### Anotaciones importantes

@Entity  
Marca la clase como entidad y la mapea a una tabla.

@Id  
Define la clave primaria.

@GeneratedValue  
Genera el ID automáticamente.

@Column  
Configura campos (nullable, length, etc.).

---

## ENTIDADES INCRUSTADAS

Permite separar lógica en clases pero guardar en una sola tabla.

Se usa @Embeddable.

Ejemplo:
- Customer
- Address

---

## ENUMS EN JPA

Permite mapear tipos enumerados.

Ejemplo:
- User
- Rol (ALUMNO, DOCENTE)

---

## REPOSITORIOS Y CRUD

Ejemplo:
public interface UniversityRepository extends CrudRepository<University, Long> {}

Métodos automáticos:
- save()
- findById()
- existsById()
- findAll()
- count()
- deleteById()
- delete()

Detalles:
- findById devuelve Optional
- save inserta o actualiza

---

## TIPOS DE CONSULTAS

1. Query Derivation
2. JPQL
3. SQL nativo

---

## CONFIGURACIÓN

spring.jpa.hibernate.ddl-auto=update

---

## DTO (Data Transfer Object)

Permite controlar qué datos se envían o reciben.

Ejemplo:
- User
- UserResponseDTO
- UserRequestDTO

Ventajas:
- Seguridad
- Personalización

---

## OPTIONAL

Evita NullPointerException.

Uso:
Optional<User> user = repository.findById(id);

- Optional → 1 resultado
- List → varios resultados

---

## CREAR JAR

Compilar:
./mvnw clean package

Ejecutar:
java -jar archivo.jar
