package com.tiendaDB.service;

import com.tiendaDB.dao.CategoriaDAO;
import com.tiendaDB.model.Categoria;
import jakarta.persistence.EntityManager;

public class CategoriaService {
    private final EntityManager entityManager;
    private final CategoriaDAO categoriaDAO;

    public CategoriaService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.categoriaDAO = new CategoriaDAO(entityManager);
    }

    public Categoria crearCategoria(String nombre){
        if(nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre de la categoría no puede ser nulo o estar en blanco.");
        }
        if(nombre.length()<3){
            throw new IllegalArgumentException("El nombre es demasiado corto, debe tener 3 caracteres o más");
        }
        try {
            entityManager.getTransaction().begin();
            Categoria categoria = new Categoria(null, nombre);
            categoriaDAO.guardar(categoria);
            entityManager.getTransaction().commit();
            System.out.println("La categoría " + categoria.getNombre() + " se guardó correctamente!");
            return categoria;
        }catch (Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Ocurrió un error al registrar la categoría: ", e);
        }
    }

    public Categoria buscarPorId(Long id){
        Categoria categoria = categoriaDAO.buscarPorId(id);
        if(categoria==null){
            throw new IllegalArgumentException("La categoría con ID " + id + " no fue encontrada.");
        }
        return categoria;
    }
}
