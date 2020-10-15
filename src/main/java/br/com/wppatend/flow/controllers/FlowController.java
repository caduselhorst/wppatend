package br.com.wppatend.flow.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.websocket.server.PathParam;

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

import br.com.wppatend.entities.Departamento;
import br.com.wppatend.flow.entities.Flow;
import br.com.wppatend.flow.entities.FlowNode;
import br.com.wppatend.flow.entities.FlowNodeAction;
import br.com.wppatend.flow.entities.FlowNodeCollect;
import br.com.wppatend.flow.entities.FlowNodeDecision;
import br.com.wppatend.flow.entities.FlowNodeEnqueue;
import br.com.wppatend.flow.entities.FlowNodeMenu;
import br.com.wppatend.flow.entities.FlowNodeMenuOption;
import br.com.wppatend.flow.entities.FlowParameter;
import br.com.wppatend.flow.services.FlowService;
import br.com.wppatend.services.DepartamentoService;

@Controller
@RequestMapping("fluxos")
public class FlowController {
	
	private static final Logger logger = LoggerFactory.getLogger(FlowController.class);
	
	@Autowired
	private FlowService flowService;
	
	@Autowired
	private DepartamentoService departamentoService;
	
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
	 * Load to edit an action node
	 */
	@GetMapping("/nodes/edit/actionNode/{flowId}/{nodeId}")
	public String editActionNodeOnFlow(@PathVariable(name = "flowId") Long flowId, 
			@PathVariable(name = "nodeId") Long nodeId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeAction nodeAction = (FlowNodeAction) flowService.findFlowNodeById(nodeId).get();
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
		if(nodeAtion.getId() != null) {
			flowService.saveFlowNode(nodeAtion);
		} else {
			flow.getNodes().add(nodeAtion);
			flowService.saveFlow(flow);
		}
		
		return "redirect:/fluxos/loadflow/" + flowId;
	}
	
	
	/*
	 * Load to add a menu node
	 */
	@GetMapping("/nodes/add/menuNode/{flowId}")
	public String addMenuNodeOnFlow(@PathVariable Long flowId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeMenu nodeMenu = new FlowNodeMenu();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeMenu);
		return "fluxos/nodemenuform";
	}
	
	/*
	 * Load to edit a menu node
	 */
	@GetMapping("/nodes/edit/menuNode/{flowId}/{nodeId}")
	public String editMenuNodeOnFlow(@PathVariable(name = "flowId") Long flowId, 
			@PathVariable(name = "nodeId") Long nodeId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeMenu nodeAction = (FlowNodeMenu) flowService.findFlowNodeById(nodeId).get();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeAction);
		return "fluxos/nodemenuform";
	}
	
	/*
	 * Save a menu node
	 */
	@PostMapping("/nodes/save/menuNode")
	public String saveMenuNodeOnFlow(Long flowId, Model model, FlowNodeMenu nodeMenu, final RedirectAttributes ra) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		Flow flow = f.get();
		if(flow.getNodes() == null) {
			flow.setNodes(new ArrayList<>());
		}
		if(nodeMenu.getId() != null) {
			flowService.saveFlowNode(nodeMenu);
		} else {
			flow.getNodes().add(nodeMenu);
			flowService.saveFlow(flow);
		}
		
		return "redirect:/fluxos/loadflow/" + flowId;
	}
	
	/*
	 * Load to add a menu option into a node menu
	 */
	@GetMapping("/nodes/nodemenu/options/add/{flowId}/{flowNodeId}")
	public String addOptionIntoNodeMenu(
			@PathVariable(name = "flowId") Long flowId,
			@PathVariable(name = "flowNodeId") Long flowNodeId, Model model) {
		Optional<Flow> optFlow = flowService.findFlowById(flowId);
		Optional<FlowNode> optFlowNode = flowService.findFlowNodeById(flowNodeId);
		FlowNodeMenu nodeMenu = (FlowNodeMenu) optFlowNode.get();
		model.addAttribute("flow", optFlow.get());
		model.addAttribute("flowNode", nodeMenu);
		model.addAttribute("option", new FlowNodeMenuOption());
		
		return "fluxos/formnodemenuoption";
		
	}
	
	/*
	 * Load to edit a menu option
	 */
	@GetMapping("/nodes/nodemenu/options/edit/{flowId}/{flowNodeId}/{flowNodeOptionId}")
	public String editMenuOption(
			@PathVariable(name = "flowId") Long flowId,
			@PathVariable(name = "flowNodeId") Long flowNodeId, 
			@PathVariable(name = "flowNodeOptionId") Long flowNodeOptionId, Model model) {
		
		Optional<Flow> optFlow = flowService.findFlowById(flowId);
		Optional<FlowNode> optFlowNode = flowService.findFlowNodeById(flowNodeId);
		FlowNodeMenu nodeMenu = (FlowNodeMenu) optFlowNode.get();
		Optional<FlowNodeMenuOption> optMenuOption = flowService.findFlowNodeMenuOptionById(flowNodeOptionId);
		model.addAttribute("flow", optFlow.get());
		model.addAttribute("flowNode", nodeMenu);
		model.addAttribute("option", optMenuOption.get());
		
		return "fluxos/formnodemenuoption";
	}
	
	/*
	 * Save a menu option into a menu node
	 */
	@PostMapping("/nodes/save/menuNode/option")
	public String saveMenuOptionIntoMenuNode(Long flowId, Long flowNodeId, Model model, 
			FlowNodeMenuOption menuOption, final RedirectAttributes ra) {
		Optional<FlowNode> optFlow = flowService.findFlowNodeById(flowNodeId);
		FlowNodeMenu nodeMenu = (FlowNodeMenu) optFlow.get();
		if(nodeMenu.getOptions() == null) {
			nodeMenu.setOptions(new ArrayList<>());
		}
		if(menuOption.getId() != null) {
			for(FlowNodeMenuOption opt : nodeMenu.getOptions()) {
				if(opt.getId().equals(menuOption.getId())) {
					opt.setPattern(menuOption.getPattern());
					opt.setNextNode(menuOption.getNextNode());
				}
			}
			flowService.saveFlowNode(nodeMenu);
		} else {
			nodeMenu.getOptions().add(menuOption);
			flowService.saveFlowNode(nodeMenu);
		}
		
		return "redirect:/fluxos/nodes/nodemenu/options/" + flowId + "/" + flowNodeId;
		
	}
	
	/*
	 * Redirect to menu options
	 */
	@GetMapping("/nodes/nodemenu/options/{flowId}/{nodeId}")
	public String loadMenuOptions(@PathVariable(name = "flowId") Long flowId,
			@PathVariable(name = "nodeId") Long nodeId,
			Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		Flow flow = f.get();
		FlowNodeMenu nodeAction = (FlowNodeMenu) flowService.findFlowNodeById(nodeId).get();
        model.addAttribute("flow", flow);
        model.addAttribute("flowNode", nodeAction);
        return "fluxos/nodemenuoptionlist";
	}
	
	/*
	 * Load to add a collector node
	 */
	@GetMapping("/nodes/add/collectNode/{flowId}")
	public String addCollectorNodeOnFlow(@PathVariable Long flowId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeCollect nodeCollect = new FlowNodeCollect();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeCollect);
		return "fluxos/nodecollectform";
	}
	
	/*
	 * Load to edit a collector node
	 */
	@GetMapping("/nodes/edit/collectNode/{flowId}/{nodeId}")
	public String editCollectorNodeOnFlow(@PathVariable(name = "flowId") Long flowId, 
			@PathVariable(name = "nodeId") Long nodeId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeCollect nodeAction = (FlowNodeCollect) flowService.findFlowNodeById(nodeId).get();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeAction);
		return "fluxos/nodecollectform";
	}
	
	/*
	 * Save a collector node
	 */
	@PostMapping("/nodes/save/collectNode")
	public String saveCollectorNodeOnFlow(Long flowId, Model model, FlowNodeCollect nodeCollect, final RedirectAttributes ra) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		Flow flow = f.get();
		if(flow.getNodes() == null) {
			flow.setNodes(new ArrayList<>());
		}
		if(nodeCollect.getId() != null) {
			flowService.saveFlowNode(nodeCollect);
		} else {
			flow.getNodes().add(nodeCollect);
			flowService.saveFlow(flow);
		}
		
		return "redirect:/fluxos/loadflow/" + flowId;
	}
	
	/*
	 * Load to add a decision node
	 */
	@GetMapping("/nodes/add/decisionNode/{flowId}")
	public String addDecisionNodeOnFlow(@PathVariable Long flowId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeDecision nodeDecision = new FlowNodeDecision();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeDecision);
		return "fluxos/nodedecisionform";
	}
	
	/*
	 * Load to edit a decision node
	 */
	@GetMapping("/nodes/edit/decisionNode/{flowId}/{nodeId}")
	public String editDecisionNodeOnFlow(@PathVariable(name = "flowId") Long flowId, 
			@PathVariable(name = "nodeId") Long nodeId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeDecision nodeAction = (FlowNodeDecision) flowService.findFlowNodeById(nodeId).get();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeAction);
		return "fluxos/nodedecisionform";
	}
	
	/*
	 * Save a decision node
	 */
	@PostMapping("/nodes/save/decisionNode")
	public String saveDecisionNodeOnFlow(Long flowId, Model model, FlowNodeDecision nodeDecision, final RedirectAttributes ra) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		Flow flow = f.get();
		if(flow.getNodes() == null) {
			flow.setNodes(new ArrayList<>());
		}
		if(nodeDecision.getId() != null) {
			flowService.saveFlowNode(nodeDecision);
		} else {
			flow.getNodes().add(nodeDecision);
			flowService.saveFlow(flow);
		}
		
		return "redirect:/fluxos/loadflow/" + flowId;
	}
	
	/*
	 * Load to add an enqueue node
	 */
	@GetMapping("/nodes/add/enqueueNode/{flowId}")
	public String addEnqueueNodeOnFlow(@PathVariable Long flowId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeEnqueue nodeEnqueue = new FlowNodeEnqueue();
		List<Departamento> departamentos = departamentoService.getList();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeEnqueue);
        model.addAttribute("departamentos", departamentos);
		return "fluxos/nodeenqueueform";
	}
	
	/*
	 * Load to edit an enqueue node
	 */
	@GetMapping("/nodes/edit/enqueueNode/{flowId}/{nodeId}")
	public String editEnqueueNodeOnFlow(@PathVariable(name = "flowId") Long flowId, 
			@PathVariable(name = "nodeId") Long nodeId, Model model) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		FlowNodeEnqueue nodeAction = (FlowNodeEnqueue) flowService.findFlowNodeById(nodeId).get();
		List<Departamento> departamentos = departamentoService.getList();
        model.addAttribute("flow", f.get());
        model.addAttribute("flowNode", nodeAction);
        model.addAttribute("departamentos", departamentos);
		return "fluxos/nodeenqueueform";
	}
	
	/*
	 * Save an enqueue node
	 */
	@PostMapping("/nodes/save/enqueueNode")
	public String saveEnqueueNodeOnFlow(Long flowId, Model model, FlowNodeEnqueue nodeEnqueue, final RedirectAttributes ra) {
		Optional<Flow> f = flowService.findFlowById(flowId);
		Flow flow = f.get();
		if(flow.getNodes() == null) {
			flow.setNodes(new ArrayList<>());
		}
		if(nodeEnqueue.getId() != null) {
			flowService.saveFlowNode(nodeEnqueue);
		} else {
			flow.getNodes().add(nodeEnqueue);
			flowService.saveFlow(flow);
		}
		
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
	
	
	/*
	 * Load flow parameters
	 */
	@GetMapping("/parameters/{flowId}")
	public String loadFlowParameters(@PathVariable Long flowId, Model model) {
		
		Optional<Flow> optFlow = flowService.findFlowById(flowId);
		model.addAttribute("list", optFlow.get().getParameters());
		model.addAttribute("flow", optFlow.get());
		return "fluxos/parameterslist";
		
	}
	
	/*
	 * Load to add a new parameter into a flow
	 */
	@GetMapping("/parameters/{flowId}/add")
	public String addFlowParameter(@PathVariable Long flowId, Model model) {
		Optional<Flow> optFlow = flowService.findFlowById(flowId);
		FlowParameter parameter = new FlowParameter();
		model.addAttribute("flow", optFlow.get());
		model.addAttribute("parameter", parameter);
		
		return "fluxos/formparameter";
	}
	
	/*
	 * Load to edit a new parameter into a flow
	 */
	@GetMapping("/parameters/{flowId}/edit/{flowParameterId}")
	public String editFlowParameter(@PathVariable(name = "flowId") Long flowId, 
			@PathVariable(name="flowParameterId") Long flowParameterId, Model model) {
		Optional<Flow> optFlow = flowService.findFlowById(flowId);
		Optional<FlowParameter> parameter = flowService.findFlowParameterById(flowParameterId);
		model.addAttribute("flow", optFlow.get());
		model.addAttribute("parameter", parameter.get());
		
		return "fluxos/formparameter";
	}
	
	
	/*
	 * Save parameter into a flow
	 */
	@PostMapping("/parameters/save")
	public String saveParameter(Long flowId, Model model, FlowParameter parameter) {
		Flow flow = flowService.findFlowById(flowId).get();
		
		if(flow.getParameters() == null) {
			flow.setParameters(new ArrayList<>());
		}
		
		if(parameter.getId() == null) {
			flow.getParameters().add(parameter);
			flowService.saveFlow(flow);
		} else {
			flowService.saveFlowParameter(parameter);
		}
		
		return "redirect:/fluxos/parameters/" + flowId;
	}
	
	/*
	 * Delete a flow parameter
	 */
	@GetMapping("/parameters/{flowId}/delete/{parameterId}")
	public String deleteParameter(@PathVariable(name="flowId") Long flowId, 
			@PathVariable(name="parameterId") Long parameterId) {
		Flow flow = flowService.findFlowById(flowId).get();
		flow.getParameters().remove(flowService.findFlowParameterById(parameterId).get());
		flowService.saveFlow(flow);
		return "redirect:/fluxos/parameters/" + flowId;
	}

}
