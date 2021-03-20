package com.whatsub.honeybread.common.support;

import springfox.documentation.service.Tag;

public abstract class HoneyBreadSwaggerTags {
    public static final String ALL = "[00] 전체보기";
    public static final String TEST = "[90] 테스트";
    public static final String COMMON = "[99] 공통";

    public static Tag defaultTag() {
        return new Tag(ALL, "전체", 0);
    }
}
