package com.mytextile.notification.properties.bean;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
@Getter
public class BuildInfo {

    @Value("${build.version}")
    private String buildVersion;
}
