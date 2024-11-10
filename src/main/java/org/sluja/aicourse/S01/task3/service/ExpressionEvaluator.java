package org.sluja.aicourse.S01.task3.service;

import org.apache.commons.lang3.StringUtils;

public class ExpressionEvaluator {


    public Integer evaluate(String expression) {
        expression = expression.replaceAll(StringUtils.SPACE, StringUtils.EMPTY);
        String[] elements = expression.split("\\+");
        return Integer.parseInt(elements[0]) + Integer.parseInt(elements[1]);
    }
}
