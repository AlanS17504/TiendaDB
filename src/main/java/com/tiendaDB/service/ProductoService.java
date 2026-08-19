package com.tiendaDB.service;

import com.tiendaDB.dao.CategoriaDAO;
import com.tiendaDB.dao.ProductoDAO;
import com.tiendaDB.model.Categoria;
import com.tiendaDB.model.Producto;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.util.List;

public class ProductoService {
    private final EntityManager entityManager;
    private final ProductoDAO productoDAO;
    private final CategoriaDAO categoriaDAO;

    public ProductoService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.productoDAO = new ProductoDAO(entityManager);
        this.categoriaDAO = new CategoriaDAO(entityManager);
    }

    public Producto buscarPorId(Long id){
        Producto producto = productoDAO.buscarPorId(id);
        if(producto==null){
            throw new IllegalArgumentException("Error, el producto con ID " + id + " no fue encontrado");
        }
        return producto;
    }

    public void registrarProducto(String nombre, BigDecimal precio, Integer stock, Long categoriaId){
        //Reglas del negocio, las validaciones que debemos hacer antes de tocar a la base de datos
        if(precio.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("El precio del producto debe ser mayor a 0");
        }
        if(stock<0){
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }

        try{
            entityManager.getTransaction().begin();
            Categoria categoria = categoriaDAO.buscarPorId(categoriaId);
            if(categoria == null){
                throw new IllegalArgumentException("Error al crear el producto: La categoría con ID " + categoriaId + " no existe.");
            }
            Producto producto = new Producto(null, nombre, precio, stock, categoria);
            productoDAO.guardar(producto);
            entityManager.getTransaction().commit();
            System.out.println("Éxito, el producto " + producto.getNombre() + " se guardó correctamente!");
        }catch (Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error en la transacción al registrar el producto: " + e);
        }
    }

    public void actualizarPrecio(Long productoId, BigDecimal nuevoPrecio){
        if(nuevoPrecio.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("El nuevo precio debe ser mayor que $0.");
        }
        try{
            entityManager.getTransaction().begin();
            Producto producto = productoDAO.buscarPorId(productoId);
            if(producto==null){
                throw new IllegalArgumentException("Error, el producto con ID " + productoId + " no existe.");
            }
            producto.setPrecio(nuevoPrecio);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Ocurrió un error inesperado al tratar de modificar el precio del producto. ", e);
        }
    }

    public List<Producto> consultarPorRango(BigDecimal min, BigDecimal max){
        if(min.compareTo(BigDecimal.ZERO) <0 || max.compareTo(BigDecimal.ZERO) <0){
            throw new IllegalArgumentException("Los rangos deben ser números mayores o iguales que 0");
        }
        if(min.compareTo(max) > 0){
            throw new IllegalArgumentException("El rango es inválido, el rango minimo no puede ser mayor al maximo.");
        }
        return productoDAO.buscarPorRangoDePrecio(min, max);
    }

}
