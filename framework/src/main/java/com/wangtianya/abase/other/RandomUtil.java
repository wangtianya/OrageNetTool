/*
 * Copyright (C) 2015 wangtianya.com, Inc. All Rights Reserved.
 */
package com.wangtianya.abase.other;

import java.util.Random;

/**
 * Created by wangtianya on 15/9/6.
 */
public class RandomUtil {
    public static int getNumber(int min, int max) {
        return min + (new Random().nextInt(max - min));
    }
}