package org.sluja.aicourse.newClasses.utils.webcontent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebRequestBody {
    private String apikey;
    private String query;
} 