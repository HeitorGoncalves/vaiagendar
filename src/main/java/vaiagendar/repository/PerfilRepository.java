package vaiagendar.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vaiagendar.model.Perfil;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    
    @Query("FROM Perfil p WHERE (:id is null or p.id = :id) AND (:nome is '' or UPPER(p.nome) like %:nome%) " +
    "AND (:username is '' or UPPER(p.username) like %:nome%)")
    Page<Perfil> searchPerfil(@Param("id") Long id, @Param("nome") String nome, 
    @Param("username") String username, Pageable pageable);

}
