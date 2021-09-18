package br.com.wppatend.flow.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wppatend.flow.entities.FlowNodeMenu;
import br.com.wppatend.flow.entities.FlowNodeMenuOption;

public interface FlowNodeMenuOptionRepository extends JpaRepository<FlowNodeMenuOption, Long> {
	
	public List<FlowNodeMenuOption> findByMenuNode(FlowNodeMenu menu);

}
