package com.yangqi.recommendedsystem.common;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author xiaoer
 * @date 2020/2/23 11:11
 */
public class CommonUtil {
    /**
     * 对错误信息进行拼接
     *
     * @param bindingResult 错误信息参数
     * @return 拼接后的错误信息
     */
    public static String processErrorString(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getDefaultMessage()).append("，");
        }
         return stringBuilder.substring(0, stringBuilder.length() - 1);
    }
}
