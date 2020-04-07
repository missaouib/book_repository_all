package am.egs.socialSite.mappers;

import am.egs.socialSite.model.Book;
import am.egs.socialSite.payload.BookDto;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingException;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

@Component
public class BookMapper extends ConfigurableMapper {
    @Override
    protected void configure(MapperFactory factory) {
        String fieldName;
        try {
            fieldName = BookDto.class.getDeclaredField("author").getName();
        } catch (NoSuchFieldException ignored) {
            throw new MappingException("Ignored field dose not exist.");
        }

        factory.classMap(BookDto.class, Book.class).fieldAToB(fieldName, fieldName).byDefault().register();
    }

}

