package org.mobilise.bms.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by: @oladapoyuken
 * Date: 05/05/2024
 */

@Component
public class BmsUtils {

    //generate random value as book ISBN
    public String generateIsbn(){
        return UUID.randomUUID().toString();
    }
}
