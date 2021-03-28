package com.whatsub.honeybread.core.domain.menu.history;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;

@Data
@Document(collection = "menu_history")
@Builder
public class MenuHistory {

    @Id
    private String id;

    private Long menuId;

    private Long storeId;

    private String name;

    private String imageUrl;

    private String description;

    private BigDecimal price;

    private boolean isMain;

    private boolean isBest;

    private Long menuGroupId;

    private Long categoryId;

    private List<OptionGroup> optionGroups = List.of();

    @Data
    @Builder
    static class OptionGroup {
        private Long optionGroupId;
        private String name;
        private Type type;
        private int minimumSelectCount;
        private int maximumSelectCount;
        private List<Option> options = List.of();

        enum Type {
            BASIC,
            OPTIONAL,
            ;
        }
    }

    @Data
    @Builder
    static class Option {
        private Long optionId;
        private String name;
        private BigDecimal price;
    }

}
