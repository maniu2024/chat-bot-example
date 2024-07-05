package org.example.utils;

import lombok.Builder;
import lombok.Data;

/**
 * 段落风格和内容实体
 */
@Data
@Builder
public class StyleTextVO {
    private Integer titleLevel;
    private String style;
    private String type;
    private String text;
    private String content;
}