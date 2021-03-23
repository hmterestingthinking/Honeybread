package com.whatsub.honeybread.mgmtadmin.domain.category.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.whatsub.honeybread.core.domain.category.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@ApiModel("카테고리 요청")
@Value
public class CategoryRequest {

    @ApiModelProperty(value = "카테고리 명", example = "한식")
    @NotBlank(message = "카테고리 명은 빈 값이 올 수 없습니다.")
    @Length(min = 2, max = 50, message = "카테고리 명은 2자 이상 50자 이하로 구성되어야 합니다.")
    String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CategoryRequest(final String name) {
        this.name = name;
    }

    public Category toEntity() {
        return new Category(name);
    }
}
