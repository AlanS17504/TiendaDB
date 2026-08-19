package com.tiendaDB.dao;

import com.tiendaDB.model.Categoria;
import jakarta.persistence.EntityManager;

import java.util.List;

public class CategoriaDAO {
    private final EntityManager entityManager;

    public CategoriaDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void guardar(Categoria categoria){
        entityManager.persist(categoria);
    }

    public Categoria buscarPorId(Long id){
        return entityManager.find(Categoria.class, id);
    }

    //Metodo que provoca el problema N+1
    public List<Categoria> buscarTodas(){
        String jpql = "SELECT c FROM Categoria c";
        return entityManager.createQuery(jpql, Categoria.class)
                .getResultList();
    }

    //Metodo que resuelve el problema de N+1: JOIN FETCH
    public List<Categoria> buscarTodasConProductos(){
        String jpql = "SELECT c FROM Categoria c JOIN FETCH c.productos";
        return entityManager.createQuery(jpql, Categoria.class)
                .getResultList();
    }

    public void eliminar(Long id){
        Categoria categoria = entityManager.find(Categoria.class, id);
        if(categoria!=null){
            entityManager.remove(categoria);
        }
    }
}
