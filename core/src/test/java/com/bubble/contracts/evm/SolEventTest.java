package com.bubble.contracts.evm;

import com.bubble.protocol.core.methods.response.TransactionReceipt;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class SolEventTest extends BaseContractTest {

    private String address = "atp1y33t47jmnuvu8c444ccrlgu34rswtstztakn9p";

    @Test
    public void deployByT() throws Exception {
        SolEvent contract = SolEvent.deploy(web3j, transactionManager, gasProvider).send();
        System.out.println("address="+contract.getContractAddress());
    }

    @Test
    public void deployByC() throws Exception {
        SolEvent contract = SolEvent.deploy(web3j, credentials, gasProvider).send();
        System.out.println("address="+contract.getContractAddress());
    }

    @Test
    public void setNameAndEmitAddress() throws Exception {

        SolEvent contract =  loadT();

        TransactionReceipt transactionReceipt = contract.setNameAndEmitAddress("x", address).send();
        System.out.println("hash = " + transactionReceipt.getTransactionHash());
        List<SolEvent.SetNameEventEventResponse> tokenAddress = contract.getSetNameEventEvents(transactionReceipt);

        assertThat(tokenAddress.size(), is(1));

        tokenAddress.stream().forEach(
                event -> {
                    assertThat(event.name1, is("x"));
                    assertThat(event.addr, is(address));
                    System.out.println(event.log + "  "  + event.name1 + "  " + event.name2 + "  " + event.addr);
                }
        );

        contract =  loadD();
        transactionReceipt = contract.setNameAndEmitAddress("y", address).send();
        System.out.println("hash = " + transactionReceipt.getTransactionHash());
        tokenAddress = contract.getSetNameEventEvents(transactionReceipt);

        assertThat(tokenAddress.size(), is(1));

        tokenAddress.stream().forEach(
                event -> {
                    assertThat(event.name1, is("y"));
                    assertThat(event.addr, is(address));
                    System.out.println(event.log + "  "  + event.name1 + "  " + event.name2 + "  " + event.addr);
                }
        );
    }


    @Test
    public void getAddress() throws Exception {

        SolEvent contract =  loadT();

        String result = contract.getAddress().send();
        assertThat(result, is(credentialsAddress));
        System.out.println("address = " + result);

        contract =  loadD();
        result = contract.getAddress().send();
        assertThat(result, is(credentialsAddress));
        System.out.println("address = " + result);

    }

    @Test
    public void getName() throws Exception {

        SolEvent contract =  loadT();

        String result = contract.getName().send();
        assertThat(result, is("y"));
        System.out.println("name = " + result);

        contract =  loadD();
        result = contract.getName().send();
        assertThat(result, is("y"));
        System.out.println("name = " + result);

    }

    private SolEvent loadT(){
        return SolEvent.load(address, web3j, transactionManager, gasProvider);
    }

    private SolEvent loadD(){
        return SolEvent.load(address, web3j, credentials, gasProvider);
    }
}
