package br.com.wppatend.flow.entities;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="flownodeend")
@PrimaryKeyJoinColumn(name = "nodeid")
public class FlowNodeEnd extends FlowNode {

}
