package lansforsakringar.api.sync;

import lansforsakringar.api.beans.Account;
import lansforsakringar.api.beans.Transaction;
import lansforsakringar.api.beans.TransactionList;
import lansforsakringar.api.services.RestClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import static java.awt.SystemColor.text;

public class Synchronizer implements BiConsumer<Account, Integer> {


    private final RestClient client;


    public Synchronizer(RestClient client) throws NoSuchAlgorithmException {

        this.client = client;
    }

    public void accept(Account account, Integer homeBankAccount) {
        System.out.println("Synchronizing account " + account);
        try {
            TransactionList transactions = client.getTransactions(account, 0);
            List<String> syncedTransactions = getSyncedTransactions();
            List<Transaction> newTransactions = new ArrayList<Transaction>();

            boolean continueParsing = true;
            while (continueParsing) {
                continueParsing = parse(transactions, syncedTransactions, newTransactions);
                continueParsing &= transactions.getHasMore();
                if (continueParsing) {
                    transactions = client.getTransactions(account, transactions.getNextSequenceNumber());
                }
            }
            System.out.println(newTransactions.size() + " new transactions were found");
            handleNewTransactions(newTransactions, account, homeBankAccount);
        } catch (Exception e) {
            throw new RuntimeException("Failed to synchronize account " + account, e);
        }
    }

    private void handleNewTransactions(List<Transaction> newTransactions, Account account, Integer homeBankAccount) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        List<Map> newNodes = new ArrayList<Map>();
        for (Transaction t : newTransactions) {
            newNodes.add(createNode(t, homeBankAccount));

        }
        addNewNodesToHomeBank(newNodes);
        List<String> newHashes = newTransactions.stream().filter(p -> p != null).map(p -> p.sha1hash()).collect(Collectors.toList());
        FileUtils.writeLines(new File(SyncProperties.SYNC_DATA_FILE), newHashes, true);
    }

    private void addNewNodesToHomeBank(List<Map> newNodes) throws IOException, ParserConfigurationException, SAXException, TransformerException {
        if (newNodes.isEmpty()) {
            return;
        }
        File homeBankFile = new File(SyncProperties.HOME_BANK_FILE);
        File backup = new File(SyncProperties.BACKUP_DIR + "\\Homebank_backup_" + System.currentTimeMillis() + ".xhb");
        FileUtils.copyFile(homeBankFile, backup);

        List<String> strings = FileUtils.readLines(homeBankFile,"Cp1252");
        strings.remove(strings.size() - 1);


        for (Map<String, String> m : newNodes) {
            String line = "<ope ";

            for (Map.Entry<String, String> e : m.entrySet()) {
                line += e.getKey() + "=\"" + e.getValue() + "\" ";
            }
            line += "/>";
            strings.add(line);
        }
        strings.add("</homebank>");
        FileUtils.writeLines(homeBankFile, "Cp1252",strings, false);
    }


    private Map<String, String> createNode(Transaction t, Integer account) {
        //<ope date="736696" amount="-140" account="2" paymode="1" info="JARVSOBACKEN JERVSO" />
        HashMap<String, String> map = new HashMap<>();
        String paymode = t.getAmmount() > 0.0 ? "9" : "1";
        map.put("paymode", paymode);
        map.put("date", t.getHomeBankDateFormat().toString());
        map.put("amount", t.getAmmount().toString());
        map.put("account", account.toString());
        map.put("info", ansify(t.getText()));
        map.put("autoImported", "true");

        return map;
    }

    private String ansify(String text) {

        return text.replaceAll("[^A-Za-z0-9 ]", "_");
    }

    /**
     * @param transactions
     * @param syncedTransactions
     * @param newTransactions
     * @return true if all transactions in the transaction list were new
     */

    private boolean parse(TransactionList transactions, List<String> syncedTransactions, List<Transaction> newTransactions) {
        for (Transaction t : transactions.getTransactions()) {
            String hash = t.sha1hash();
            boolean isOldTransaction = syncedTransactions.contains(hash);
            if (isOldTransaction) {
                return false;
            }
            System.out.println("        Adding new transaction " + t);
            newTransactions.add(t);
        }
        return true;
    }


    private List getSyncedTransactions() throws IOException {
        File syncFile = new File(SyncProperties.SYNC_DATA_FILE);
        if (!syncFile.exists()) {
            syncFile.createNewFile();
        }
        List<String> list = FileUtils.readLines(syncFile, "UTF-8");
        System.out.println(list.size() + " transactions have already been synced");
        return list;
    }
}
