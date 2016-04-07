package fr.univtln.mgajovski482.Rest;

import fr.univtln.mgajovski482.JPA.Exception.DAOException;
import fr.univtln.mgajovski482.JPA.IdentifiableEntity;
import fr.univtln.mgajovski482.Rest.utils.Page;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;


    @Produces({MediaType.APPLICATION_JSON})

    public abstract class AbstractResourceRest<PK extends Serializable,
        T extends IdentifiableEntity<PK>> implements IResourceRest<PK, T> {

        public static final Logger LOGGER = LoggerFactory.getLogger(AbstractResourceRest.class);
        private int maxPageSize = 500;

        @Override
        @GET
        @Path("{id}")
        public T find(@PathParam("id") PK id) throws DAOException {
            T result = getGenericManager().find(id);
            getGenericManager().done();
            return result;
        }

        //todo: use CDI (hk2) instead ?
        public abstract IRestManager<PK, T> getGenericManager();

        @Override
        @DELETE
        @Path("{id}")
        public void delete(@PathParam("id") PK id) throws DAOException {
            System.out.println("DELETE OK");
            getGenericManager().delete(id);
            getGenericManager().done();
        }

        @Override
        public long size() throws DAOException {
            long result = getGenericManager().getSize();
            getGenericManager().done();
            return result;
        }

        @Override
        @HEAD
        public Response metadata() throws DAOException {
            return Response.status(Response.Status.OK)
                    .header("X-Total-Item-Count", size())
                    .header("Access-Control-Expose-Headers", "X-Total-Item-Count, X-Total-Page-Count").build();
        }

        @Override
        @GET
        public Response findAll(@QueryParam("reverse") @DefaultValue("false") boolean reverse,
                                @QueryParam("pagenumber") @DefaultValue("-1") int pageNumber,
                                @QueryParam("perpage") @DefaultValue("-1") int perPage,
                                @QueryParam("limit") @DefaultValue("-1") int limit,
                                @Context UriInfo uriInfo) throws DAOException {
            System.out.println("pageNumber = " + pageNumber);
            Response response;
            if (pageNumber == -1)
                response=listToResponse(getGenericManager().findAll(reverse, limit), uriInfo)
                        .build();
            else {
                if (perPage < 0) perPage = getMaxPageSize();
                LOGGER.info("pre findall");
                Page result = getGenericManager().findAllByPage(reverse, pageNumber, perPage, limit);
                LOGGER.info("post findall");
                response = pageToResponse(result, uriInfo)
                        .build();
                LOGGER.info("post response built");
            }
            getGenericManager().done();
            return response;
        }

        protected Response.ResponseBuilder listToResponse(List list, UriInfo uriInfo) throws DAOException {
           //I try with : GenericEntity<List<T>> genericEntity = new GenericEntity<List<T>>(list){};

           return Response.status(Response.Status.OK).entity(list);

        }

        protected Response.ResponseBuilder pageToResponse(Page page, UriInfo uriInfo) throws DAOException {
            List<String> links = new ArrayList<>();
            if (page.PAGE_NUMBER > 0) {
                links.add("<" + uriInfo.getAbsolutePath() + "?pagenumber=0" + "&perpage=" + page.PAGE_SIZE + ">; rel=\"first\"");
                links.add("<" + uriInfo.getAbsolutePath() + "?pagenumber=" + (page.PAGE_NUMBER + 1) + "&perpage=" + page.PAGE_SIZE + ">; rel=\"previous\"");
            }
            if (page.PAGE_NUMBER < page.TOTAL_PAGES - 1) {
                links.add("<" + uriInfo.getAbsolutePath() + "?pagenumber=" + (page.PAGE_NUMBER + 1) + "&perpage=" + page.PAGE_SIZE + ">; rel=\"next\"");
                links.add("<" + uriInfo.getAbsolutePath() + "?pagenumber=" + (page.TOTAL_PAGES - 1) + "&perpage=" + page.PAGE_SIZE + ">; rel=\"last\"");
            }

            return Response.status(Response.Status.OK).entity(page.content)
                    .header("Link", links.stream().collect(Collectors.joining(", ")))
                    .header("X-Total-Item-Count", page.TOTAL_ITEMS)
                    .header("X-Total-Page-Count", page.TOTAL_PAGES)
                    .header("Access-Control-Expose-Headers", "X-Total-Item-Count, X-Total-Page-Count");
        }

        @Override
        @GET
        @Path("ids")
        public List<PK> getIds(@QueryParam("reverse") @DefaultValue("false") boolean reverse) throws DAOException {
            List<PK> result = getGenericManager().getIds(reverse);
            getGenericManager().done();
            return result;
        }

        @Override
        @PUT
        @Consumes({"application/json"})
        public T create(T item) throws DAOException {
            T result = getGenericManager().create(item);
            getGenericManager().done();
            return result;
        }

        @Override
        @PUT
        @Path("collection")
        @Consumes({"application/json"})
        public int create(List<T> list) throws DAOException {
            System.out.println(list);
            System.out.println("LOOOL");
            int result = getGenericManager().create(list);
            getGenericManager().done();
            return result;
        }

        @Override
        @POST
        @Consumes({"application/json"})
        public T update(T t) throws DAOException {
            T result = getGenericManager().update(t);
            getGenericManager().done();
            return result;
        }

        @Override
        @DELETE
        public int delete() throws DAOException {
            System.out.println("BOOOM ALL DELETED !");
            int result = getGenericManager().deleteAll();
            getGenericManager().done();
            return result;
        }

        @Override
        @GET
        @Path("maxpagesize")
        public int getMaxPageSize() {
            return maxPageSize;
        }

        @Override
        @PUT
        @Path("maxpagesize")
        public void setMaxPageSize(int pageSize) {
            this.maxPageSize = pageSize;
        }
    }

