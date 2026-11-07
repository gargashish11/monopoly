package com.ashish.monopoly.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.CorsFilter;

import static org.junit.jupiter.api.Assertions.*;

public class JpaConfigurationTest {

    @Test
    public void testCorsFilterRegistration() {
        JpaConfiguration config = new JpaConfiguration();
        FilterRegistrationBean<CorsFilter> bean = config.corsFilterRegistration();

        assertNotNull(bean);
        assertEquals(Ordered.HIGHEST_PRECEDENCE, bean.getOrder());
        assertNotNull(bean.getFilter());
    }
}
