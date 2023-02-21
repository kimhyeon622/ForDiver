package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("shopMember")
public class ShopMemDTO {

    private String loginId;

    private String btCustomList;
    private String eqBasketList;






}
