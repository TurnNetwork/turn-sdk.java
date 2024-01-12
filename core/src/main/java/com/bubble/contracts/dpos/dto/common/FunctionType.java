package com.bubble.contracts.dpos.dto.common;

public class FunctionType {

    /**
     * 发起质押
     */
    public static final int STAKING_FUNC_TYPE = 1000;
    /**
     * 修改质押信息
     */
    public static final int UPDATE_STAKING_INFO_FUNC_TYPE = 1001;
    /**
     * 增持质押
     */
    public static final int ADD_STAKING_FUNC_TYPE = 1002;
    /**
     * 撤销质押(一次性发起全部撤销，多次到账)
     */
    public static final int WITHDREW_STAKING_FUNC_TYPE = 1003;
    /**
     * 发起委托
     */
    public static final int DELEGATE_FUNC_TYPE = 1004;
    /**
     * 减持/撤销委托(全部减持就是撤销)
     */
    public static final int WITHDREW_DELEGATE_FUNC_TYPE = 1005;
    /**
     * 领取解锁的委托
     */
    public static final int REDEEM_DELEGATE_FUNC_TYPE = 1006;
    /**
     * 查询当前结算周期的验证人队列
     */
    public static final int GET_VERIFIERLIST_FUNC_TYPE = 1100;
    /**
     * 查询当前共识周期的验证人列表
     */
    public static final int GET_VALIDATORLIST_FUNC_TYPE = 1101;
    /**
     * 查询所有实时的候选人列表
     */
    public static final int GET_CANDIDATELIST_FUNC_TYPE = 1102;
    /**
     * 查询当前账户地址所委托的节点的NodeID和质押Id
     */
    public static final int GET_DELEGATELIST_BYADDR_FUNC_TYPE = 1103;
    /**
     * 查询当前单个委托信息
     */
    public static final int GET_DELEGATEINFO_FUNC_TYPE = 1104;
    /**
     * 查询当前节点的质押信息
     */
    public static final int GET_STAKINGINFO_FUNC_TYPE = 1105;
    /**
     * 查询账户处于锁定期与解锁期的委托信息
     */
    public static final int GET_DELEGATIONLOCKINFO_FUNC_TYPE = 1106;
    /**
     * 查询当前结算周期的区块奖励
     */
    public static final int GET_PACKAGEREWARD_FUNC_TYPE = 1200;
    /**
     * 查询当前结算周期的质押奖励
     */
    public static final int GET_STAKINGREWARD_FUNC_TYPE = 1201;
    /**
     * 查询打包区块的平均时间
     */
    public static final int GET_AVGPACKTIME_FUNC_TYPE = 1202;
    /**
     * 提交文本提案
     */
    public static final int SUBMIT_TEXT_FUNC_TYPE = 2000;
    /**
     * 提交升级提案
     */
    public static final int SUBMIT_VERSION_FUNC_TYPE = 2001;
    /**
     * 提交参数提案
     */
    public static final int SUBMIR_PARAM_FUNCTION_TYPE = 2002;
    /**
     * 给提案投票
     */
    public static final int VOTE_FUNC_TYPE = 2003;
    /**
     * 版本声明
     */
    public static final int DECLARE_VERSION_FUNC_TYPE = 2004;
    /**
     * 提交取消提案
     */
    public static final int SUBMIT_CANCEL_FUNC_TYPE = 2005;
    /**
     * 查询提案
     */
    public static final int GET_PROPOSAL_FUNC_TYPE = 2100;
    /**
     * 查询提案结果
     */
    public static final int GET_TALLY_RESULT_FUNC_TYPE = 2101;
    /**
     * 查询提案列表
     */
    public static final int GET_PROPOSAL_LIST_FUNC_TYPE = 2102;
    /**
     * 查询提案生效版本
     */
    public static final int GET_ACTIVE_VERSION = 2103;
    /**
     * 查询当前块高的治理参数值
     */
    public static final int GET_GOVERN_PARAM_VALUE = 2104;
    /**
     * 查询提案的累积可投票人数
     */
    public static final int GET_ACCUVERIFIERS_COUNT = 2105;
    /**
     * 查询可治理列表
     */
    public static final int GET_PARAM_LIST = 2106;
    /**
     * 举报双签
     */
    public static final int REPORT_DOUBLESIGN_FUNC_TYPE = 3000;
    /**
     * 查询节点是否已被举报过多签
     */
    public static final int CHECK_DOUBLESIGN_FUNC_TYPE = 3001;
    /**
     * 创建锁仓计划
     */
    public static final int CREATE_RESTRICTINGPLAN_FUNC_TYPE = 4000;
    /**
     * 获取锁仓信息
     */
    public static final int GET_RESTRICTINGINFO_FUNC_TYPE = 4100;
    /**
     * 提取账户当前所有的可提取的委托奖励
     */
    public static final int WITHDRAW_DELEGATE_REWARD_FUNC_TYPE = 5000;
    /**
     * 查询账户在各节点未提取委托奖励
     */
    public static final int GET_DELEGATE_REWARD_FUNC_TYPE = 5100;

    /**
     * 发起L2质押
     */
    public static final int L2_STAKING_FUNC_TYPE = 7000;

    /**
     * 查询L2质押信息
     */
    public static final int GET_L2_STAKING_INFO_FUNC_TYPE = 7103;

    /**
     * 撤销L2质押(一次性发起全部撤销，多次到账)
     */
    public static final int WITHDREW_L2_STAKING_FUNC_TYPE = 7003;

    /**
     * 修改L2质押信息
     */
    public static final int UPDATE_L2_STAKING_INFO_FUNC_TYPE = 7001;

    /**
     * 增持L2质押
     */
    public static final int ADD_L2_STAKING_FUNC_TYPE = 6004;

    /**
     * 创建子链
     */
    public static final int CREATE_BUBBLE_FUNC_TYPE = 8001;

    /**
     * 释放子链
     */
    public static final int RELEASE_BUBBLE_FUNC_TYPE = 8002;

    /**
     * 发起L2Token质押
     */
    public static final int L2_TOKEN_STAKING_FUNC_TYPE = 8003;

    /**
     * 赎回L2Token质押
     */
    public static final int WITHDREW_L2_TOKEN_STAKING_FUNC_TYPE = 8004;

    /**
     * 结算
     */
    public static final int SETTLE_BUBBLE_FUNC_TYPE = 8005;

    /**
     * 查询子链信息
     */
    public static final int GET_BUBBLE_INFO_FUNC_TYPE = 8100;

    /**
     * 根据子链Hash查询主链Hash
     */
    public static final int GET_L1_HASH_BY_L2_HASH_FUNC_TYPE = 8101;

    /**
     * 获取Bubble网络在主链上的交易hash列表
     */
    public static final int GET_BUB_TX_HASH_LIST_FUNC_TYPE = 8102;

    /**
     * 铸币
     */
    public static final int MINT_TOKEN_FUNC_TYPE = 6000;

    /**
     * 结算
     */
    public static final int L2_SETTLE_BUBBLE_FUNC_TYPE = 6001;

    /**
     * 根据主链Hash获取子链Hash
     */
    public static final int GET_L2_HASH_BY_L1_HASH_FUNC_TYPE = 6100;

}
