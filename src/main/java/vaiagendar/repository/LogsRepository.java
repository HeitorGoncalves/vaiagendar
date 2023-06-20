package vaiagendar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vaiagendar.model.Logs;

@Repository
public interface LogsRepository extends JpaRepository<Logs, Long> {
    
    @Query("FROM Logs l WHERE LOWER(l.usuario) like %:searchTerm% ORDER BY l.id ASC")
    Page<Logs> search(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("FROM Logs l WHERE (:id is null or l.id = :id) AND (:usuario is '' or UPPER(l.usuario) like %:usuario%) " +
    " AND (:cadastroId = null or l.cadastroId = :cadastroId) AND (:cadastro is '' or UPPER(l.cadastro) like %:cadastro%) " +
    " AND (:acao is '' or UPPER(l.acao) like %:acao%)")
    Page<Logs> searchLogs(@Param("id")Long id, @Param("usuario") String usuario,
    @Param("cadastroId") Long cadastroId, @Param("cadastro") String cadastro, 
    @Param("acao") String acao, Pageable pageable);
    
}
