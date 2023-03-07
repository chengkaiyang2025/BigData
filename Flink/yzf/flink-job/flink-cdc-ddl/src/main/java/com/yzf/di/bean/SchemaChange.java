package com.yzf.di.bean;

import java.util.List;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/11/18 11:10
 * @description：
 */
public class SchemaChange {
    private TableSource source;
    private String databaseName;
    private String ddl;
    private List<TableChanges> tableChanges;

    public TableSource getSource() {
        return source;
    }

    public void setSource(TableSource source) {
        this.source = source;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDdl() {
        return ddl;
    }

    public void setDdl(String ddl) {
        this.ddl = ddl;
    }

    public List<TableChanges> getTableChanges() {
        return tableChanges;
    }

    public void setTableChanges(List<TableChanges> tableChanges) {
        this.tableChanges = tableChanges;
    }

    @Override
    public String toString() {
        return "SchemaChange{" +
                "source=" + source +
                ", databaseName='" + databaseName + '\'' +
                ", ddl='" + ddl + '\'' +
                ", tableChanges=" + tableChanges +
                '}';
    }
}
