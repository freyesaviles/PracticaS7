package ni.edu.uam.gestionproductos.producto;

import ni.edu.uam.gestionproductos.categoria.Categoria;
import ni.edu.uam.gestionproductos.categoria.CategoriaRepository;
import ni.edu.uam.gestionproductos.proveedor.Proveedor;
import ni.edu.uam.gestionproductos.proveedor.ProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public Producto guardar(Producto producto) {
        Integer categoriaId = producto.getCategoria().getId();
        if (categoriaId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La categoría debe incluir un ID válido");
        }

        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la categoría con ID " + categoriaId));

        producto.setCategoria(categoria);

        if (producto.getProveedor() != null) {
            Integer proveedorId = producto.getProveedor().getId();
            if (proveedorId == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "El proveedor debe incluir un ID válido");
            }

            Proveedor proveedor = proveedorRepository.findById(proveedorId)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "No existe el proveedor con ID " + proveedorId));
            producto.setProveedor(proveedor);
        }

        return productoRepository.save(producto);
    }
}
