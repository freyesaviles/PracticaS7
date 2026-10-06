package ni.edu.uam.gestionproductos.dto;

import ni.edu.uam.gestionproductos.entity.Categoria;

import java.util.List;

public class CategoriaConProductosDTO {

    private final Integer id;
    private final String nombre;
    private final boolean activa;
    private final List<ProductoResumenDTO> productos;

    public CategoriaConProductosDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.nombre = categoria.getNombre();
        this.activa = categoria.isActiva();
        this.productos = categoria.getProductos()
                .stream()
                .map(ProductoResumenDTO::new)
                .toList();
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActiva() {
        return activa;
    }

    public List<ProductoResumenDTO> getProductos() {
        return productos;
    }
}
