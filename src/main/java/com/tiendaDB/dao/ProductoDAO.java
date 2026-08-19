package com.tiendaDB.dao;

import com.tiendaDB.model.Producto;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

public class ProductoDAO {
    private final EntityManager entityManager;

    public ProductoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void guardar(Producto producto){
        entityManager.persist(producto);
    }

    public Producto buscarPorId(Long id){
        return entityManager.find(Producto.class, id);
    }

    public List<Producto> buscarTodos(){
        String jpql = "SELECT p FROM Producto p";
        return entityManager.createQuery(jpql, Producto.class)
                .getResultList();
    }

    public List<Producto> buscarPorCategoriaId(Long categoriaId){
        String jpql = "SELECT p FROM Producto p WHERE p.categoria.id = :categoriaId";
        return entityManager.createQuery(jpql, Producto.class)
                .setParameter("categoriaId", categoriaId)
                .getResultList();
    }

    public List<Producto> buscarPorRangoDePrecio(BigDecimal precioMin, BigDecimal precioMax){
        String jpql = "SELECT p FROM Producto p WHERE p.precio BETWEEN :precioMin AND :precioMax";
        return entityManager.createQuery(jpql, Producto.class)
                .setParameter("precioMin", precioMin)
                .setParameter("precioMax", precioMax)
                .getResultList();
    }

    public void eliminar(Long id){
        Producto producto = entityManager.find(Producto.class, id);
        if(producto!=null){
            entityManager.remove(producto);
        }
    }
}
