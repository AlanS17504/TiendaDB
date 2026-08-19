package com.tiendaDB.main;

import com.tiendaDB.model.Categoria;
import com.tiendaDB.model.Producto;
import com.tiendaDB.service.CategoriaService;
import com.tiendaDB.service.ProductoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jdk.swing.interop.SwingInterOpUtils;

import java.math.BigDecimal;
import java.util.List;

public class PruebaService {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("tiendaPU");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        CategoriaService categoriaService = new CategoriaService(entityManager);
        ProductoService productoService = new ProductoService(entityManager);
        try(entityManagerFactory; entityManager) {
//            System.out.println("================CREAR CATEGORÍA============");
//            Categoria videojuegos = categoriaService.crearCategoria("Videojuegos");
//            System.out.println("================BUSCAR CATEGORÍA POR ID============");
//            Categoria buscar = categoriaService.buscarPorId(4L);
//            System.out.println("Categoría encontrada: " + buscar.getNombre());
//            productoService.registrarProducto("Gears 3", new BigDecimal("1200.00"), 5, 3L);
//            productoService.registrarProducto("Mario bros 3", new BigDecimal("1000.00"), 2, 3L);
//            productoService.registrarProducto("Super Smash Bros Ultimate", new BigDecimal("1500.00"), 8, 3L);
//            System.out.println("================ACTUALIZAR PRECIO DE UN PRODUCTO============");
//            Producto producto = productoService.buscarPorId(3L);
//            productoService.actualizarPrecio(producto.getId(), new BigDecimal("1100.00"));
//            System.out.println("================PRODUCTOS DE UN DETERMINADO RANGO DE PRECIO (900-2000)============");
//            List<Producto> productos = productoService.consultarPorRango(new BigDecimal("900.00"), new BigDecimal("2000.00"));
//            productos.forEach(p -> System.out.println("Nombre: " + p.getNombre()));
            System.out.println("\n\n=============IMPRIMIENDO SIN JOIN FETCH=============");
            categoriaService.imprimirReporte(false);
            entityManager.clear();
            System.out.println("\n\n=============IMPRIMIENDO CON JOIN FETCH=============");
            categoriaService.imprimirReporte(true);

        }catch (IllegalArgumentException e){
            System.out.println("Validación fallida: " + e.getMessage());
        }catch (RuntimeException e){
            System.out.println("Error de base de datos o sistema: " + e.getMessage());
        }catch (Exception e){
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }
}
