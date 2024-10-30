package com.msc.ms.users.utils;

import com.msc.ms.users.common.model.dto.filters.FilterCriteria;
import com.msc.ms.users.common.utils.FieldsUtils;
import com.msc.ms.users.user.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class ClassUtilsTest {

    @Test
    public void testPropertyFunctions() {
        final var hasNameProperty = FieldsUtils.validIfClassHasField(UserEntity.class, "name");
        final var hasNameProfileProperty = FieldsUtils.validIfClassHasField(UserEntity.class, "profile.name");
        final var notHasProperty = FieldsUtils.validIfClassHasField(UserEntity.class, "country");
        final var fields = FieldsUtils.getFieldsNames(FilterCriteria.class);
        log.info("the class  has {} fields", fields.size());
        fields.forEach(log::info);
        Assert.assertTrue(hasNameProfileProperty);
        Assert.assertTrue(hasNameProperty);
        Assert.assertFalse(notHasProperty);

    }
}
