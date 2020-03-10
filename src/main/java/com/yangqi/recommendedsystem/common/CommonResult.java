package com.yangqi.recommendedsystem.common;

/**
 * @author xiaoer
 * @date 2020/2/23 9:10
 */
public class CommonResult {
    /**
     * 表明对应请求的返回处理结果，"success" 或 "fail"
     */
    private String status;

    /**
     * 若 status = success 时，表明对应的返回的 json 类数据
     * 若 status = fail 时，则 data 内将使用通用的错误码对应的格式
     */
    private Object data;

    /**
     * 定义一个通用的创建返回对象的方法
     *
     * @param result 结果对象
     * @return 通用结果对象
     */
    public static CommonResult create(Object result) {
        return CommonResult.create(result, "success");
    }

    /**
     *
     * @param result 结果对象
     * @param status 请求状态码
     * @return 通用结果对象
     */
    public static CommonResult create(Object result, String status) {
        CommonResult commonResult = new CommonResult();
        commonResult.setStatus(status);
        commonResult.setData(result);

        return commonResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
