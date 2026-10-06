package ni.edu.uam.gestionproductos.dto;

import ni.edu.uam.gestionproductos.entity.Producto;

public class ProductoResumenDTO {

    private final Integer id;
    private final String codigo;
    private final String nombre;

    public ProductoResumenDTO(Producto producto) {
        this.id = producto.getId();
        this.codigo = producto.getCodigo();
        this.nombre = producto.getNombre();
    }

    public Integer getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }
}
