package com.ra34.projecte2.repository;

import com.ra34.projecte2.model.Condition;
import com.ra34.projecte2.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository para la entidad Product.
 * Extiende JpaRepository para acceso a datos mediante ORM.
 * Incluye Query Derivation y @Query JPQL.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // ==================== QUERY DERIVATION ====================

    /**
     * Busca productos cuyo nombre comienza con el prefijo dado.
     * Solo retorna productos activos (status = true).
     */
    List<Product> findByNameContainingIgnoreCaseAndStatusTrue(String prefix);

    /**
     * Busca productos por condición específica.
     */
    List<Product> findByConditionAndStatusTrue(Condition condition);

    /**
     * Busca todos los productos activos, ordenados por precio ascendente.
     */
    List<Product> findByStatusTrueOrderByPriceAsc();

    /**
     * Busca todos los productos activos, ordenados por precio descendente.
     */
    List<Product> findByStatusTrueOrderByPriceDesc();

    /**
     * Busca todos los productos activos, ordenados por rating descendente.
     */
    List<Product> findByStatusTrueOrderByRatingDesc();

    /**
     * Busca todos los productos activos, ordenados por rating ascendente.
     */
    List<Product> findByStatusTrueOrderByRatingAsc();

    /**
     * Busca todos los productos activos.
     */
    List<Product> findByStatusTrue();

    /**
     * Busca todos los productos activos con paginación.
     */
    Page<Product> findByStatusTrue(Pageable pageable);

    // ==================== JPQL QUERIES ====================

    /**
     * Busca productos en un rango de precios.
     * 
     * @param priceMin Precio mínimo (inclusive)
     * @param priceMax Precio máximo (inclusive)
     * @return Lista de productos activos dentro del rango de precios
     */
    @Query("SELECT p FROM Product p WHERE p.status = true AND p.price BETWEEN :priceMin AND :priceMax " +
            "ORDER BY p.price ASC")
    List<Product> findProductsByPriceRange(@Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax);

    /**
     * Busca productos en un rango de precios con ordenamiento personalizado.
     * 
     * @param priceMin Precio mínimo (inclusive)
     * @param priceMax Precio máximo (inclusive)
     * @param limit    Número máximo de resultados
     * @return Lista de productos activos dentro del rango de precios
     */
    @Query(value = "SELECT p FROM Product p WHERE p.status = true AND p.price BETWEEN :priceMin AND :priceMax " +
            "ORDER BY p.price ASC LIMIT :limit", nativeQuery = false)
    List<Product> findProductsByPriceRangeWithLimit(@Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            @Param("limit") int limit);

    /**
     * Top 5 productos con mejor relación calidad-precio.
     * Ordenado por rating descendente y precio ascendente.
     * 
     * @return Lista de los 5 mejores productos
     */
    @Query("SELECT p FROM Product p WHERE p.status = true AND p.rating IS NOT NULL " +
            "ORDER BY p.rating DESC, p.price ASC")
    List<Product> findTop5BestQualityPrice(Pageable pageable);

    /**
     * Busca productos en un rango de rating.
     * 
     * @param ratingMin Rating mínimo (inclusive)
     * @param ratingMax Rating máximo (inclusive)
     * @return Lista de productos activos dentro del rango de rating
     */
    @Query("SELECT p FROM Product p WHERE p.status = true AND p.rating BETWEEN :ratingMin AND :ratingMax " +
            "ORDER BY p.rating DESC")
    List<Product> findProductsByRatingRange(@Param("ratingMin") BigDecimal ratingMin,
            @Param("ratingMax") BigDecimal ratingMax);

    /**
     * Top 10 productos nuevos mejor valorados.
     * Filtra por condición NUEVO y ordena por rating descendente.
     * 
     * @param condition La condición del producto (NUEVO)
     * @return Lista de los 10 mejores productos nuevos
     */
    @Query("SELECT p FROM Product p WHERE p.status = true AND p.condition = :condition AND p.rating IS NOT NULL " +
            "ORDER BY p.rating DESC, p.dataCreated DESC")
    List<Product> findTop10NewBestRated(@Param("condition") Condition condition, Pageable pageable);

}
