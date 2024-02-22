package vaiagendar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vaiagendar.model.Servicos;

@Repository
public interface ServicosRepository extends JpaRepository<Servicos, Long> {
    
    @Query("FROM Servicos s WHERE (:id is null or s.id = :id) " +
    "AND (:nome is '' or UPPER(s.nome) like %:nome%)")
    Page<Servicos> searchServicos(@Param("id") Long id, @Param("nome") String nome, Pageable pageable);

}
