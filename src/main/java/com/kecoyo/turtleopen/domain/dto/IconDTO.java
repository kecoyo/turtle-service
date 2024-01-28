package com.kecoyo.turtleopen.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "图标管理的请求参数")
public class IconDTO {

    @Schema(description = "图标分类ID", example = "0")
    private Integer iconTypeId;

}