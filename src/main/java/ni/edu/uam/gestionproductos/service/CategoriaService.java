package ni.edu.uam.gestionproductos.service;

import ni.edu.uam.gestionproductos.entity.Categoria;
import ni.edu.uam.gestionproductos.entity.Producto;
import ni.edu.uam.gestionproductos.repository.CategoriaRepository;
import ni.edu.uam.gestionproductos.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository repository;
    private final ProductoRepository productoRepository;

    public CategoriaService(CategoriaRepository repository,
                            ProductoRepository productoRepository) {
        this.repository = repository;
        this.productoRepository = productoRepository;
    }

    public List<Categoria> listar() {
        return repository.findAll();
    }

    public Categoria guardar(Categoria categoria) {
        return repository.save(categoria);
    }

    public List<Producto> listarProductos(Integer categoriaId) {
        if (!repository.existsById(categoriaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe la categoría con ID " + categoriaId);
        }

        return productoRepository.findByCategoria_Id(categoriaId);
    }
}
