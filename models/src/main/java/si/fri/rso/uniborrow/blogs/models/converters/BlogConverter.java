package si.fri.rso.uniborrow.blogs.models.converters;

import si.fri.rso.uniborrow.blogs.lib.Blog;
import si.fri.rso.uniborrow.blogs.models.entities.BlogEntity;

public class BlogConverter {

    public static Blog toDto(BlogEntity entity) {

        Blog dto = new Blog();
        dto.setBlogId(entity.getId());
        dto.setTimestamp(entity.getTimestamp());
        dto.setText(entity.getText());
        dto.setTitle(entity.getTitle());
        dto.setUserId(entity.getUserId());

        return dto;

    }

    public static BlogEntity toEntity(Blog dto) {

        BlogEntity entity = new BlogEntity();
        entity.setTimestamp(dto.getTimestamp());
        entity.setText(dto.getText());
        entity.setTitle(dto.getTitle());
        entity.setUserId(dto.getUserId());

        return entity;

    }

}
