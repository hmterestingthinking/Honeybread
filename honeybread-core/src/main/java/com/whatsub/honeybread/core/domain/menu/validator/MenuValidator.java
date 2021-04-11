package com.whatsub.honeybread.core.domain.menu.validator;

import com.whatsub.honeybread.core.domain.category.CategoryRepository;
import com.whatsub.honeybread.core.domain.menu.Menu;
import com.whatsub.honeybread.core.domain.menu.MenuOptionGroup;
import com.whatsub.honeybread.core.domain.menu.repository.MenuGroupRepository;
import com.whatsub.honeybread.core.domain.model.Money;
import com.whatsub.honeybread.core.infra.errors.ErrorCode;
import com.whatsub.honeybread.core.infra.exception.HoneyBreadException;
import com.whatsub.honeybread.core.infra.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MenuValidator {
    private final MenuGroupRepository groupRepository;
    private final CategoryRepository categoryRepository;

    public void validate(final Menu menu) {
        final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(menu, Menu.class.getSimpleName());

        validateMenu(menu);
        validateOptionGroup(menu, menu.getOptionGroups(), bindingResult);

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
    }

    private void validateMenu(Menu menu) {
        if (!categoryRepository.existsById(menu.getCategoryId())) {
            throw new HoneyBreadException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        if (!groupRepository.existsById(menu.getMenuGroupId())) {
            throw new HoneyBreadException(ErrorCode.MENU_GROUP_NOT_FOUND);
        }
    }

    /**
     * 기본 판매가가 0원이라면, 반드시 1개가 존재해야 한다.
     * <p>
     * 옵션 그룹의 최소 선택 개수는 1개 이상이어야 한다.
     * <p>
     * 옵션 그룹의 최대 선택 개수는 최소 선택개수보다 작을 수 없다.
     */
    private void validateOptionGroup(final Menu menu, final List<MenuOptionGroup> optionGroups, final Errors errors) {
        if (menu.getPrice().equals(Money.ZERO) && !existsBasicOption(optionGroups)) {
            errors.reject(
                "basic_option_group.must_be_exist",
                new Object[]{"기본 옵션 그룹"},
                "기본 판매가가 0원이라면, 기본 옵션 그룹은 반드시 하나가 존재해야 합니다."
            );
        }

        for (MenuOptionGroup optionGroup : optionGroups) {
            if (optionGroup.getMinimumSelectCount() < MenuOptionGroup.MINIMUM_SELECT_DEFAULT) {
                errors.reject(
                    "option_group_minimum_size.must_be_greater_than_zero",
                    new Object[]{"옵션 그룹 최소 선택 개수"},
                    String.format(
                        "옵션 그룹의 최소 선택 개수는 최소 %s 이어야 합니다.",
                        MenuOptionGroup.MINIMUM_SELECT_DEFAULT
                    )
                );
            }

            if (optionGroup.getMaximumSelectCount() < optionGroup.getMinimumSelectCount()) {
                errors.reject(
                    "option_group_maximum_size.must_be_greater_than.minimum_size",
                    new Object[]{"옵션 그룹 최대 선택 개수"},
                    "옵션 그룹의 최대 선택 개수는 최소 선택 개수보다 작을 수 없습니다."
                );
            }
        }
    }

    private boolean existsBasicOption(final List<MenuOptionGroup> optionGroups) {
        return optionGroups.stream()
            .anyMatch(optionGroup -> optionGroup.getType() == MenuOptionGroup.Type.BASIC);
    }
}
