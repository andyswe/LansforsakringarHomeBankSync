package lansforsakringar.api.sync;

import lansforsakringar.api.beans.Account;

import java.util.HashMap;
import java.util.Map;

public class SyncProperties {

    public static final String BASE_DIR="E:\\Andreas\\Ekonomi";

    public static final String SYNC_DIR=BASE_DIR+"\\sync";
    public static final String SYNC_DATA_FILE=SYNC_DIR+"\\sync.data";
    public static final String HOME_BANK_FILE=BASE_DIR+"\\ANDREAS_Homebank.xhb";
    public static final Map accounts = new HashMap<Account,Integer>();
    public static final String BACKUP_DIR = BASE_DIR+"\\backups";

    static {
        Account account = new Account();
        account.setAccountNumber("your_account_number");
        accounts.put(account,2);

    }


}
