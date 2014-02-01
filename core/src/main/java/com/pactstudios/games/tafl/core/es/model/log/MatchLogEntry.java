package com.pactstudios.games.tafl.core.es.model.log;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pactstudios.games.tafl.core.consts.Constants;
import com.pactstudios.games.tafl.core.es.model.TaflMatch;

@DatabaseTable(tableName = Constants.DbConstants.LOG_TABLE)
public class MatchLogEntry {

    public static final String ID_COLUMN = "_id";
    public static final String MATCH_ID_COLUMN = "match_id";
    public static final String TEAM_COLUMN = "team";
    public static final String SOURCE_COLUMN = "source";
    public static final String DESTINATION_COLUMN = "destination";
    public static final String CAPTURE_1_COLUMN = "capture1";
    public static final String CAPTURE_2_COLUMN = "capture2";
    public static final String CAPTURE_3_COLUMN = "capture3";
    public static final String UPDATED_COLUMN = "updated";

    // id is generated by the database and set on the object automagically
    @DatabaseField(generatedId = true)
    public int _id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = MATCH_ID_COLUMN, canBeNull = false)
    public TaflMatch match;

    @DatabaseField(columnName = TEAM_COLUMN, canBeNull = false)
    public int team;

    @DatabaseField(columnName = SOURCE_COLUMN, canBeNull = false)
    public int source;

    @DatabaseField(columnName = DESTINATION_COLUMN, canBeNull = false)
    public int destination;

    @DatabaseField(columnName = UPDATED_COLUMN, canBeNull = false)
    public Date updated;

    @DatabaseField(columnName = CAPTURE_1_COLUMN, canBeNull = false)
    public int capture1 = Constants.BoardConstants.ILLEGAL_CELL;

    @DatabaseField(columnName = CAPTURE_2_COLUMN, canBeNull = false)
    public int capture2 = Constants.BoardConstants.ILLEGAL_CELL;

    @DatabaseField(columnName = CAPTURE_3_COLUMN, canBeNull = false)
    public int capture3 = Constants.BoardConstants.ILLEGAL_CELL;


    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object other) {
        return other != null && other instanceof MatchLogEntry && (other == this || ((MatchLogEntry)other)._id == _id) ;
    }

    @Override
    public String toString() {
        return Integer.toString(_id);
    }
}
