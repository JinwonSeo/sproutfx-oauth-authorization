package kr.sproutfx.oauth.authorization.common.base;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class BaseController {

    private static final String SELF_LINK_REL_NAME = "self";
    private static final String UPDATE_LINK_REL_NAME = "update";
    private static final String DELETE_LINK_REL_NAME = "delete";

    protected Links getSingleItemLinks(Class<?> controller, UUID id, Link... links) {
        ArrayList<Link> tempLinks = new ArrayList<>();

        if (!Links.of(links).hasLink(SELF_LINK_REL_NAME)) tempLinks.add(linkTo(controller).slash(id).withSelfRel());
        if (!Links.of(links).hasLink(UPDATE_LINK_REL_NAME))
            tempLinks.add(linkTo(controller).slash(id).withRel(UPDATE_LINK_REL_NAME));
        if (!Links.of(links).hasLink(DELETE_LINK_REL_NAME))
            tempLinks.add(linkTo(controller).slash(id).withRel(DELETE_LINK_REL_NAME));

        tempLinks.addAll(Arrays.asList(links));

        return Links.of(tempLinks);
    }

    protected class ObjectEntityModel<T> {
        @JsonUnwrapped
        private EntityModel<T> object;

        public ObjectEntityModel(T object) {
            this.object = EntityModel.of(object);
        }

        public ObjectEntityModel(T object, Links links) {
            this.object = EntityModel.of(object, links);
        }
    }

    protected static class StructuredBody<T> extends BaseResponseBody<T> {
        public StructuredBody(T contents) {
            super(contents);
        }

        public static <T> StructuredBody<T> contents(T contents) {
            return new StructuredBody<>(contents);
        }
    }

}
