package com.ra34.projecte2.controller;

import com.ra34.projecte2.dto.ErrorDTO;
import com.ra34.projecte2.dto.ProductCreateDTO;
import com.ra34.projecte2.dto.ProductDTO;
import com.ra34.projecte2.dto.ProductUpdateDTO;
import com.ra34.projecte2.exception.ResourceNotFoundException;
import com.ra34.projecte2.model.Condition;
import com.ra34.projecte2.service.ProductService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * REST Controller para gestionar productos.
 * Proporciona endpoints para CRUD, búsquedas y carga masiva de CSV.
 */
@RestController
@RequestMapping("/api/products")
@Slf4j
public class ProductController {

    @Autowired
    private ProductService productService;

    // ==================== CRUD ENDPOINTS ====================

    /**
     * GET /api/products
     * Obtiene todos los productos activos.
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        log.info("GET /api/products - Obteniendo todos los productos");
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/{id}
     * Obtiene un producto específico por ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        log.info("GET /api/products/{} - Obteniendo producto", id);
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * POST /api/products
     * Crea un nuevo producto.
     */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductCreateDTO createDTO) {
        log.info("POST /api/products - Creando nuevo producto: {}", createDTO.getName());
        ProductDTO newProduct = productService.createProduct(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
    }

    /**
     * PUT /api/products/{id}
     * Actualiza todos los campos de un producto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateDTO updateDTO) {
        log.info("PUT /api/products/{} - Actualizando producto", id);
        ProductDTO updatedProduct = productService.updateProduct(id, updateDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * PATCH /api/products/{id}/stock
     * Actualiza solo el stock de un producto.
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductDTO> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        log.info("PATCH /api/products/{}/stock - Actualizando stock a {}", id, stock);
        ProductDTO updatedProduct = productService.updateStock(id, stock);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * PATCH /api/products/{id}/price
     * Actualiza solo el precio de un producto.
     */
    @PatchMapping("/{id}/price")
    public ResponseEntity<ProductDTO> updatePrice(
            @PathVariable Long id,
            @RequestParam BigDecimal price) {
        log.info("PATCH /api/products/{}/price - Actualizando precio a {}", id, price);
        ProductDTO updatedProduct = productService.updatePrice(id, price);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * DELETE /api/products/{id}
     * Elimina físicamente un producto (borrado permanente).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{} - Eliminando producto", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * DELETE /api/products/{id}/logical
     * Elimina lógicamente un producto (status = false).
     */
    @DeleteMapping("/{id}/logical")
    public ResponseEntity<Void> logicalDeleteProduct(@PathVariable Long id) {
        log.info("DELETE /api/products/{}/logical - Eliminando lógicamente producto", id);
        productService.logicalDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== BULK UPLOAD ====================

    /**
     * POST /api/products/upload
     * Carga masiva de productos desde archivo CSV.
     * Formato CSV: name,description,stock,price,rating,condition
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file) {
        log.info("POST /api/products/upload - Cargando CSV");
        try {
            int count = productService.uploadProductsFromCSV(file);
            return ResponseEntity.ok("{\"message\": \"Se cargaron " + count + " productos\"}");
        } catch (IOException e) {
            log.error("Error al cargar CSV: {}", e.getMessage());
            ErrorDTO error = new ErrorDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    "Error al cargar el archivo",
                    e.getMessage()
            );
            return ResponseEntity.badRequest().body(error);
        } catch (RuntimeException e) {
            log.error("Error transaccional al cargar CSV: {}", e.getMessage());
            ErrorDTO error = new ErrorDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error al procesar el archivo - Transacción revertida",
                    e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ==================== SEARCH ENDPOINTS - QUERY DERIVATION ====================

    /**
     * GET /api/products/search/nombre?prefix=...
     * Busca productos cuyo nombre contenga el prefix.
     */
    @GetMapping("/search/nombre")
    public ResponseEntity<List<ProductDTO>> searchByName(@RequestParam String prefix) {
        log.info("GET /api/products/search/nombre - Buscando por nombre: {}", prefix);
        List<ProductDTO> products = productService.searchByName(prefix);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/search/condition?condition=...
     * Busca productos por condición específica.
     */
    @GetMapping("/search/condition")
    public ResponseEntity<List<ProductDTO>> searchByCondition(@RequestParam Condition condition) {
        log.info("GET /api/products/search/condition - Buscando por condición: {}", condition);
        List<ProductDTO> products = productService.searchByCondition(condition);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/search/order?campo=precio&order=asc
     * Ordena productos por campo (precio o rating) en orden ascendente o descendente.
     */
    @GetMapping("/search/order")
    public ResponseEntity<List<ProductDTO>> orderProducts(
            @RequestParam String campo,
            @RequestParam(defaultValue = "asc") String order) {
        log.info("GET /api/products/search/order - Ordenando por {} {}", campo, order);
        List<ProductDTO> products = productService.orderProducts(campo, order);
        return ResponseEntity.ok(products);
    }

    // ==================== SEARCH ENDPOINTS - JPQL ====================

    /**
     * GET /api/products/search/price-range?priceMin=...&priceMax=...
     * Busca productos en rango de precios.
     */
    @GetMapping("/search/price-range")
    public ResponseEntity<List<ProductDTO>> searchByPriceRange(
            @RequestParam BigDecimal priceMin,
            @RequestParam BigDecimal priceMax) {
        log.info("GET /api/products/search/price-range - Buscando en rango: {} - {}", priceMin, priceMax);
        List<ProductDTO> products = productService.searchByPriceRange(priceMin, priceMax);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/search/best-quality-price
     * Top 5 productos con mejor relación calidad-precio.
     */
    @GetMapping("/search/best-quality-price")
    public ResponseEntity<List<ProductDTO>> getBestQualityPrice() {
        log.info("GET /api/products/search/best-quality-price - Obteniendo Top 5");
        List<ProductDTO> products = productService.getTop5BestQualityPrice();
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/search/rating-range?ratingMin=...&ratingMax=...
     * Busca productos en rango de rating.
     */
    @GetMapping("/search/rating-range")
    public ResponseEntity<List<ProductDTO>> searchByRatingRange(
            @RequestParam BigDecimal ratingMin,
            @RequestParam BigDecimal ratingMax) {
        log.info("GET /api/products/search/rating-range - Buscando en rango: {} - {}", ratingMin, ratingMax);
        List<ProductDTO> products = productService.searchByRatingRange(ratingMin, ratingMax);
        return ResponseEntity.ok(products);
    }

    /**
     * GET /api/products/search/top-new-rated
     * Top 10 productos nuevos mejor valorados.
     */
    @GetMapping("/search/top-new-rated")
    public ResponseEntity<List<ProductDTO>> getTopNewRated() {
        log.info("GET /api/products/search/top-new-rated - Obteniendo Top 10");
        List<ProductDTO> products = productService.getTop10NewBestRated();
        return ResponseEntity.ok(products);
    }

    // ==================== PAGINATION ====================

    /**
     * GET /api/products/paginated?page=0&size=5
     * Retorna productos con paginación (5 por página por defecto).
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<ProductDTO>> getProductsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("GET /api/products/paginated - Página {}, tamaño {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.getProductsPaginated(pageable);
        return ResponseEntity.ok(products);
    }

    // ==================== EXCEPTION HANDLERS ====================

    /**
     * Manejo de excepciones de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException e) {
        log.error("Error de validación: {}", e.getMessage());
        ErrorDTO error = new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validación",
                e.getBindingResult().getFieldError().getDefaultMessage()
        );
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * Manejo de ResourceNotFoundException.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleResourceNotFound(ResourceNotFoundException e) {
        log.error("Recurso no encontrado: {}", e.getMessage());
        ErrorDTO error = new ErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Manejo genérico de excepciones.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGeneralException(Exception e) {
        log.error("Error no controlado: {}", e.getMessage(), e);
        ErrorDTO error = new ErrorDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
