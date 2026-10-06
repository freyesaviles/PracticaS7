package ni.edu.uam.gestionproductos.controller;

import jakarta.validation.Valid;
import ni.edu.uam.gestionproductos.entity.Categoria;
import ni.edu.uam.gestionproductos.entity.Producto;
import ni.edu.uam.gestionproductos.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Categoria> listar() {
        return service.listar();
    }

    @GetMapping("/{categoriaId}/productos")
    public List<Producto> listarProductos(@PathVariable Integer categoriaId) {
        return service.listarProductos(categoriaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria guardar(@Valid @RequestBody Categoria categoria) {
        return service.guardar(categoria);
    }
}
