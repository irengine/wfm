package com.kwchina.wfm.domain.model.organization;

import java.util.Collection;
import java.util.LinkedHashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="T_NODES")
@DiscriminatorColumn(name="NODE_TYPE")
public abstract class Node {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@OneToMany(targetEntity=Node.class, cascade=CascadeType.ALL, mappedBy="parent")
	private Collection<Node> children = new LinkedHashSet<Node>();
	
	@ManyToOne
	@JoinColumn(name="parentId")
	private Node parent;

	@Column(name="leftId", nullable=false)
	private Long left = 0L;
	
	@Column(name="rightId", nullable=false)
	private Long right = 0L;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Collection<Node> getChildren() {
		return children;
	}

	public void setChildren(Collection<Node> children) {
		this.children = children;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Long getLeft() {
		return left;
	}

	public void setLeft(Long left) {
		this.left = left;
	}

	public Long getRight() {
		return right;
	}

	public void setRight(Long right) {
		this.right = right;
	}
	
	public void addChild(Node node)
	{
		this.children.add(node);
		node.setParent(this);
	}
	
	public void removeChild(Node node)
	{
		this.children.remove(node);
		node.setParent(null);
	}
}
