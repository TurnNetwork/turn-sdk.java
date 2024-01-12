package com.bubble.parameters;



import java.util.HashMap;
import java.util.Map;

public class NetworkParameters {
    protected long chainId;
    protected static NetworkParameters currentNetwork;
    private static Map<String, NetworkParameters> networksContainer = new HashMap<>();
    private static String bubbleNetworkKey = String.valueOf(ReservedChainId.Bubble.getChainId());

    static {

        NetworkParameters bubble = new NetworkParameters(ReservedChainId.Bubble.getChainId());
        networksContainer.put(bubbleNetworkKey, bubble);

        // he default context loads the bubble mainnet
        currentNetwork=bubble;
    }

    //锁仓合约地址
    protected String dposContractAddressOfRestrictingPlan;
    //staking合约地址
    protected String dposContractAddressOfStaking;
    //激励池地址
    protected String dposContractAddressOfIncentivePool;
    //惩罚合约地址
    protected String dposContractAddressOfSlash;
    //治理合约地址
    protected String dposContractAddressOfProposal;
    //委托收益合约地址
    protected String dposContractAddressOfReward;
    protected String dposContractAddressOfL2Staking;
    protected String dposContractAddressOfBubble;
    protected String dposContractAddressOfBubbleToken;

    public static long getChainId() {
        return currentNetwork.chainId;
    }

    public static String getDposContractAddressOfRestrctingPlan() {
        return currentNetwork.dposContractAddressOfRestrictingPlan;
    }

    public static String getDposContractAddressOfStaking() {
        return currentNetwork.dposContractAddressOfStaking;
    }

    public static String getDposContractAddressOfL2Staking() {
        return currentNetwork.dposContractAddressOfL2Staking;
    }

    public static String getDposContractAddressOfBubble() {
        return currentNetwork.dposContractAddressOfBubble;
    }

    public static String getDposContractAddressOfBubbleToken() {
        return currentNetwork.dposContractAddressOfBubbleToken;
    }

    public static String getDposContractAddressOfIncentivePool() {
        return currentNetwork.dposContractAddressOfIncentivePool;
    }

    public static String getDposContractAddressOfSlash() {
        return currentNetwork.dposContractAddressOfSlash;
    }

    public static String getDposContractAddressOfProposal() {
        return currentNetwork.dposContractAddressOfProposal;
    }

    public static String getDposContractAddressOfReward() {
        return currentNetwork.dposContractAddressOfReward;
    }


    protected NetworkParameters(){
    }

    protected NetworkParameters(Long chainID){
        this.chainId = chainID;

        this.dposContractAddressOfRestrictingPlan = InnerContracts.getRestrictingAddr();
        this.dposContractAddressOfStaking = InnerContracts.getStakingAddr();
        this.dposContractAddressOfIncentivePool = InnerContracts.getRewardManagerPoolAddr();
        this.dposContractAddressOfSlash = InnerContracts.getSlashingAddr();
        this.dposContractAddressOfProposal = InnerContracts.getGovAddr();
        this.dposContractAddressOfReward = InnerContracts.getDelegateRewardPoolAddr();
        this.dposContractAddressOfL2Staking = InnerContracts.getL2StakingAddr();
        this.dposContractAddressOfBubble = InnerContracts.getBubbleAddr();
        this.dposContractAddressOfBubbleToken = InnerContracts.getBubbleTokenAddr();
    }


    /**
     * init a custom network, and this network will be the current one.
     * @param chainId  chainId, it cannot be same as the  Bubble network's id.
     */
    public static void init(long chainId){
        if(networksContainer.containsKey(String.valueOf(chainId))){
            currentNetwork = networksContainer.get(String.valueOf(chainId));
        }
    }

    /**
     * switch the current network
     * @param chainId the custom network's id
     */
    public static void selectNetwork(long chainId){
        if(networksContainer.keySet().size()==1){
            //currentNetwork cannot switch to another if only one network has been initialized.
            return;
        }
        currentNetwork = networksContainer.get(String.valueOf(chainId));
    }

    /**
     * switch to the Bubble network
     */
    public static void selectBubble(){
        currentNetwork = networksContainer.get(bubbleNetworkKey);
    }

    public enum ReservedChainId {
        Bubble(2501L);

        private final long chainId;
        ReservedChainId(long chainId){
            this.chainId = chainId;
        }
        public long getChainId(){
            return this.chainId;
        }
    }
}
