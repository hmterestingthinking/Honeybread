package com.whatsub.honeybread.core.domain.category;

import com.whatsub.honeybread.core.domain.category.dto.CategorySearch;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestConstructor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
class CategoryRepositoryTest {
    final CategoryRepository repository;

    @Test
    void 중복되는_이름이_없다면_거짓을_반환() {
        // given
        final String name = "한식";

        // when
        boolean exists = repository.existsByName(name);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void 중복되는_이름이_있다면_참을_반환() {
        // given
        final String name = "한식";

        repository.save(new Category(name));

        // when
        boolean exists = repository.existsByName(name);

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void 검색조건이_없다면_SIZE_만큼_목록을_반환() {
        // given
        final int size = 10;
        final List<Category> categories = generateCategories(size);
        repository.saveAll(categories);

        PageRequest pageRequest = PageRequest.of(0, size);
        CategorySearch search = new CategorySearch();

        // when
        Page<Category> page = repository.getCategories(pageRequest, search);

        // then
        assertThat(page.getTotalElements()).isEqualTo(size);
    }

    @Test
    void 검색조건이_있다면_해당하는_목록을_반환() {
        // given
        final int size = 10;
        final List<Category> categories = generateCategories(size);
        repository.saveAll(categories);

        PageRequest pageRequest = PageRequest.of(0, size);
        CategorySearch search = new CategorySearch();
        search.setName("카테고리1");

        // when
        Page<Category> page = repository.getCategories(pageRequest, search);

        // then
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().get(0).getName()).isEqualTo(search.getName());
    }

    private List<Category> generateCategories(final int size) {
        return IntStream.range(0, size)
            .mapToObj(value -> new Category("카테고리" + value))
            .collect(Collectors.toList());
    }
}