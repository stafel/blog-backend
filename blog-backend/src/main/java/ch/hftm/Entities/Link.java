package ch.hftm.Entities;

import ch.hftm.API.PostResource;
import jakarta.persistence.Entity;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

/**
 * The link is used for HATEOAS and technically not the same category of entity as the others
 * But for simplicity sake it will be grouped here
 * 
 * TODO: Refactor Models into API facing and DB facing
 */
public class Link {
    String href;
    String rel;
    String type;

    public Link()
    {

    }

    public Link(UriInfo uriInfo, Class targetResource, String linkRelation, String requestMethod)
    {
        this.href = uriInfo.getBaseUri().toString() + UriBuilder.fromResource(targetResource).build("").toString();
        this.rel = linkRelation;
        this.type = requestMethod;
    }

    public Link(String targetUri, String linkRelation, String requestMethod)
    {
        this.href = targetUri;
        this.rel = linkRelation;
        this.type = requestMethod;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
