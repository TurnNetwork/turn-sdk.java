package com.bubble.console;

import com.bubble.codegen.Console;
import com.bubble.crypto.Credentials;
import com.bubble.crypto.WalletUtils;
import com.bubble.protocol.Web3j;
import com.bubble.protocol.core.methods.response.TransactionReceipt;
import com.bubble.protocol.core.methods.response.Web3ClientVersion;
import com.bubble.protocol.exceptions.TransactionException;
import com.bubble.protocol.http.HttpService;
import com.bubble.tx.Transfer;
import com.bubble.utils.Convert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static com.bubble.codegen.Console.exitError;

/**
 * Simple class for creating a wallet file.
 */
public class WalletSendFunds extends WalletManager {

    private static final String USAGE = "send <walletfile> <destination-address>";

    public static void main(String[] args) {
        if (args.length != 2) {
            exitError(USAGE);
        } else {
            new WalletSendFunds().run(args[0], args[1]);
        }
    }

    private void run(String walletFileLocation, String destinationAddress) {
        File walletFile = new File(walletFileLocation);
        Credentials credentials = getCredentials(walletFile);
        console.printf("Wallet for address " + credentials.getAddress() + " loaded\n");

        if (!WalletUtils.isValidAddress(destinationAddress)) {
            exitError("Invalid destination address specified");
        }

        Web3j web3j = getBubbleClient();

        BigDecimal amountToTransfer = getAmountToTransfer();
        Convert.Unit transferUnit = getTransferUnit();
        BigDecimal amountInVon = Convert.toVon(amountToTransfer, transferUnit);

        confirmTransfer(amountToTransfer, transferUnit, amountInVon, destinationAddress);

        TransactionReceipt transactionReceipt = performTransfer(
                web3j, destinationAddress, credentials, amountInVon);

        console.printf("Funds have been successfully transferred from %s to %s%n"
                        + "Transaction hash: %s%nMined block number: %s%n",
                credentials.getAddress(),
                destinationAddress,
                transactionReceipt.getTransactionHash(),
                transactionReceipt.getBlockNumber());
    }

    private BigDecimal getAmountToTransfer() {
        String amount = console.readLine("What amound would you like to transfer "
                + "(please enter a numeric value): ")
                .trim();
        try {
            return new BigDecimal(amount);
        } catch (NumberFormatException e) {
            exitError("Invalid amount specified");
        }
        throw new RuntimeException("Application exit failure");
    }

    private Convert.Unit getTransferUnit() {
        String unit = console.readLine("Please specify the unit (KPVON, VON, ...) [KPVON]: ")
                .trim();

        Convert.Unit transferUnit;
        if (unit.equals("")) {
            transferUnit = Convert.Unit.KPVON;
        } else {
            transferUnit = Convert.Unit.fromString(unit.toLowerCase());
        }

        return transferUnit;
    }

    private void confirmTransfer(
            BigDecimal amountToTransfer, Convert.Unit transferUnit, BigDecimal amountInVon,
            String destinationAddress) {

        console.printf("Please confim that you wish to transfer %s %s (%s %s) to address %s%n",
                amountToTransfer.stripTrailingZeros().toPlainString(), transferUnit,
                amountInVon.stripTrailingZeros().toPlainString(),
                Convert.Unit.VON, destinationAddress);
        String confirm = console.readLine("Please type 'yes' to proceed: ").trim();
        if (!confirm.toLowerCase().equals("yes")) {
            exitError("OK, some other time perhaps...");
        }
    }

    private TransactionReceipt performTransfer(
            Web3j web3j, String destinationAddress, Credentials credentials,
            BigDecimal amountInVon) {

        console.printf("Commencing transfer (this may take a few minutes) ");
        try {
            Future<TransactionReceipt> future = Transfer.sendFunds(
                    web3j, credentials, destinationAddress, amountInVon, Convert.Unit.KPVON)
                    .sendAsync();

            while (!future.isDone()) {
                console.printf(".");
                Thread.sleep(500);
            }
            console.printf("$%n%n");
            return future.get();
        } catch (InterruptedException | ExecutionException | TransactionException | IOException e) {
            exitError("Problem encountered transferring funds: \n" + e.getMessage());
        }
        throw new RuntimeException("Application exit failure");
    }

    private Web3j getBubbleClient() {
        String clientAddress = console.readLine(
                "Please confirm address of running Bubble/Alaya client you wish to send "
                + "the transfer request to [" + HttpService.DEFAULT_URL + "]: ")
                .trim();

        Web3j web3j;
        if (clientAddress.equals("")) {
            web3j = Web3j.build(new HttpService());
        } else {
            web3j = Web3j.build(new HttpService(clientAddress));
        }

        try {
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().sendAsync().get();
            if (web3ClientVersion.hasError()) {
                Console.exitError("Unable to process response from client: "
                        + web3ClientVersion.getError());
            } else {
                console.printf("Connected successfully to client: %s%n",
                        web3ClientVersion.getWeb3ClientVersion());
                return web3j;
            }
        } catch (InterruptedException | ExecutionException e) {
            exitError("Problem encountered verifying client: " + e.getMessage());
        }
        throw new RuntimeException("Application exit failure");
    }
}
