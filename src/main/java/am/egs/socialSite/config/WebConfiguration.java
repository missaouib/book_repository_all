package am.egs.socialSite.config;


import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@EnableWebMvc
@Configuration
//@ComponentScan(basePackages = {"net.atos.bfi.tpm.retail.gui.*", "net.atos.bfi.tpm.retail.rs.dto.*", "net.atos.bfi.tpm.retail.gui.session"})
public class WebConfiguration extends WebMvcConfigurerAdapter {
        //implements ApplicationContextAware {
    private ApplicationContext applicationContext;
//    @Autowired
//    private Environment env;
//
//    /**
//     * Configure ViewResolvers to deliver preferred views.
//     */
//    @Override
//    public void configureViewResolvers(final ViewResolverRegistry registry) {
//
//        final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(this.getTemplateEngine());
//        registry.viewResolver(viewResolver);
//    }
//
//    @Override
//    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
//        this.applicationContext = applicationContext;
//    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");

    }

//    @Bean(name = "templateEngine")
//    public TemplateEngine getTemplateEngine() {
//        final SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
//        springTemplateEngine.setEnableSpringELCompiler(true);
//        springTemplateEngine.setTemplateResolver(this.getTemplateResolver());
//        springTemplateEngine.addDialect(new LayoutDialect());
//        springTemplateEngine.addDialect(new SpringSecurityDialect());
//
//        return springTemplateEngine;
//    }

    @Bean(name = "templateResolver")
    public ITemplateResolver getTemplateResolver() {
        final SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(this.applicationContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCacheable(false);
        templateResolver.setCharacterEncoding("UTF-8");

        return templateResolver;
    }

//    @Override
//    public void configurePathMatch(final PathMatchConfigurer configurer) {
//        super.configurePathMatch(configurer);
//        configurer.setUseRegisteredSuffixPatternMatch(false);
//        configurer.setUseSuffixPatternMatch(false);
//    }
//
//    @Bean
//    public MessageSource messageSource() {
//
//        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.addBasenames("messages");
//        return messageSource;
//
//    }
//
//    @Bean
//    public CookieLocaleResolver localeResolver() {
//        final CookieLocaleResolver localeResolver = new CookieLocaleResolver();
//        localeResolver.setDefaultLocale(Locale.FRENCH);
//        localeResolver.setCookieName("my-locale-cookie");
//        localeResolver.setCookieMaxAge(3600);
//        return localeResolver;
//    }
//
//    @Bean
//    public LocaleChangeInterceptor localeInterceptor() {
//        final LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
//        interceptor.setParamName("lang");
//        return interceptor;
//    }
//
//    @Override
//    public void addInterceptors(final InterceptorRegistry registry) {
//        registry.addInterceptor(this.localeInterceptor());
//    }
//
//    @Bean
//    public HttpSessionEventPublisher httpSessionEventPublisher() {
//        return new HttpSessionEventPublisher();
//    }
//
//    @Bean
//    public SessionListener sessionListener() {
//        return new SessionListener();
//    }
}
