package ni.edu.uam.gestionproductos.service;

import ni.edu.uam.gestionproductos.dto.ProductoRequestDTO;
import ni.edu.uam.gestionproductos.entity.Categoria;
import ni.edu.uam.gestionproductos.entity.Etiqueta;
import ni.edu.uam.gestionproductos.entity.Producto;
import ni.edu.uam.gestionproductos.entity.Proveedor;
import ni.edu.uam.gestionproductos.repository.CategoriaRepository;
import ni.edu.uam.gestionproductos.repository.EtiquetaRepository;
import ni.edu.uam.gestionproductos.repository.ProductoRepository;
import ni.edu.uam.gestionproductos.repository.ProveedorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProveedorRepository proveedorRepository;
    private final EtiquetaRepository etiquetaRepository;

    public ProductoService(ProductoRepository productoRepository,
                           CategoriaRepository categoriaRepository,
                           ProveedorRepository proveedorRepository,
                           EtiquetaRepository etiquetaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.proveedorRepository = proveedorRepository;
        this.etiquetaRepository = etiquetaRepository;
    }

    @Transactional(readOnly = true)
    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe el producto con ID " + id));
    }

    @Transactional
    public Producto guardar(ProductoRequestDTO dto) {
        Producto producto = new Producto();
        aplicarDatos(producto, dto);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Integer id, ProductoRequestDTO dto) {
        Producto producto = buscarPorId(id);
        aplicarDatos(producto, dto);
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(Integer id) {
        Producto producto = buscarPorId(id);
        producto.getEtiquetas().clear();
        productoRepository.delete(producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarPorCategoria(Integer categoriaId) {
        if (!categoriaRepository.existsById(categoriaId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe la categoría con ID " + categoriaId);
        }
        return productoRepository.findByCategoria_Id(categoriaId);
    }

    @Transactional
    public Producto agregarEtiqueta(Integer productoId, Integer etiquetaId) {
        Producto producto = buscarPorId(productoId);
        Etiqueta etiqueta = buscarEtiqueta(etiquetaId);
        producto.getEtiquetas().add(etiqueta);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto eliminarEtiqueta(Integer productoId, Integer etiquetaId) {
        Producto producto = buscarPorId(productoId);
        Etiqueta etiqueta = buscarEtiqueta(etiquetaId);

        if (!producto.getEtiquetas().remove(etiqueta)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "El producto no tiene asociada la etiqueta indicada");
        }

        return productoRepository.save(producto);
    }

    @Transactional(readOnly = true)
    public List<Producto> listarPorEtiqueta(Integer etiquetaId) {
        buscarEtiqueta(etiquetaId);
        return productoRepository.findDistinctByEtiquetas_Id(etiquetaId);
    }

    private Etiqueta buscarEtiqueta(Integer etiquetaId) {
        return etiquetaRepository.findById(etiquetaId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la etiqueta con ID " + etiquetaId));
    }

    private void aplicarDatos(Producto producto, ProductoRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe la categoría con ID " + dto.getCategoriaId()));

        producto.setCodigo(dto.getCodigo());
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto.setExistencia(dto.getExistencia());
        producto.setCategoria(categoria);

        if (dto.getProveedorId() == null) {
            producto.setProveedor(null);
        } else {
            Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "No existe el proveedor con ID " + dto.getProveedorId()));
            producto.setProveedor(proveedor);
        }
    }
}
