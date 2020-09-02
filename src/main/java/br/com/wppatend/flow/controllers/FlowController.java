package br.com.wppatend.flow.controllers;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowNodeAction;
import br.com.wppatend.flow.services.FlowService;

@Controller
@RequestMapping("fluxos")
public class FlowController {
	
	private static final Logger logger = LoggerFactory.getLogger(FlowController.class);
	
	@Autowired
	private FlowService flowService;
	
	@GetMapping
    public String index() {
        return "redirect:/fluxos/1";
    }
	
	@GetMapping(value = "/{pageNumber}")
    public String list(@PathVariable Integer pageNumber, Model model) {
		logger.debug("Entrou na lista de finalizações");
        Page<Flow> page = flowService.getFlowList(pageNumber);

        int current = page.getNumber() + 1;
        int begin = Math.max(1, current - 5);
        int end = Math.min(begin + 10, page.getTotalPages());

        model.addAttribute("list", page);
        model.addAttribute("beginIndex", begin);
        model.addAttribute("endIndex", end);
        model.addAttribute("currentIndex", current);

        return "fluxos/list";

    }
	
	@GetMapping("/add/fluxo")
    public String add(Model model) {
		logger.debug("Entrou em add de finalizações");
    	Flow f = new Flow();
        model.addAttribute("fluxo", f);
        return "fluxos/form";

    }
	
	@GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
		logger.debug("Entrou edit de finalizações");
    	Optional<Flow> f = flowService.findFlowById(id);
        model.addAttribute("fluxo", f.get());
        return "fluxos/form";

    }
	
	@GetMapping("/loadflow/{id}")
	public String loadFlow(@PathVariable Long id, Model model) {
		Optional<Flow> f = flowService.findFlowById(id);
        model.addAttribute("flow", f.get());
		return "fluxos/nodes";
	}
	
	/*
	 * Load to add an action node
	 */
	@GetMapping("/nodes/add/actionNode/{flowId}")
	public String addActionNodeOnFlow(@PathVariable Long flowId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeAction nodeAction = new FlowNodeAction();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeAction);
		return "fluxos/nodeactionform";
	}
	
	/*
	 * Save an action node
	 */
	@PostMapping("/nodes/save/actionNode")
	public String saveActionNodeOnFlow(Long flowId, Model model, FlowNodeAction nodeAtion, final RedirectAttributes ra) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		Flow flow = f.get();
		if(flow.getNodes() == null) {
			flow.setNodes(new ArrayList<>());
		}
		flow.getNodes().add(nodeAtion);
		flowService.saveFlow(flow);
		return "redirect:/fluxos/loadflow/" + flowId;
	}
	
	/*
	 * Delete a node
	 */
	@GetMapping("nodes/delete/{flowId}/{nodeId}")
	public String deleteNodeFromFlow(@PathVariable(name = "flowId") Long flowId, @PathVariable(name = "nodeId") Long nodeId ) {
		Flow f = flowService.findFlowById(flowId).get();
		f.getNodes().remove(flowService.findFlowNodeById(nodeId).get());
		flowService.saveFlow(f);
		return "redirect:/fluxos/loadflow/" + flowId;
	}
	
	@PostMapping(value = "/save")
    public String save(Flow flow, final RedirectAttributes ra) {
		logger.debug("Entrou em save de fluxos");
    	String msg = "Fluxo salva com sucesso.";
		@SuppressWarnings("unused")
		Flow save = flowService.saveFlow(flow);
        ra.addFlashAttribute("successFlash", msg);
        return "redirect:/fluxos";

    }
	
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		flowService.deleteFlow(id);
		return "redirect:/finalizacoes";
	}

}
