package si.fri.rso.uniborrow.blogs.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.uniborrow.blogs.lib.Blog;
import si.fri.rso.uniborrow.blogs.models.converters.BlogConverter;
import si.fri.rso.uniborrow.blogs.models.entities.BlogEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


import javax.annotation.PostConstruct;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.time.temporal.ChronoUnit;

@RequestScoped
public class BlogBean {

    private final Logger log = Logger.getLogger(BlogBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Blog> getBlogs() {
        TypedQuery<BlogEntity> query =
                em.createNamedQuery("BlogEntity.getAll", BlogEntity.class);
        List<BlogEntity> resultList = query.getResultList();
        return resultList.stream().map(BlogConverter::toDto).collect(Collectors.toList());
    }

    public List<BlogEntity> getBlogsFilter(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, BlogEntity.class, queryParameters);
    }

    public Blog getBlog(Integer id) {
        BlogEntity blogEntity = em.find(BlogEntity.class, id);
        if (blogEntity == null) {
            throw new NotFoundException();
        }
        Blog blog = BlogConverter.toDto(blogEntity);
        return blog;
    }

    public BlogEntity createBlog(BlogEntity blogEntity) {
        try {
            beginTx();
            em.persist(blogEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (blogEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }
        return blogEntity;
    }

    public Blog putBlog(Blog blog, Integer id) {
        BlogEntity blogEntity = em.find(BlogEntity.class, id);
        if (blogEntity == null) {
            return null;
        }

        BlogEntity updatedBlogEntity = BlogConverter.toEntity(blog);
        try {
            beginTx();
            updatedBlogEntity.setId(blogEntity.getId());
            updatedBlogEntity = em.merge(updatedBlogEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.warning(e.getMessage());
            return null;
        }
        return BlogConverter.toDto(updatedBlogEntity);
    }

    public Blog patchBlog(Blog blog, Integer id) {
        BlogEntity blogEntity = em.find(BlogEntity.class, id);
        if (blogEntity == null) {
            return null;
        }

        BlogEntity updatedBlogEntity = BlogConverter.toEntity(blog);
        try {
            beginTx();

            if (updatedBlogEntity.getText() == null) {
                updatedBlogEntity.setText(blogEntity.getText());
            }
            if (updatedBlogEntity.getTitle() == null) {
                updatedBlogEntity.setTitle(blogEntity.getTitle());
            }
            if (updatedBlogEntity.getUserId() == null) {
                updatedBlogEntity.setUserId(blogEntity.getUserId());
            }
            if (updatedBlogEntity.getTimestamp() == null) {
                updatedBlogEntity.setTimestamp(blogEntity.getTimestamp());
            }
            updatedBlogEntity.setId(blogEntity.getId());
            updatedBlogEntity = em.merge(updatedBlogEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.warning(e.getMessage());
            return null;
        }
        return BlogConverter.toDto(updatedBlogEntity);
    }

    public boolean deleteBlog(Integer id) {
        BlogEntity blogEntity = em.find(BlogEntity.class, id);
        if (blogEntity == null) {
            return false;
        }
        try {
            beginTx();
            em.remove(blogEntity);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.warning(e.getMessage());
            return false;
        }
        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}