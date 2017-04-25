package com.hk.sfs.metadata.utils;

import org.hibernate.AssertionFailure;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.internal.util.StringHelper;

import java.io.Serializable;

/**
 * @author Administrator
 * @date 2017/4/15
 */
public class TableUpperCaseImprovedNamingStrategy implements NamingStrategy, Serializable {

    private static final long serialVersionUID = 7380268465988449940L;

    public static final NamingStrategy INSTANCE = new TableUpperCaseImprovedNamingStrategy();

    public TableUpperCaseImprovedNamingStrategy() {
    }

    public String classToTableName(String className) {
        return addUnderscoresUpperCase(StringHelper.unqualify(className));
    }

    public String propertyToColumnName(String propertyName) {
        return addUnderscores(StringHelper.unqualify(propertyName));
    }

    public String tableName(String tableName) {
        return addUnderscoresUpperCase(tableName);
    }

    public String columnName(String columnName) {
        return addUnderscores(columnName);
    }

    protected static String addUnderscores(String name) {
        StringBuilder buf = new StringBuilder(name.replace('.', '_'));

        for (int i = 1; i < buf.length() - 1; ++i) {
            if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i))
                    && Character.isLowerCase(buf.charAt(i + 1))) {
                buf.insert(i++, '_');
            }
        }

        return buf.toString().toLowerCase();
    }

    protected static String addUnderscoresUpperCase(String name) {
        StringBuilder buf = new StringBuilder(name.replace('.', '_'));

        for (int i = 1; i < buf.length() - 1; ++i) {
            if (Character.isLowerCase(buf.charAt(i - 1)) && Character.isUpperCase(buf.charAt(i))
                    && Character.isLowerCase(buf.charAt(i + 1))) {
                buf.insert(i++, '_');
            }
        }

        return buf.toString().toUpperCase();
    }

    public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity,
                                      String associatedEntityTable, String propertyName) {
        return this.tableName(ownerEntityTable + '_' + this.propertyToColumnName(propertyName));
    }

    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        return this.columnName(joinedColumn);
    }

    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName,
                                       String referencedColumnName) {
        String header = propertyName != null ? StringHelper.unqualify(propertyName) : propertyTableName;
        if (header == null) {
            throw new AssertionFailure("NamingStrategy not properly filled");
        } else {
            return this.columnName(header);
        }
    }

    public String logicalColumnName(String columnName, String propertyName) {
        return StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName);
    }

    public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable,
                                             String propertyName) {
        return tableName != null ? tableName : ownerEntityTable + "_"
                + (associatedEntityTable != null ? associatedEntityTable : StringHelper.unqualify(propertyName));
    }

    public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
        return StringHelper.isNotEmpty(columnName) ? columnName : StringHelper.unqualify(propertyName) + "_"
                + referencedColumn;
    }
}
