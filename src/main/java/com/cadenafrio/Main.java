package com.cadenafrio;

import com.cadenafrio.dao.*;
import com.cadenafrio.model.*;
import com.cadenafrio.util.HibernateUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        CooperativaDAO cooperativaDAO = new CooperativaDAO();
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        CavaDAO cavaDAO = new CavaDAO();
        ProductoDAO productoDAO = new ProductoDAO();
        LoteInventarioDAO loteDAO = new LoteInventarioDAO();

        try {
            // ===== Datos base =====
            Cooperativa coop = new Cooperativa();
            coop.setNombre("Cooperativa Pesquera de Necocli");
            coop.setMunicipio("Necocli");
            coop.setTelefono("3000000000");
            cooperativaDAO.crear(coop);

            Usuario usuario = new Usuario();
            usuario.setCooperativa(coop);
            usuario.setNombre("Jean David");
            usuario.setEmail("jean" + System.currentTimeMillis() + "@correo.com");
            usuario.setPasswordHash("hash123");
            usuario.setRol(Rol.ADMIN);
            usuarioDAO.crear(usuario);

            // ===== 1. CRUD CAVA =====
            System.out.println("\n=== CRUD CAVA ===");
            Cava cava = new Cava();
            cava.setCooperativa(coop);
            cava.setNombre("Cava 1");
            cava.setCapacidadKg(new BigDecimal("500.00"));
            cava.setTempCritica(new BigDecimal("-18.00"));
            cavaDAO.crear(cava); // CREATE
            System.out.println("CREATE: cava creada con id " + cava.getIdCava());

            Cava cavaLeida = cavaDAO.buscarPorId(cava.getIdCava()); // READ
            System.out.println("READ: " + cavaLeida.getNombre() + " | capacidad " + cavaLeida.getCapacidadKg() + " kg");

            cava.setCapacidadKg(new BigDecimal("800.00"));
            cavaDAO.actualizar(cava); // UPDATE
            System.out.println("UPDATE: nueva capacidad " + cavaDAO.buscarPorId(cava.getIdCava()).getCapacidadKg() + " kg");

            // ===== 2. CRUD PRODUCTO =====
            System.out.println("\n=== CRUD PRODUCTO ===");
            Producto producto = new Producto();
            producto.setEspecie("Pargo Rojo");
            producto.setPresentacion("Entero eviscerado");
            producto.setUnidad("KG");
            producto.setPrecioKg(new BigDecimal("22000.00"));
            productoDAO.crear(producto); // CREATE
            System.out.println("CREATE: producto creado con id " + producto.getIdProducto());

            Producto prodLeido = productoDAO.buscarPorId(producto.getIdProducto()); // READ
            System.out.println("READ: " + prodLeido.getEspecie() + " | $" + prodLeido.getPrecioKg() + "/kg");

            producto.setPrecioKg(new BigDecimal("25000.00"));
            productoDAO.actualizar(producto); // UPDATE
            System.out.println("UPDATE: nuevo precio $" + productoDAO.buscarPorId(producto.getIdProducto()).getPrecioKg() + "/kg");

            // ===== 3. CRUD LOTE INVENTARIO =====
            System.out.println("\n=== CRUD LOTE INVENTARIO ===");
            LoteInventario lote = new LoteInventario();
            lote.setProducto(producto);
            lote.setCava(cava);
            lote.setUsuario(usuario);
            lote.setCantidadKg(new BigDecimal("120.00"));
            lote.setFechaIngreso(LocalDateTime.now());
            lote.setFechaVencimiento(LocalDate.now().plusDays(15));
            lote.setEstado(EstadoLote.DISPONIBLE);
            loteDAO.crear(lote); // CREATE
            System.out.println("CREATE: lote creado con id " + lote.getIdLote());

            LoteInventario loteLeido = loteDAO.buscarPorId(lote.getIdLote()); // READ
            System.out.println("READ: lote #" + loteLeido.getIdLote() + " | cantidad " + loteLeido.getCantidadKg() + " kg | estado " + loteLeido.getEstado());

            lote.setCantidadKg(new BigDecimal("95.50"));
            loteDAO.actualizar(lote); // UPDATE
            System.out.println("UPDATE: nueva cantidad " + loteDAO.buscarPorId(lote.getIdLote()).getCantidadKg() + " kg");

            // ===== DELETE (en orden para respetar claves foráneas: lote -> producto -> cava) =====
            System.out.println("\n=== DELETE ===");
            loteDAO.eliminar(lote.getIdLote());
            System.out.println("DELETE: lote eliminado con id " + lote.getIdLote());

            productoDAO.eliminar(producto.getIdProducto());
            System.out.println("DELETE: producto eliminado con id " + producto.getIdProducto());

            cavaDAO.eliminar(cava.getIdCava());
            System.out.println("DELETE: cava eliminada con id " + cava.getIdCava());

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
