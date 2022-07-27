package iObeya.feu.backend.controller;

import iObeya.feu.backend.model.Node;
import iObeya.feu.backend.repository.NodeRepository;
import iObeya.feu.backend.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;

import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.*;
@Controller
@RequestMapping(value={"/nodes","/"})

public class NodeController {

    @Autowired
    private NodeRepository repo;

    @Autowired
    private NodeService nodeService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    public String home(Model model) {
        List<Node>  allNodes =  nodeService.getOrphanNodes();
        model.addAttribute("categories",allNodes);
        model.addAttribute("node",new Node());
        return "home";
    }



    @GetMapping("{id}")
    public String getChildrenNodes(@PathVariable final long  id,Model model) {
        Date startTime = new Date();
        logger.info("Get all children nodes of a given node " + id);
        Date endTime = new Date();
        logger.info(" Execution time " + (startTime.getTime() - endTime.getTime()) / 1000);
        List<Node>  childrenNodes = nodeService.getChildrenNodes(id);
        model.addAttribute("categories",childrenNodes);
        model.addAttribute("node",new Node());
        return "home" ;
    }

    @PostMapping
    public String  registerNewNode(@ModelAttribute Node node, Model model){
        final String parent_id = node.getRoot_id();
        if(node.getId()!=0){
            // node exist update only
            System.out.println("UPDATING "+node.getId());
            nodeService.updateNode(node.getId(),node.getName());
        }else{
            System.out.println("CREATING NEW NODE "+node.getId());
            logger.info("register New Node :"+ node.getName());
            nodeService.addNewNode(node);
        }
        return "redirect:/nodes/"+parent_id;
    }


    @GetMapping(value={"/update/{id}","/update/{id}/{name}"})
    public String updateName(@PathVariable("id") long id,Model model,@PathVariable(required = false) Optional<String> optionaName) {
        logger.info("update node :"+ id);
        if(optionaName.isPresent()){
            final String newName = optionaName.get();
            nodeService.updateNode(id,newName);
        }
        Node node = nodeService.findNodeById(id);
        if(node!=null){
            model.addAttribute("node",node);
            model.addAttribute("categories",node.getChilds());
        }
        return "home";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView deleteNode(@PathVariable("id") Long id){
        logger.info("Delete node with id " + id);
        Node node = nodeService.findNodeById(id);
        if(node!=null){
            nodeService.deleteNode(id);
        }
        return new ModelAndView("redirect:/nodes/");
    }

    @PostMapping("/search")
    public String findByName(@ModelAttribute Node node,Model model){
        List<Node> nodeList = nodeService.findNode(node.getName());
        model.addAttribute("categories",nodeList);
        model.addAttribute("node",new Node());
        return "home" ;
    }
}
