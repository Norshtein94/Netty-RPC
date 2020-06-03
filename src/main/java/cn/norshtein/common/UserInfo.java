package cn.norshtein.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Norshtein
 * @Date: 2020/6/2
 * @Desc:
 */
@Data
public class UserInfo implements Serializable {
    private String name;
    private int age;
    private String address;
}