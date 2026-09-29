package ni.edu.uam.gestionproductos.producto;

import ni.edu.uam.gestionproductos.categoria.Categoria;
import ni.edu.uam.gestionproductos.categoria.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public Producto guardar(Producto producto) {
        Integer categoriaId = producto.getCategoria().getId();
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la categoría con ID " + categoriaId));

        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }
}
