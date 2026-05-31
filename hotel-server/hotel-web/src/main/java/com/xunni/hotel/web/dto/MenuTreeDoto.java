package com.xunni.hotel.web.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuTreeDoto {
    private Integer id;
    private Integer parentId;
    private String name;
    private String path;
    private String component;
    private String icon;
    private Integer order;
    private Integer status;
    private String menuType;
    private String menuKey;
    private Boolean hasPermission;
    private List<MenuTreeDoto> children = new ArrayList<>();
}