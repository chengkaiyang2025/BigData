package com.yzf.di.kuducdc.kudutest.util;

import org.junit.Test;

import java.util.Random;

public class OctopusDataGeneratorTest {
    private Random random = new Random();
    @Test
    public void generator() {
        System.out.println(OctopusDataGenerator.randomGenerateOctopusData());
    }
    @Test
    public void t(){
        Long qyid = Math.abs(random.nextLong());
        String qymc = "测试"+String.valueOf(qyid);
        int kjnd = random.nextInt(2021 - 2019 + 1) + 2018;
        int kjqj = random.nextInt(12 - 1 + 1) + 1;
        String id = String.format("%s_%s_%s_778944412830113793_778944412834308097_sheet1", qyid, kjnd, kjqj);
        System.out.println(kjnd);
        System.out.println(kjqj);
        System.out.println(id);
        System.out.println(qyid);
    }
}