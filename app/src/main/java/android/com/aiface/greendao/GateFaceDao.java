package android.com.aiface.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import android.com.aiface.database.bean.GateFace;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GATE_FACE".
*/
public class GateFaceDao extends AbstractDao<GateFace, Long> {

    public static final String TABLENAME = "GATE_FACE";

    /**
     * Properties of entity GateFace.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserName = new Property(1, String.class, "userName", false, "USER_NAME");
        public final static Property UserId = new Property(2, String.class, "userId", false, "USER_ID");
        public final static Property CheckTime = new Property(3, String.class, "checkTime", false, "CHECK_TIME");
    }


    public GateFaceDao(DaoConfig config) {
        super(config);
    }
    
    public GateFaceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GATE_FACE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USER_NAME\" TEXT NOT NULL ," + // 1: userName
                "\"USER_ID\" TEXT NOT NULL ," + // 2: userId
                "\"CHECK_TIME\" TEXT);"); // 3: checkTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GATE_FACE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GateFace entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserName());
        stmt.bindString(3, entity.getUserId());
 
        String checkTime = entity.getCheckTime();
        if (checkTime != null) {
            stmt.bindString(4, checkTime);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GateFace entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getUserName());
        stmt.bindString(3, entity.getUserId());
 
        String checkTime = entity.getCheckTime();
        if (checkTime != null) {
            stmt.bindString(4, checkTime);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public GateFace readEntity(Cursor cursor, int offset) {
        GateFace entity = new GateFace( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // userName
            cursor.getString(offset + 2), // userId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // checkTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GateFace entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserName(cursor.getString(offset + 1));
        entity.setUserId(cursor.getString(offset + 2));
        entity.setCheckTime(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GateFace entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GateFace entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GateFace entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
