package com.ra34.projecte2.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.ra34.projecte2.dto.ProductCreateDTO;
import com.ra34.projecte2.dto.ProductDTO;
import com.ra34.projecte2.dto.ProductUpdateDTO;
import com.ra34.projecte2.exception.ResourceNotFoundException;
import com.ra34.projecte2.model.Condition;
import com.ra34.projecte2.model.Product;
import com.ra34.projecte2.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para productos.
 * Gestiona CRUD, búsquedas y carga de datos desde CSV.
 * Implementa transaccionalidad para garantizar consistencia.
 */
@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // ==================== CONVERSIÓN DTO <-> ENTITY ====================

    /**
     * Convierte una entidad Product a ProductDTO.
     */
    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getStock(),
                product.getPrice(),
                product.getRating(),
                product.getCondition(),
                product.getStatus(),
                product.getDataCreated(),
                product.getDataUpdated()
        );
    }

    /**
     * Convierte ProductCreateDTO a entidad Product.
     */
    private Product convertToEntity(ProductCreateDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setStock(dto.getStock());
        product.setPrice(dto.getPrice());
        product.setRating(dto.getRating());
        product.setCondition(dto.getCondition() != null ? dto.getCondition() : Condition.NUEVO);
        product.setStatus(true);
        return product;
    }

    // ==================== CRUD BASIC ====================

    /**
     * Obtiene todos los productos activos.
     */
    public List<ProductDTO> getAllProducts() {
        log.info("Obteniendo todos los productos activos");
        return productRepository.findByStatusTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un producto por ID si está activo.
     */
    public ProductDTO getProductById(Long id) {
        log.info("Obteniendo producto con ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        
        if (!product.getStatus()) {
            throw new ResourceNotFoundException("El producto ha sido eliminado");
        }
        
        return convertToDTO(product);
    }

    /**
     * Crea un nuevo producto.
     */
    public ProductDTO createProduct(ProductCreateDTO createDTO) {
        log.info("Creando nuevo producto: {}", createDTO.getName());
        Product product = convertToEntity(createDTO);
        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    /**
     * Actualiza todos los campos de un producto.
     */
    public ProductDTO updateProduct(Long id, ProductUpdateDTO updateDTO) {
        log.info("Actualizando producto con ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (!product.getStatus()) {
            throw new ResourceNotFoundException("El producto ha sido eliminado");
        }

        // Actualizar solo los campos no nulos
        if (updateDTO.getName() != null) product.setName(updateDTO.getName());
        if (updateDTO.getDescription() != null) product.setDescription(updateDTO.getDescription());
        if (updateDTO.getStock() != null) product.setStock(updateDTO.getStock());
        if (updateDTO.getPrice() != null) product.setPrice(updateDTO.getPrice());
        if (updateDTO.getRating() != null) product.setRating(updateDTO.getRating());
        if (updateDTO.getCondition() != null) product.setCondition(updateDTO.getCondition());

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    /**
     * Actualiza solo el stock de un producto.
     */
    public ProductDTO updateStock(Long id, Integer newStock) {
        log.info("Actualizando stock del producto ID {} a {}", id, newStock);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (!product.getStatus()) {
            throw new ResourceNotFoundException("El producto ha sido eliminado");
        }

        product.setStock(newStock);
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    /**
     * Actualiza solo el precio de un producto.
     */
    public ProductDTO updatePrice(Long id, BigDecimal newPrice) {
        log.info("Actualizando precio del producto ID {} a {}", id, newPrice);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));

        if (!product.getStatus()) {
            throw new ResourceNotFoundException("El producto ha sido eliminado");
        }

        product.setPrice(newPrice);
        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    /**
     * Elimina físicamente un producto (borrado permanente).
     */
    public void deleteProduct(Long id) {
        log.info("Eliminando físicamente producto con ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        productRepository.delete(product);
    }

    /**
     * Elimina lógicamente un producto (status = false).
     */
    public void logicalDeleteProduct(Long id) {
        log.info("Eliminando lógicamente producto con ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
        product.setStatus(false);
        productRepository.save(product);
    }

    // ==================== BÚSQUEDAS Y FILTROS ====================

    /**
     * Busca productos por nombre (prefix).
     */
    public List<ProductDTO> searchByName(String prefix) {
        log.info("Buscando productos con nombre que contenga: {}", prefix);
        return productRepository.findByNameContainingIgnoreCaseAndStatusTrue(prefix)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos por condición.
     */
    public List<ProductDTO> searchByCondition(Condition condition) {
        log.info("Buscando productos con condición: {}", condition);
        return productRepository.findByConditionAndStatusTrue(condition)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Ordena productos por campo y dirección.
     * Campo soportado: "precio", "rating"
     * Order: "asc" o "desc"
     */
    public List<ProductDTO> orderProducts(String field, String order) {
        log.info("Ordenando productos por {} {}", field, order);
        List<Product> products = new ArrayList<>();

        switch (field.toLowerCase()) {
            case "precio":
                products = "desc".equalsIgnoreCase(order) 
                    ? productRepository.findByStatusTrueOrderByPriceDesc()
                    : productRepository.findByStatusTrueOrderByPriceAsc();
                break;
            case "rating":
                products = "desc".equalsIgnoreCase(order)
                    ? productRepository.findByStatusTrueOrderByRatingDesc()
                    : productRepository.findByStatusTrueOrderByRatingAsc();
                break;
            default:
                throw new IllegalArgumentException("Campo no soportado: " + field);
        }

        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos en rango de precios.
     */
    public List<ProductDTO> searchByPriceRange(BigDecimal priceMin, BigDecimal priceMax) {
        log.info("Buscando productos en rango de precio: {} - {}", priceMin, priceMax);
        return productRepository.findProductsByPriceRange(priceMin, priceMax)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los Top 5 productos con mejor relación calidad-precio.
     */
    public List<ProductDTO> getTop5BestQualityPrice() {
        log.info("Obteniendo Top 5 productos con mejor relación calidad-precio");
        return productRepository.findTop5BestQualityPrice(Pageable.ofSize(5))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca productos en rango de rating.
     */
    public List<ProductDTO> searchByRatingRange(BigDecimal ratingMin, BigDecimal ratingMax) {
        log.info("Buscando productos en rango de rating: {} - {}", ratingMin, ratingMax);
        return productRepository.findProductsByRatingRange(ratingMin, ratingMax)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene los Top 10 productos nuevos mejor valorados.
     */
    public List<ProductDTO> getTop10NewBestRated() {
        log.info("Obteniendo Top 10 productos nuevos mejor valorados");
        return productRepository.findTop10NewBestRated(Condition.NUEVO, Pageable.ofSize(10))
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene productos con paginación.
     */
    public Page<ProductDTO> getProductsPaginated(Pageable pageable) {
        log.info("Obteniendo productos paginados: página {}, tamaño {}", pageable.getPageNumber(), pageable.getPageSize());
        return productRepository.findByStatusTrue(pageable)
                .map(this::convertToDTO);
    }

    // ==================== CARGA MASIVA CSV ====================

    /**
     * Carga productos desde un archivo CSV.
     * TRANSACCIONAL: Si falla, no se guarda nada.
     * Formato CSV esperado:
     * name,description,stock,price,rating,condition
     * 
     * @param file Archivo CSV multipart
     * @return Número de productos cargados exitosamente
     * @throws IOException Si hay error al leer el archivo
     */
    @Transactional
    public int uploadProductsFromCSV(MultipartFile file) throws IOException {
        log.info("Iniciando carga de productos desde CSV");
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío");
        }

        int successCount = 0;
        int lineNumber = 0;

        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            boolean firstLine = true;

            while ((nextLine = reader.readNext()) != null) {
                lineNumber++;
                
                // Saltar header
                if (firstLine) {
                    firstLine = false;
                    continue;
                }

                try {
                    // Validar que tenga los 6 campos esperados
                    if (nextLine.length < 6) {
                        log.warn("Línea {} tiene menos campos de los esperados", lineNumber);
                        continue;
                    }

                    // Parsear CSV a entidad
                    Product product = parseProductFromCSV(nextLine);
                    productRepository.save(product);
                    successCount++;
                    log.debug("Producto {} guardado exitosamente", product.getName());

                } catch (Exception e) {
                    log.error("Error al procesar línea {}: {}", lineNumber, e.getMessage());
                    // Aquí la transacción se revertirá por la excepción
                    throw new RuntimeException("Error en línea " + lineNumber + ": " + e.getMessage(), e);
                }
            }

            log.info("Carga completada. {} productos guardados", successCount);
            return successCount;

        } catch (CsvValidationException e) {
            log.error("Error al validar CSV: {}", e.getMessage());
            throw new RuntimeException("Error al validar CSV", e);
        }
    }

    /**
     * Parsea una línea CSV a entidad Product.
     * Formato: name,description,stock,price,rating,condition
     */
    private Product parseProductFromCSV(String[] line) {
        Product product = new Product();
        
        // name (index 0)
        String name = line[0].trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        product.setName(name);

        // description (index 1)
        String description = line[1].trim();
        product.setDescription(description.isEmpty() ? null : description);

        // stock (index 2)
        int stock = Integer.parseInt(line[2].trim());
        product.setStock(stock);

        // price (index 3)
        BigDecimal price = new BigDecimal(line[3].trim());
        product.setPrice(price);

        // rating (index 4)
        String ratingStr = line[4].trim();
        BigDecimal rating = ratingStr.isEmpty() ? null : new BigDecimal(ratingStr);
        product.setRating(rating);

        // condition (index 5)
        String conditionStr = line[5].trim();
        try {
            Condition condition = Condition.valueOf(conditionStr.toUpperCase());
            product.setCondition(condition);
        } catch (IllegalArgumentException e) {
            product.setCondition(Condition.NUEVO);
        }

        product.setStatus(true);
        return product;
    }

}
