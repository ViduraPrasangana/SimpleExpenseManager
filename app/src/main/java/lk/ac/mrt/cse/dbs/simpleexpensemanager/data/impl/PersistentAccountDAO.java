package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public PersistentAccountDAO(Context context) {
        this.context = context;
    }

    public PersistentAccountDAO open() {
        dbHelper = DatabaseHelper.getInstance(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    @Override
    public List<String> getAccountNumbersList() {
        String[] columns = new String[]{DatabaseHelper.ID,
                DatabaseHelper.ACCOUNT_NO,
                DatabaseHelper.BANK_NAME,
                DatabaseHelper.ACCOUNT_HOLDER_NAME,
                DatabaseHelper.BALANCE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_1, columns, null, null, null, null, null);
        ArrayList<String> numberList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                numberList.add(cursor.getString(1));
            }
        }

        return numberList;
    }

    @Override
    public List<Account> getAccountsList() {
        String[] columns = new String[]{DatabaseHelper.ID,
                DatabaseHelper.ACCOUNT_NO,
                DatabaseHelper.BANK_NAME,
                DatabaseHelper.ACCOUNT_HOLDER_NAME,
                DatabaseHelper.BALANCE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_1, columns, null, null, null, null, null);
        ArrayList<Account> accountList = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                accountList.add(new Account(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4)));
            }
        }

        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String[] columns = new String[]{DatabaseHelper.ID,
                DatabaseHelper.ACCOUNT_NO,
                DatabaseHelper.BANK_NAME,
                DatabaseHelper.ACCOUNT_HOLDER_NAME,
                DatabaseHelper.BALANCE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_1, columns, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToNext()) {
                return new Account(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4));
            }
        }
        throw new InvalidAccountException("Account not exists");

    }

    @Override
    public void addAccount(Account account) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.ACCOUNT_NO, account.getAccountNo());
        contentValue.put(DatabaseHelper.BANK_NAME, account.getBankName());
        contentValue.put(DatabaseHelper.ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        contentValue.put(DatabaseHelper.BALANCE, account.getBalance());
        database.insert(DatabaseHelper.TABLE_NAME_1, null, contentValue);
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        int  i = database.delete(DatabaseHelper.TABLE_NAME_1, DatabaseHelper.ACCOUNT_NO + " = " + accountNo, null);
        if(i!=1){
            throw new InvalidAccountException("Account not exists");
        }
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        double sum = (expenseType == ExpenseType.EXPENSE ? -1 * amount : amount);

        String[] columns = new String[]{DatabaseHelper.ID,
                DatabaseHelper.ACCOUNT_NO,
                DatabaseHelper.BALANCE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_1, columns, DatabaseHelper.ACCOUNT_NO + " = ?",
                new String[]{accountNo}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        } else {
            throw new InvalidAccountException("Account not exists");
        }
        double updatedValue = 0;
        while (cursor.moveToNext()) {
            updatedValue = cursor.getDouble(3) + sum;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.BALANCE, updatedValue);
        database.update(DatabaseHelper.TABLE_NAME_1, contentValues, DatabaseHelper.ACCOUNT_NO + " = ?" , new String[]{accountNo} );
    }
}
