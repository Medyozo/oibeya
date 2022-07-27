package iObeya.feu.backend.service;


import iObeya.feu.backend.model.Node;

import java.util.List;

public interface NodeService {

    List<Node> getAllNodes();

    List<Node> getChildrenNodes(Long id);

    void addNewNode(Node node);

    Node updateNode(final long nodeId, final String name);

     void deleteNode(final Long id);

    List<Node> findNode(final String name);

    Node findNodeById(final Long id);
    List<Node> getOrphanNodes();
}
