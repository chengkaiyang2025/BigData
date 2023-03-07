package com.yzf.di.bean;

/**
 * @author ：zhangyu
 * @date ：Created in 2021/11/18 11:24
 * @description：
 */
public class TableSource {
    private String version;
    private String connector;
    private String name;
    private Long ts_ms;
    private String snapshot;
    private String db;
    private String sequence;
    private String table;
    private Integer server_id;
    private String gtid;
    private String file;
    private Long pos;
    private Long row;
    private String thread;
    private String query;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getConnector() {
        return connector;
    }

    public void setConnector(String connector) {
        this.connector = connector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTs_ms() {
        return ts_ms;
    }

    public void setTs_ms(Long ts_ms) {
        this.ts_ms = ts_ms;
    }

    public String getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(String snapshot) {
        this.snapshot = snapshot;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Integer getServer_id() {
        return server_id;
    }

    public void setServer_id(Integer server_id) {
        this.server_id = server_id;
    }

    public String getGtid() {
        return gtid;
    }

    public void setGtid(String gtid) {
        this.gtid = gtid;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Long getPos() {
        return pos;
    }

    public void setPos(Long pos) {
        this.pos = pos;
    }

    public Long getRow() {
        return row;
    }

    public void setRow(Long row) {
        this.row = row;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "TableSource{" +
                "version='" + version + '\'' +
                ", connector='" + connector + '\'' +
                ", name='" + name + '\'' +
                ", ts_ms=" + ts_ms +
                ", snapshot='" + snapshot + '\'' +
                ", db='" + db + '\'' +
                ", sequence='" + sequence + '\'' +
                ", table='" + table + '\'' +
                ", server_id=" + server_id +
                ", gtid='" + gtid + '\'' +
                ", file='" + file + '\'' +
                ", pos=" + pos +
                ", row=" + row +
                ", thread='" + thread + '\'' +
                ", query='" + query + '\'' +
                '}';
    }
}
