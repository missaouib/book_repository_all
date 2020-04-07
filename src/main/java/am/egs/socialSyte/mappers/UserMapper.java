//package am.egs.socialSyte.mappers;
//
//import am.egs.socialSyte.model.User;
//import am.egs.socialSyte.payload.UserDto;
//import ma.glasnost.orika.MapperFactory;
//import ma.glasnost.orika.MappingException;
//import org.springframework.stereotype.Component;
//import ma.glasnost.orika.impl.ConfigurableMapper;
//
//@Component
//public class UserMapper extends ConfigurableMapper {
//
//    @Override
//    protected void configure(MapperFactory factory) {
//        String fieldName;
//        try {
//            fieldName = UserDto.class.getDeclaredField("password").getName();
//        } catch (NoSuchFieldException ignored) {
//            throw new MappingException("Ignored field dose not exist.");
//        }
//
//        factory.classMap(UserDto.class, User.class).fieldAToB(fieldName, fieldName).byDefault().register();
//    }
//}
