package com.epam.arangoPractice.model;

import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;


public class RelationEdge {

    @DocumentField(Type.ID)
    private String id;

    @DocumentField(Type.KEY)
    private String key;

    @DocumentField(Type.REV)
    private String revision;

    @DocumentField(Type.FROM)
    private String from;

    @DocumentField(Type.TO)
    private String to;

    String relation;

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
