package ni.edu.uam.gestionproductos.repository;

import ni.edu.uam.gestionproductos.entity.Categoria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @EntityGraph(attributePaths = "productos")
    @Query("select c from Categoria c")
    List<Categoria> findAllWithProductos();
}
