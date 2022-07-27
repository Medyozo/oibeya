package iObeya.feu.backend.service;

import iObeya.feu.backend.model.Node;
import iObeya.feu.backend.repository.NodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

    @Autowired
    NodeRepository nodeRepository;

    @Override
    public List<Node> getAllNodes() {
        return nodeRepository.getAllNodes();
    }
    /**
     * get All children by id parrent
     *
     * @param id
     */
    @Override
    public List<Node> getChildrenNodes(Long id) {
        return nodeRepository.findByParent(id);
    }

    /**
     * Add new Node
     *
     * @param node
     */
    @Transactional
    public void addNewNode(Node node) {
        final int numberRoots = nodeRepository.getOrphanNodes().size();
        if (node.getRoot_id() == "") {
            if (numberRoots < 1000) {
                node.setHeight(0);
                node.setRoot_id(String.valueOf(nodeRepository.save(node).getId()));
                nodeRepository.save(node);
            } else throw new IllegalStateException("Error create Orphan Node, MAX Roots (1000) Reached");
        } else {
            Node nodeParent = nodeRepository.findById(Long.valueOf(node.getRoot_id())).get();
            System.out.println("Parent found :"+nodeParent.getName());
            if (nodeParent.getHeight() < 10) {
                node.setParent(nodeParent);
                node.setHeight(nodeParent.getHeight() + 1);
                node.setRoot_id(nodeParent.getRoot_id());
            } else throw new IllegalStateException("Error create Child, MAX length (10) Reached");
        }
    }

    /**
     * Update Node name
     *
     * @param nodeId
     * @param name
     * @return
     */
    @Override
    public Node updateNode(long nodeId, String name) {
        Node currNode = nodeRepository.findById(nodeId).get();
        currNode.setName(name);
        nodeRepository.save(currNode);
        return currNode;
    }

    /**
     * Delete Node if doest have children
     *
     * @param id
     */
    @Override
    public void deleteNode(Long id) {
        Node currNode = nodeRepository.findById(id).get();
        if (currNode.isLeaf()){
            nodeRepository.deleteById(id);
        }else {
            throw new IllegalStateException("Category can not be deleted has children");
        }
    }

    /**
     * @param name
     * @return
     */
    @Override
    public List<Node> findNode(String name) {
        return nodeRepository.findByName(name);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Node findNodeById(Long id) {
        return nodeRepository.getReferenceById(id);
    }

    /**
     * @return List of orphan nodes
     */
    @Override
    public List<Node> getOrphanNodes() {
        return nodeRepository.getOrphanNodes();
    }
}
