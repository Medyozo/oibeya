package iObeya.feu.backend.repository;

import iObeya.feu.backend.model.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<Node, Long> {
    List<Node> getAllNodes();

    @Query("SELECT p FROM Node p WHERE p.parent.id = ?1")
    List<Node> findByParent(Long id);


    @Query("SELECT p FROM Node p WHERE p.parent.id = NULL")
    List<Node> getOrphanNodes();

    @Query("SELECT p FROM Node p WHERE p.name like %?1%")
    List<Node> findByName(String name);

}
