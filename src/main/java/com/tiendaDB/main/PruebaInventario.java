package com.tiendaDB.main;

import com.tiendaDB.dao.CategoriaDAO;
import com.tiendaDB.dao.ProductoDAO;
import com.tiendaDB.model.Categoria;
import com.tiendaDB.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;

public class PruebaInventario {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tiendaPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CategoriaDAO categoriaDAO = new CategoriaDAO(entityManager);
        ProductoDAO productoDAO = new ProductoDAO(entityManager);

        try(entityManagerFactory; entityManager){
            //Iniciamos la transacción
            entityManager.getTransaction().begin();
            //Creamos dos categorias
            Categoria categoriaLap = new Categoria(null, "Laptops");
            Categoria categoriaSmart = new Categoria(null, "Smartphones");

            //Guardamos las categorias en la BD
            categoriaDAO.guardar(categoriaLap);
            categoriaDAO.guardar(categoriaSmart);

            //Creamos los productos y les asignamos las categorias ya creadas
            Producto asus = new Producto(null, "ASUS VIVOBOOK", new BigDecimal("19000.00"), 8, categoriaLap);
            Producto macbook = new Producto(null, "APPLE MACBOOK", new BigDecimal("22000.00"), 4, categoriaLap);
            productoDAO.guardar(asus);
            productoDAO.guardar(macbook);
            Producto xiaomi = new Producto(null, "XIAOMI 15 ULTRA", new BigDecimal("27000.00"), 9, categoriaSmart);
            productoDAO.guardar(xiaomi);

            //PRUEBA DE LISTADOS
            List<Producto> smartphones = productoDAO.buscarPorCategoriaId(categoriaSmart.getId());
            System.out.println("=======================TODOS LOS SMARTPHONES=======================");
            smartphones.forEach(s -> System.out.println("Nombre: " + s.getNombre()));

            List<Producto> productos = productoDAO.buscarTodos();
            System.out.println("=======================TODOS LOS PRODUCTOS=======================");
            productos.forEach(p -> System.out.println("Nombre: " + p.getNombre()));

            System.out.println("=======================TODOS LOS PRODUCTOS CON PRECIO ENTRE $10,000 Y $20,000=======================");
            List<Producto> productosRango = productoDAO.buscarPorRangoDePrecio(new BigDecimal("10000.00"), new BigDecimal("20000.00"));
            productosRango.forEach(p -> System.out.println("Nombre: " + p.getNombre() + "\t\tPrecio: " + p.getPrecio()));

            entityManager.getTransaction().commit();
        }catch(Exception e){
            if(entityManager.getTransaction().isActive()){
                entityManager.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }
}
