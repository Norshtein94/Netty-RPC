package cn.norshtein.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Norshtein
 * @Date: 2020/6/1
 * @Desc:
 */
@Data
public class InvocationProtocol implements Serializable {
    private String className;
    private String methodName;
    private Class<?>[] params;
    private Object[] values;
    private Class<?> returnType;
}