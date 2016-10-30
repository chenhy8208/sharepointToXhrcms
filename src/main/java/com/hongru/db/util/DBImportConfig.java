package com.hongru.db.util;

/**
 * Created by chenhongyu on 2016/10/30.
 */
public class DBImportConfig {

    private DBImportConfig() {
    }

    private String share_url;
    private String share_username;
    private String share_password;
    private String cms_url;
    private String cms_username;
    private String cms_password;
    private boolean allIn;

    public String getShare_url() {
        return share_url;
    }

    public String getShare_username() {
        return share_username;
    }

    public String getShare_password() {
        return share_password;
    }

    public String getCms_url() {
        return cms_url;
    }

    public String getCms_username() {
        return cms_username;
    }

    public String getCms_password() {
        return cms_password;
    }

    public boolean isAllIn() {
        return allIn;
    }

    public String getImport_time_area() {
        return import_time_area;
    }

    private String import_time_area;

    private static DBImportConfig build(Builder builder) {
        DBImportConfig db = new DBImportConfig();
        db.share_url = builder.share_url;
        db.share_password = builder.share_password;
        db.share_username = builder.share_username;
        db.cms_url = builder.cms_url;
        db.cms_username = builder.cms_username;
        db.cms_password = builder.cms_password;
        db.allIn = builder.allIn;
        db.import_time_area = builder.import_time_area;

        return db;
    }

    public static class Builder {
        public DBImportConfig build() {
            return DBImportConfig.build(this);
        }

        private String share_url;
        private String share_username;
        private String share_password;
        private String cms_url;
        private String cms_username;
        private String cms_password;
        private boolean allIn;
        private String import_time_area; //10/11/2016 - 10/27/2016

        public String getShare_url() {
            return share_url;
        }

        public Builder setShare_url(String share_url) {
            this.share_url = share_url;
            return this;
        }

        public String getShare_username() {
            return share_username;
        }

        public Builder setShare_username(String share_username) {
            this.share_username = share_username;
            return this;
        }

        public String getShare_password() {
            return share_password;
        }

        public Builder setShare_password(String share_password) {
            this.share_password = share_password;
            return this;
        }

        public String getCms_url() {
            return cms_url;
        }

        public Builder setCms_url(String cms_url) {
            this.cms_url = cms_url;
            return this;
        }

        public String getCms_username() {
            return cms_username;
        }

        public Builder setCms_username(String cms_username) {
            this.cms_username = cms_username;
            return this;
        }

        public String getCms_password() {
            return cms_password;
        }

        public Builder setCms_password(String cms_password) {
            this.cms_password = cms_password;
            return this;
        }

        public boolean isAllIn() {
            return allIn;
        }

        public Builder setAllIn(boolean allIn) {
            this.allIn = allIn;
            return this;
        }

        public String getImport_time_area() {
            return import_time_area;
        }

        public Builder setImport_time_area(String import_time_area) {
            this.import_time_area = import_time_area;
            return this;
        }
    }

}
