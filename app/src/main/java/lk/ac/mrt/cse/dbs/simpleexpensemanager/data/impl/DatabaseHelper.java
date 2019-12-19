package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    static DatabaseHelper instance;

    static final String DB_NAME = "170708T";
    static final int DB_VERSION = 2;

    public static final String TABLE_NAME_1="accounts";
    public static final String ID = "id";
    public static final String ACCOUNT_NO = "account_no";
    public static final String BANK_NAME = "bank_name";
    public static final String ACCOUNT_HOLDER_NAME = "account_holder_name";
    public static final String BALANCE = "balance";
    private static final String CREATE_TABLE_1 = "create table " + TABLE_NAME_1 + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + ACCOUNT_NO + " TEXT NOT NULL UNIQUE, "
            + BANK_NAME + " TEXT, "
            + ACCOUNT_HOLDER_NAME + " TEXT, "
            + BALANCE + " INTEGER(11) );";

    public static final String TABLE_NAME_2="transactions";
    public static final String TRANSACTION_ID = "id";
    public static final String TRANSACTION_DATE = "date";
    public static final String TRANSACTION_ACCOUNT_NO = "account_no";
    public static final String EXPENSE_TYPE = "expense_type";
    public static final String AMOUNT = "amount";
    private static final String CREATE_TABLE_2 = "create table " + TABLE_NAME_2 + "(" + TRANSACTION_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TRANSACTION_DATE + " TEXT, "
            + TRANSACTION_ACCOUNT_NO + " TEXT NOT NULL UNIQUE, "
            + EXPENSE_TYPE + " TEXT CHECK( "+EXPENSE_TYPE+" IN ('INCOME','EXPENSE') ), "
            + AMOUNT + " REAL );";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_1);
        sqLiteDatabase.execSQL(CREATE_TABLE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(sqLiteDatabase);
    }

    public synchronized static DatabaseHelper getInstance(Context context) {
        if(instance==null){
            instance = new DatabaseHelper(context);
        }
        return instance;
    }
}
