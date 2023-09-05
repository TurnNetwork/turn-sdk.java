package com.bubble.parameters;



import java.util.Arrays;
import java.util.List;

public class InnerContracts {
    private static String RestrictingAddr = "0x1000000000000000000000000000000000000001";
    private static String StakingAddr = "0x1000000000000000000000000000000000000002";
    private static String RewardManagerPoolAddr = "0x1000000000000000000000000000000000000003";
    private static String SlashingAddr = "0x1000000000000000000000000000000000000004";
    private static String GovAddr = "0x1000000000000000000000000000000000000005";
    private static String DelegateRewardPoolAddr = "0x1000000000000000000000000000000000000006";
    private static String L2StakingAddr = "0x2000000000000000000000000000000000000001";
    private static String BubbleAddr = "0x2000000000000000000000000000000000000002";

    private static String BubbleTokenAddr = "0x1000000000000000000000000000000000000020";

    private static List<String> InnerAddrList = Arrays.asList(RestrictingAddr, StakingAddr, RewardManagerPoolAddr, SlashingAddr, GovAddr, DelegateRewardPoolAddr, L2StakingAddr , BubbleAddr, BubbleTokenAddr);


    public static String getRestrictingAddr() {
        return RestrictingAddr;
    }

    public static String getStakingAddr() {
        return StakingAddr;
    }

    public static String getRewardManagerPoolAddr() {
        return RewardManagerPoolAddr;
    }

    public static String getSlashingAddr() {
        return SlashingAddr;
    }

    public static String getGovAddr() {
        return GovAddr;
    }

    public static String getDelegateRewardPoolAddr() {
        return DelegateRewardPoolAddr;
    }

    public static List<String> getInnerAddrList() {
        return InnerAddrList;
    }

    public static boolean isInnerAddr(String address){
        return InnerAddrList.contains(address);
    }

    public static String getL2StakingAddr() {
        return L2StakingAddr;
    }

    public static String getBubbleAddr() {
        return BubbleAddr;
    }

    public static String getBubbleTokenAddr() {
        return BubbleTokenAddr;
    }
}
