package com.epam.arango_practice.model;

import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

/**
 * Represent the connection between family-members
 */
public class RelationEdge {
	@ApiModelProperty(value = "Arango system property: is a computed property from '_key' and 'collection name'", example = "relations/97615456", notes = "generated value")
	@DocumentField(Type.ID)
	private String id;

	@ApiModelProperty(value = "Arango system property:  identifies edge within a collection", example = "97615456", notes = "generated value")
	@DocumentField(Type.KEY)
	private String key;

	@ApiModelProperty(value = "Arango system property: revision ID managed by the system", example = "_YGKd8S--G", notes = "generated value")
	@DocumentField(Type.REV)
	private String revision;

	@NotNull
	@ApiModelProperty(value = "Start Vertex", required = true, example = "family/167943")
	@DocumentField(Type.FROM)
	private String from;

	@NotNull
	@ApiModelProperty(value = "End Vertex", required = true, example = "family/167944")
	@DocumentField(Type.TO)
	private String to;

	@NotNull
	@ApiModelProperty(value = "Relation between nodes", required = true, example = "marriage", allowableValues = "dad,mom,marriage")
	String relation;

	public RelationEdge() {
	}

	public RelationEdge(final String from, final String to, final String relation) {
		this.from = from;
		this.to = to;
		this.relation = relation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
}
