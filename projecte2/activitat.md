

# [Aav5] Activitat Ra3 (part1) - projecte 2

---

## Projecte 2 (part 1) - RA3 i RA6

**MÒDUL:** Accés a dades  

**RA3.-** Gestiona la persistència de les dades identificant eines de mapatge objecte relacional (ORM) i desenvolupant aplicacions que les utilitzen.  

**RA6.-** Programa components d'accés a dades identificant les característiques que ha de posseir un component i utilitzant eines de desenvolupament.  

---

## ACTIVITAT AVALUABLE

---

## Objectius:

1. Instal·lar l'eina ORM.  
2. Configurar l'eina ORM.  
3. Definir configuracions de mapatge.  
4. Aplicar mecanismes de persistència als objectes.  
5. Desenvolupar aplicacions que modifiquen i recuperen objectes persistents.  
6. Desenvolupar aplicacions que fan consultes usant el llenguatge SQL.  
7. Gestionar les transaccions.  
8. Programar components que gestionen informació emmagatzemada en fitxers.  

---

## Condicions de treball:

- Treball per parelles.  
- Treballar amb git i github. Crear projecte de github de nom **AD_PROJECTE_2**.  

  - Afegir a: **rsobrinog** i **joriolroca** com a col·laboradors.  

- Crear projecte Spring Boot amb **group+artifact com.ra34.projecte2**  

- Cal repartir-se la feina de forma adequada i equilibrada.  

  - Definir branques per cada membre de l’equip.  
  - Crear issues al GitHub i assignar-se-la cadascú.  

    - Es pot i cal vincular els commits necessaris a una issue.  

---

📄 *1 de 7*

---

## Criteris d’acceptació:

- S’han de fer servir totes les funcionalitats que s’han explicat a classe:  
  controlador, repositori, model, service, ResponseEntity, DTO així com la teoria del document RA3.  

- S’ha de comentar el codi.  

- No s’han de fer servir implementacions no explicades a classe.  

- La pràctica s’ha de fer amb la dependència de JPA i s’ha de fer servir **JpaRepository**.  

- En la càrrega de l’excel s’ha de fer servir **@Transactional**  

- S’ha d’utilitzar entorns-profile.  

- S’haurà de crear un DTO: **ErrorDTO** per gestionar l’estat i la descripció dels errors no controlats que haurà de retornar el controller.  

---

Aquest document s’anirà ampliant segons la teoria explicada en la RA4.  

---

⚠️ No complir amb el llistat anterior implica una penalització d’un punt per cada un dels criteris.  

---

## Condicions d’entrega:

- Enllaç github.  

  - Codi de l’aplicació funcional.  
  - Readme.md amb un enllaç a un video mostrant l’execució del fitxer .jar i mostrar totes les funcionalitats demanades.  
  - Caldrà treballar amb branques. Cada integrant tindrà la seva branca i caldrà fusionar tot el projecte a la branca main del github.  

- S’avalua a través de rúbrica.  

---

## Projecte en grup (2 persones)

S’haurà de desenvolupar l'APIREst d’una botiga online de productes.  

Caldrà crear una APIRest amb Spring Boot per gestionar el catàleg de productes aplicant les millors pràctiques d’Spring JPA.  

---

## Entitat principal: Product

- **ID:** Autogenerat  

### Atributs bàsics:

- name: string, not null, màxim 20 caràcters  
- description: string, null, màxim 100 caràcters  
- stock: integer, not null  
- price: decimal, not null  
- rating: decimal, null  

### Atribut enum:

- condition → (nou, bon estat, acceptable, mal estat)

### Altres:

- Borrat lògic: status (boolean)  
- Auditoria: dataCreated, dataUpdated  

---

La interfície **ProductRepository** extendrà de JpaRepository i inclourà consultes amb Query Derivation i JPQL.  

Caldrà configurar el properties per automatitzar la creació de la taula i la connexió a la base de dades.  

---

## 1. Definir l’entitat producte (1 PUNT)

---

## 2. [Endpoint] Càrrega massiva de dades (.csv) (2 PUNTS)

### Integrant 1:

**Paràmetres d’entrada:**
- Fitxer CSV del producte  

**Funcionalitat:**
- Pujar un CSV amb 20 productes i guardar-los a la BD  

**Procés:**
- Tot dins d’una transacció  
- Si falla → no es guarda res  

**Return:**
- Número de registres afegits  
- En error → missatge + línia del fitxer  

---

📄 *3 de 7*

---

## 3. [CRUD] Productes (2 PUNTS)

Endpoints:

- Consultar tots els productes  
- Consultar producte per id  
- Afegir producte  
- Modificar tots els camps  
- Modificar stock  
- Modificar preu  
- Borrat físic  
- Borrat lògic  

---

## 4. [Endpoint] Query Method (2 PUNTS)

### Integrant 1:

#### GET /api/products/search/nom
- prefix  
- Retorna productes amb nom que contingui prefix i status = true  

#### GET /api/products/search/order
- camp  
- order (asc/desc)  
- Ordenar per preu  

---

### Integrant 2:

#### GET /api/products/search/condition
- condition  
- Filtrar per condició  

#### GET /api/products/search/order
- camp  
- order  
- Ordenar per rating  

---

📄 *5 de 7*

---

## 5. [Endpoint] JPQL (2 PUNTS)

### Integrant 1:

#### GET /api/products/search/order
- priceMin  
- priceMax  
- camp  
- order  
- limit  

→ Filtrar per rango de precio  

---

#### Endpoint:
- Top 5 millor relació qualitat-preu  

---

### Integrant 2:

#### GET /api/products/search/order
- ratingMin  
- ratingMax  
- camp  
- order  
- limit  

→ Filtrar per rating  

---

#### Endpoint:
- Top 10 productes nous millor valorats  

---

📄 *6 de 7*

---

## 6. [Endpoint] Paginació (2 PUNTS)

### Integrant 2:

- GET  
- Retornar 5 productes per pàgina  

---

📄 *7 de 7*

---

## RESUM FINAL

- Projecte Spring Boot amb JPA  
- CRUD complet  
- Queries (derivation + JPQL)  
- DTO obligatori  
- Transaccions  
- CSV upload  
- Paginació  
- Git + treball en equip  

---