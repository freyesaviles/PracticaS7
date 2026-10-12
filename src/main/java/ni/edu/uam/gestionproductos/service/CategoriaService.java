package ni.edu.uam.gestionproductos.service;

import ni.edu.uam.gestionproductos.entity.Categoria;
import ni.edu.uam.gestionproductos.entity.Producto;
import ni.edu.uam.gestionproductos.dto.CategoriaConProductosDTO;
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
        return repository.findAllWithProductos();
    }

    public List<CategoriaConProductosDTO> listarConProductos() {
        return repository.findAllWithProductos()
                .stream()
                .map(CategoriaConProductosDTO::new)
                .toList();
    }

    public Categoria guardar(Categoria categoria) {
        return repository.save(categoria);
    }

    public CategoriaConProductosDTO buscarPorId(Integer id) {
        return repository.findWithProductosById(id)
                .map(CategoriaConProductosDTO::new)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la categoría con ID " + id));
    }

    public Categoria actualizar(Integer id, Categoria datos) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la categoría con ID " + id));

        categoria.setNombre(datos.getNombre());
        categoria.setActiva(datos.isActiva());
        return repository.save(categoria);
    }

    public void eliminar(Integer id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe la categoría con ID " + id);
        }

        if (productoRepository.existsByCategoria_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede eliminar una categoría que tiene productos");
        }

        repository.deleteById(id);
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
