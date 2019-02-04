package server.protocol;

import chain.Transaction;

public class NewTransaction extends Request {
    private Transaction transaction;

    public NewTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
