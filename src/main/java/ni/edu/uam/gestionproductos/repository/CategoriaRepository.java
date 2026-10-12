package ni.edu.uam.gestionproductos.repository;

import ni.edu.uam.gestionproductos.entity.Categoria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    @EntityGraph(attributePaths = "productos")
    @Query("select c from Categoria c")
    List<Categoria> findAllWithProductos();

    @EntityGraph(attributePaths = "productos")
    @Query("select c from Categoria c where c.id = :id")
    Optional<Categoria> findWithProductosById(@Param("id") Integer id);
}
