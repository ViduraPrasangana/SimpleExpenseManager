package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    public PersistentTransactionDAO(Context context) {
        this.context = context;
        dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.TRANSACTION_DATE, dateFormat.format(date));
        contentValue.put(DatabaseHelper.TRANSACTION_ACCOUNT_NO, accountNo);
        contentValue.put(DatabaseHelper.EXPENSE_TYPE, expenseType.name());
        contentValue.put(DatabaseHelper.AMOUNT,amount);
        database.insert(DatabaseHelper.TABLE_NAME_2, null, contentValue);

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        String[] columns = new String[]{DatabaseHelper.TRANSACTION_ID,
                DatabaseHelper.TRANSACTION_DATE,
                DatabaseHelper.TRANSACTION_ACCOUNT_NO,
                DatabaseHelper.EXPENSE_TYPE,
                DatabaseHelper.AMOUNT,};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_2, columns, null, null, null, null, null);
        ArrayList<Transaction> transactions = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    transactions.add(new Transaction(dateFormat.parse(cursor.getString(1)),cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)),cursor.getDouble(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        String[] columns = new String[]{DatabaseHelper.TRANSACTION_ID,
                DatabaseHelper.TRANSACTION_DATE,
                DatabaseHelper.TRANSACTION_ACCOUNT_NO,
                DatabaseHelper.EXPENSE_TYPE,
                DatabaseHelper.AMOUNT,};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_2, columns, null, null, null, null, null,String.valueOf(limit));
        ArrayList<Transaction> transactions = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext() ) {
                try {
                    transactions.add(new Transaction(dateFormat.parse(cursor.getString(1)),cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)),cursor.getDouble(4)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return transactions;
    }
}
