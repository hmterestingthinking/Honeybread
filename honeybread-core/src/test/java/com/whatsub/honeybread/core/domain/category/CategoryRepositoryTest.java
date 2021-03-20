package com.whatsub.honeybread.core.domain.category;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
@RequiredArgsConstructor
class CategoryRepositoryTest {
    final CategoryRepository repository;

    @Test
    void 중복되는_이름이_없다면_거짓을_반환한다() {
        // given
        String name = "한식";

        // when
        boolean exists = repository.existsByName(name);

        // then
        assertThat(exists).isFalse();
    }

    @Test
    void 중복되는_이름이_있다면_참을_반환한다() {
        // given
        String name = "한식";

        repository.save(new Category(name));

        // when
        boolean exists = repository.existsByName(name);

        // then
        assertThat(exists).isTrue();
    }
}