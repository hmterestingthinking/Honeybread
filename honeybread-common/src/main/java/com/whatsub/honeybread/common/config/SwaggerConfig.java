package com.whatsub.honeybread.common.config;

import com.whatsub.honeybread.common.support.HoneyBreadSwaggerTags;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .directModelSubstitute(LocalDateTime.class, String.class)
                .tags(HoneyBreadSwaggerTags.defaultTag(), getTags().toArray(Tag[]::new))
                .apiInfo(
                    new ApiInfo(
                        "HoneyBread Api Doc",
                        "허니브레드 API 문서",
                        "1.0.0",
                        "",
                        new Contact("@ces518", "https://github.com/ces518", "pupupee9@gmail.com"),
                        "",
                        "",
                        Collections.emptyList()
                    )
                );
    }

    @Bean
    public UiConfiguration uiConfig() {
        return UiConfigurationBuilder.builder()
            .deepLinking(true)
            .displayOperationId(false)
            .defaultModelsExpandDepth(1)
            .defaultModelExpandDepth(1)
            .defaultModelRendering(ModelRendering.EXAMPLE)
            .displayRequestDuration(false)
            .docExpansion(DocExpansion.NONE)
            .filter(false)
            .maxDisplayedTags(null)
            .operationsSorter(OperationsSorter.ALPHA)
            .showExtensions(false)
            .showCommonExtensions(false)
            .tagsSorter(TagsSorter.ALPHA)
            .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
            .validatorUrl(null)
            .build();
    }

    public Field[] tags() {
        return new Field[0];
    }

    private List<Tag> getTags() {
        return Arrays.stream(tags())
            .filter(field -> Modifier.isStatic(field.getModifiers()))
            .map(field -> {
                try {
                    return field.get(null).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            })
            .filter(value -> !value.equals(HoneyBreadSwaggerTags.defaultTag().getName()))
            .map(value -> new Tag(value, "", value.chars().filter(Character::isDigit).sum()))
            .collect(Collectors.toList());
    }
}
